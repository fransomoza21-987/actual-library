package com.claro.sp.automation.lineas.domain.line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineResult {
    private LineOperationType operationType;
    private String cellularNumber;
    private String billNumber;
    private String subId;
    private String handle;
    private String imsi;
    private String iccid;
}
