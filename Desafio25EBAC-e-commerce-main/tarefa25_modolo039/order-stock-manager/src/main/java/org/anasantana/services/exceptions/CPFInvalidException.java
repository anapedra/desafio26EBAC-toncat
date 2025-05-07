package org.anasantana.services.exceptions;

public class CPFInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CPFInvalidException(String message) {
        super(message);
    }
}
