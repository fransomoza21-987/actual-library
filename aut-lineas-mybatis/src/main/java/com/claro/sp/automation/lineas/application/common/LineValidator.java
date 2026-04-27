package com.claro.sp.automation.lineas.application.common;

import com.claro.sp.automation.lineas.domain.line.LineRequest;

public interface LineValidator<T extends LineRequest> {
    void validate(T request);
}
