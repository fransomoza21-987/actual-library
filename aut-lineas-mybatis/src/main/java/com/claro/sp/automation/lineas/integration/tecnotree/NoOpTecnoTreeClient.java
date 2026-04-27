package com.claro.sp.automation.lineas.integration.tecnotree;

import com.claro.sp.automation.lineas.domain.line.CreationContext;
import com.claro.sp.automation.lineas.domain.line.LineRequest;

public class NoOpTecnoTreeClient implements TecnoTreeClient {
    @Override
    public boolean subscriberExists(String subId) {
        return false;
    }

    @Override
    public void createSubscriber(CreationContext context) {
        // Reemplazar por el cliente real de Tecnotree del entorno.
    }

    @Override
    public void updateSubscriber(LineRequest request) {
        // Reemplazar por el cliente real de Tecnotree del entorno.
    }

    @Override
    public void deleteSubscriber(String subId) {
        // Reemplazar por el cliente real de Tecnotree del entorno.
    }
}
