package com.claro.sp.automation.lineas.domain.country;

import com.claro.sp.automation.lineas.domain.line.CreationContext;
import com.claro.sp.automation.lineas.exception.LineasException;
import com.claro.sp.automation.lineas.util.Constants;

public class CountryDefaultsResolver {
    public CountryDefaults resolve(CountryCode countryCode) {
        if (countryCode == null) {
            throw new LineasException("El pais es obligatorio");
        }
        return switch (countryCode) {
            case AR -> new CountryDefaults("N0100", "PP", "555", "675", "1048", "65324311", "683818744", "L026500134", "637188", "8507987", 10);
            case PY -> new CountryDefaults("APP01", "PP", "66", "921", "912", "117400960", "16143588", "CACPY50240", "2114121", "593917", 9);
            case UY -> new CountryDefaults("MPP01", "PP", "77", "420", "499", "1318666", "270355", "UY00100997", "99910", "19924", 8);
        };
    }

    public void applyTo(CreationContext context) {
        CountryDefaults defaults = resolve(context.getCountryCode());
        if (isBlank(context.getNode())) {
            context.setNode("N");
        }
        if (isBlank(context.getAccountStatus())) {
            context.setAccountStatus("2");
        }
        if (isBlank(context.getBlockCode())) {
            context.setBlockCode(defaults.getDefaultBlockCode());
        }
        if (isBlank(context.getBusinessType())) {
            context.setBusinessType(defaults.getDefaultBusinessType());
        }
        if (isBlank(context.getCodeArea())) {
            context.setCodeArea(defaults.getDefaultCodeArea());
        }
        if (isBlank(context.getBeginRange())) {
            context.setBeginRange(Constants.DEFAULT_BEGIN_RANGE);
        }
        if (isBlank(context.getEndRange())) {
            context.setEndRange(Constants.DEFAULT_END_RANGE);
        }
        if (isBlank(context.getProfileId())) {
            context.setProfileId("CR".equalsIgnoreCase(context.getBusinessType()) ? defaults.getCreditProfileId() : defaults.getPrepaidProfileId());
        }
        context.setDealerId(defaults.getDealerId());
        context.setAccountId(context.isCorporativeClient() ? defaults.getCorporateAccountId() : defaults.getAccountId());
        context.setClientId(context.isCorporativeClient() ? defaults.getCorporateClientId() : defaults.getClientId());
        context.setExpectedLineLength(defaults.getExpectedLineLength());
        context.setCcardOwner("CCARD");
        applyStatus(context);
    }

    private void applyStatus(CreationContext context) {
        switch (context.getAccountStatus()) {
            case "2" -> {
                context.setStatusStealth(Constants.STH_STATUS_ACTIVE);
                context.setStatusPpay(Constants.CCARD_STATUS_ACTIVE);
                context.setCallRestrictionId(Constants.CR_ID_DDN);
            }
            case "3" -> {
                context.setStatusStealth(Constants.STH_STATUS_ACTIVE);
                context.setStatusPpay(Constants.CCARD_STATUS_LAPSED);
                context.setCallRestrictionId(Constants.CR_ID_DDN);
            }
            case "4" -> {
                context.setStatusStealth(Constants.STH_STATUS_SUSPENDED);
                context.setStatusPpay(Constants.CCARD_STATUS_SUSPENDED);
                context.setCallRestrictionId(Constants.CR_ID_ONLY_ENTRANCE);
            }
            case "5" -> {
                context.setStatusStealth(Constants.STH_STATUS_CANCELED);
                context.setStatusPpay(Constants.CCARD_STATUS_CANCELED);
                context.setCallRestrictionId(Constants.CR_ID_ONLY_ENTRANCE);
            }
            default -> throw new LineasException("Estado de cuenta no soportado: " + context.getAccountStatus());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

