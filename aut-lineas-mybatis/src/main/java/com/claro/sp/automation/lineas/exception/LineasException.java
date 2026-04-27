package com.claro.sp.automation.lineas.exception;

public class LineasException extends RuntimeException {
    public LineasException(String message) {
        super(message);
    }

    public LineasException(String message, Throwable cause) {
        super(message, cause);
    }
}
