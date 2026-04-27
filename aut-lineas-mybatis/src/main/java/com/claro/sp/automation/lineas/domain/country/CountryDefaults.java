package com.claro.sp.automation.lineas.domain.country;

import lombok.Getter;

@Getter
public class CountryDefaults {
    private final String defaultBlockCode;
    private final String defaultBusinessType;
    private final String defaultCodeArea;
    private final String prepaidProfileId;
    private final String creditProfileId;
    private final String accountId;
    private final String corporateAccountId;
    private final String dealerId;
    private final String clientId;
    private final String corporateClientId;
    private final int expectedLineLength;

    public CountryDefaults(String defaultBlockCode, String defaultBusinessType, String defaultCodeArea,
                           String prepaidProfileId, String creditProfileId, String accountId,
                           String corporateAccountId, String dealerId, String clientId,
                           String corporateClientId, int expectedLineLength) {
        this.defaultBlockCode = defaultBlockCode;
        this.defaultBusinessType = defaultBusinessType;
        this.defaultCodeArea = defaultCodeArea;
        this.prepaidProfileId = prepaidProfileId;
        this.creditProfileId = creditProfileId;
        this.accountId = accountId;
        this.corporateAccountId = corporateAccountId;
        this.dealerId = dealerId;
        this.clientId = clientId;
        this.corporateClientId = corporateClientId;
        this.expectedLineLength = expectedLineLength;
    }
}
