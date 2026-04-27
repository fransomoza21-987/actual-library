package com.claro.sp.automation.lineas.application.migration;

import com.claro.sp.automation.lineas.domain.line.LineMigrationRequest;
import com.claro.sp.automation.lineas.domain.line.LineResult;

public class MyBatisLineMigrator implements LineMigrator {
    @Override
    public LineResult migrate(LineMigrationRequest request) {
        throw new UnsupportedOperationException("Logica de migracion pendiente de implementar");
    }
}
