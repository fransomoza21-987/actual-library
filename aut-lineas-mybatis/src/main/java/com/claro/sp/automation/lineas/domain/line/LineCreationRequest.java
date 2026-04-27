package com.claro.sp.automation.lineas.domain.line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineCreationRequest extends LineRequest {
    private boolean distinctBillNumber;
    private boolean corporativeClient;
    private String codeArea;
    private String node;
    private String beginRange;
    private String endRange;
}
