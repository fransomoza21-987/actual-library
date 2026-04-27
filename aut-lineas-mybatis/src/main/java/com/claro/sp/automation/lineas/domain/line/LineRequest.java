package com.claro.sp.automation.lineas.domain.line;

import com.claro.sp.automation.lineas.domain.country.CountryCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineRequest {
    private CountryCode countryCode;
    private String environment;
    private String cellularNumber;
    private String billNumber;
    private String subId;
    private String profileId;
    private String businessType;
    private String accountStatus;
    private String ratePlanSth;
    private String statusStealth;
    private String statusPpay;
    private String callRestrictionId;
    private String blockCode;
    private String accountId;
    private String dealerId;
    private String clientId;
    private String handle;
    private String imsi;
    private String iccid;
    private boolean generateSim = true;
}
