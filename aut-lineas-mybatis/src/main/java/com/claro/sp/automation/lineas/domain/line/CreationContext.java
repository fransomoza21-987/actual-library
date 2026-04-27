package com.claro.sp.automation.lineas.domain.line;

import com.claro.sp.automation.lineas.domain.country.CountryCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreationContext extends LineCreationRequest {
    private int expectedLineLength;
    private String ccardOwner;

    public CreationContext(LineCreationRequest request) {
        setCountryCode(request.getCountryCode());
        setEnvironment(request.getEnvironment());
        setCellularNumber(request.getCellularNumber());
        setBillNumber(request.getBillNumber());
        setSubId(request.getSubId());
        setProfileId(request.getProfileId());
        setBusinessType(request.getBusinessType());
        setAccountStatus(request.getAccountStatus());
        setGenerateSim(request.isGenerateSim());
        setDistinctBillNumber(request.isDistinctBillNumber());
        setCorporativeClient(request.isCorporativeClient());
        setCodeArea(request.getCodeArea());
        setNode(request.getNode());
        setBeginRange(request.getBeginRange());
        setEndRange(request.getEndRange());
    }

    public boolean isArgentina() {
        return CountryCode.AR == getCountryCode();
    }

    public boolean isParaguay() {
        return CountryCode.PY == getCountryCode();
    }

    public boolean isUruguay() {
        return CountryCode.UY == getCountryCode();
    }
}
