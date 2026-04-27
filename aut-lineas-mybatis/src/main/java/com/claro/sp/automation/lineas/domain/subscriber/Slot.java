package com.claro.sp.automation.lineas.domain.subscriber;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Slot {
    private String packageDefinitionId;
    private String renewalPackageId;
    private Integer quantityLeft;
    private String expiryDate;
}
