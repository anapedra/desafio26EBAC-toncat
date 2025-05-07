package org.anasantana.services.exceptions;

public class TipoChaveNaoEncontradaException extends Exception {
    public TipoChaveNaoEncontradaException(String msg) {
        super(msg);
    }

    public TipoChaveNaoEncontradaException(String msg, Throwable cause) {
        super(msg, cause);
    }
}