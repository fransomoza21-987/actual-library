package com.claro.sp.automation.lineas.repository;

import com.claro.sp.automation.lineas.domain.line.CreationContext;
import com.claro.sp.automation.lineas.domain.line.LineResult;

public interface LineRepository {
    boolean isCellularAvailable(String cellularNumber);
    boolean isHandleAvailable(String handle);
    String findCcardPrefix();
    String findAvailableNumber(CreationContext context);
    String findNextHandle();
    String findRatePlanId(String profileId);
    void createLine(CreationContext context);
    void rollbackCreatedLine(CreationContext context);
    LineResult toResult(CreationContext context);
}
