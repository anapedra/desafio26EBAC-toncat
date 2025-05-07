package org.anasantana.services.exceptions;

public class DAOException extends Exception {
    public DAOException(String msg, Throwable t) {
        super(msg, t);
    }
    public DAOException(String msg) {
        super(msg);
    }
}
