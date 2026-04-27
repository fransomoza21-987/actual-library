package com.claro.sp.automation.lineas.controller;

import com.claro.sp.automation.lineas.application.creation.LineCreator;
import com.claro.sp.automation.lineas.application.migration.LineMigrator;
import com.claro.sp.automation.lineas.domain.line.LineCreationRequest;
import com.claro.sp.automation.lineas.domain.line.LineMigrationRequest;
import com.claro.sp.automation.lineas.domain.line.LineResult;

public class LineController {
    private final LineCreator lineCreator;
    private final LineMigrator lineMigrator;

    public LineController(LineCreator lineCreator, LineMigrator lineMigrator) {
        this.lineCreator = lineCreator;
        this.lineMigrator = lineMigrator;
    }

    public LineResult createLine(LineCreationRequest request) {
        return lineCreator.create(request);
    }

    public LineResult migrateLine(LineMigrationRequest request) {
        return lineMigrator.migrate(request);
    }
}
