package com.claro.sp.automation.lineas;

import com.claro.sp.automation.lineas.config.LineasFactory;

public class LineasApplication {
    public static void main(String[] args) throws Exception {
        try (LineasFactory factory = new LineasFactory()) {
            factory.lineController();
        }
    }
}
