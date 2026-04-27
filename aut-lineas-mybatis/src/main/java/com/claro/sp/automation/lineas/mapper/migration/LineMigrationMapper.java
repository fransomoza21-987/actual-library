package com.claro.sp.automation.lineas.mapper.migration;

import com.claro.sp.automation.lineas.domain.line.LineMigrationRequest;

public interface LineMigrationMapper {
    void readSourceLine(LineMigrationRequest request);
    void insertTargetLine(LineMigrationRequest request);
    void markMigrationAudit(LineMigrationRequest request);
}
