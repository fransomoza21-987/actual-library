package com.claro.sp.automation.lineas.domain.line;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineMigrationRequest extends LineRequest {
    private String sourceEnvironment;
    private String targetEnvironment;
    private boolean migratePacks;
    private boolean migrateBalances;
    private boolean migrateSim;
}
