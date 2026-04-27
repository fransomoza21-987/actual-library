package com.claro.sp.automation.lineas.application.creation;

import com.claro.sp.automation.lineas.domain.line.LineCreationRequest;
import com.claro.sp.automation.lineas.domain.line.LineResult;

public interface LineCreator {
    LineResult create(LineCreationRequest request);
}
