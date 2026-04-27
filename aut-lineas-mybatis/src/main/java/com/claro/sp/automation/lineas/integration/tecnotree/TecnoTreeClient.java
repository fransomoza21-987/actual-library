package com.claro.sp.automation.lineas.integration.tecnotree;

import com.claro.sp.automation.lineas.domain.line.CreationContext;
import com.claro.sp.automation.lineas.domain.line.LineRequest;

public interface TecnoTreeClient {
    boolean subscriberExists(String subId);
    void createSubscriber(CreationContext context);
    void updateSubscriber(LineRequest request);
    void deleteSubscriber(String subId);
}
