package com.claro.sp.automation.lineas.application.creation;

import com.claro.sp.automation.lineas.application.common.LineValidator;
import com.claro.sp.automation.lineas.domain.country.CountryDefaultsResolver;
import com.claro.sp.automation.lineas.domain.line.CreationContext;
import com.claro.sp.automation.lineas.domain.line.LineCreationRequest;
import com.claro.sp.automation.lineas.domain.line.LineResult;
import com.claro.sp.automation.lineas.exception.LineasException;
import com.claro.sp.automation.lineas.integration.tecnotree.TecnoTreeClient;
import com.claro.sp.automation.lineas.repository.LineRepository;

public class MyBatisLineCreator implements LineCreator {
    private static final int MAX_GENERATION_ATTEMPTS = 30;

    private final LineRepository repository;
    private final CountryDefaultsResolver defaultsResolver;
    private final LineValidator<LineCreationRequest> validator;
    private final TecnoTreeClient tecnoTreeClient;

    public MyBatisLineCreator(LineRepository repository, CountryDefaultsResolver defaultsResolver,
                              LineValidator<LineCreationRequest> validator, TecnoTreeClient tecnoTreeClient) {
        this.repository = repository;
        this.defaultsResolver = defaultsResolver;
        this.validator = validator;
        this.tecnoTreeClient = tecnoTreeClient;
    }

    @Override
    public LineResult create(LineCreationRequest request) {
        validator.validate(request);
        CreationContext context = new CreationContext(request);
        defaultsResolver.applyTo(context);
        completeNumberData(context);
        completeHandle(context);

        try {
            repository.createLine(context);
            tecnoTreeClient.createSubscriber(context);
            tecnoTreeClient.updateSubscriber(context);
            return repository.toResult(context);
        } catch (RuntimeException e) {
            safeRollback(context);
            if (e instanceof LineasException lineasException) {
                throw lineasException;
            }
            throw e;
        }
    }

    private void completeNumberData(CreationContext context) {
        if (hasText(context.getCellularNumber())) {
            if (!repository.isCellularAvailable(context.getCellularNumber())) {
                throw new LineasException("La linea ya existe en PROD: " + context.getCellularNumber());
            }
        } else {
            context.setCellularNumber(generateAvailableNumber(context));
        }

        if (context.isDistinctBillNumber()) {
            if (!hasText(context.getBillNumber())) {
                context.setBillNumber(generateAvailableNumber(context));
            }
            if (!repository.isCellularAvailable(context.getBillNumber())) {
                throw new LineasException("El billNumber ya existe en PROD: " + context.getBillNumber());
            }
        } else {
            context.setBillNumber(context.getCellularNumber());
        }
    }

    private String generateAvailableNumber(CreationContext context) {
        String prefix = repository.findCcardPrefix();
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            String nim = repository.findAvailableNumber(context);
            String subId = prefix + nim;
            if (repository.isCellularAvailable(nim) && !tecnoTreeClient.subscriberExists(subId)) {
                return nim;
            }
        }
        throw new LineasException("No se pudo generar una linea libre luego de " + MAX_GENERATION_ATTEMPTS + " intentos");
    }

    private void completeHandle(CreationContext context) {
        if (hasText(context.getHandle())) {
            if (!repository.isHandleAvailable(context.getHandle())) {
                throw new LineasException("El handle ya existe en CCARD: " + context.getHandle());
            }
            return;
        }
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            String handle = repository.findNextHandle();
            if (repository.isHandleAvailable(handle)) {
                context.setHandle(handle);
                return;
            }
        }
        throw new LineasException("No se pudo generar un handle libre");
    }

    private void safeRollback(CreationContext context) {
        try {
            repository.rollbackCreatedLine(context);
        } catch (RuntimeException rollbackError) {
            // Preserve the creation error; rollback failures must be logged by the caller/infrastructure.
        }
        if (context.getSubId() != null) {
            try {
                tecnoTreeClient.deleteSubscriber(context.getSubId());
            } catch (RuntimeException rollbackError) {
                // Preserve the creation error; Tecnotree rollback can be retried externally.
            }
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}


