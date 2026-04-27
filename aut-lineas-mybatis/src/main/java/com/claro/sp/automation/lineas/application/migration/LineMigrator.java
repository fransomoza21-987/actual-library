package com.claro.sp.automation.lineas.application.migration;

import com.claro.sp.automation.lineas.domain.line.LineMigrationRequest;
import com.claro.sp.automation.lineas.domain.line.LineResult;

public interface LineMigrator {
    LineResult migrate(LineMigrationRequest request);
}
