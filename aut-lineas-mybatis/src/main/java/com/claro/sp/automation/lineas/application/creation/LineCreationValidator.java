package com.claro.sp.automation.lineas.application.creation;

import com.claro.sp.automation.lineas.application.common.LineValidator;
import com.claro.sp.automation.lineas.domain.line.LineCreationRequest;
import com.claro.sp.automation.lineas.exception.LineasException;

public class LineCreationValidator implements LineValidator<LineCreationRequest> {
    @Override
    public void validate(LineCreationRequest request) {
        if (request == null) {
            throw new LineasException("La solicitud de creacion es obligatoria");
        }
        if (request.getCountryCode() == null) {
            throw new LineasException("El pais es obligatorio");
        }
        if (request.getBeginRange() != null && request.getEndRange() == null) {
            throw new LineasException("Si se informa beginRange tambien debe informarse endRange");
        }
        if (request.getEndRange() != null && request.getBeginRange() == null) {
            throw new LineasException("Si se informa endRange tambien debe informarse beginRange");
        }
    }
}
