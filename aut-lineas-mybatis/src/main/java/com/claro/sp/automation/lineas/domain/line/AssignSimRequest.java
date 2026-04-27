package com.claro.sp.automation.lineas.domain.line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignSimRequest {
    private String cellularNumber;
    private String iccid;
    private String dealerId;
    private String subId;
    private String cetId;
}
