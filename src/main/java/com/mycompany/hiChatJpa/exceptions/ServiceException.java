package com.mycompany.hiChatJpa.exceptions;

/**
 *
 * @author gatog
 */
public class ServiceException extends RuntimeException{

    private final String operation;
    private final String cause;
    private final Exception error;

    public ServiceException(String operation, String cause, Exception error) {
        super(String.format("error al %s debido a %s", operation, cause), error);
        this.operation = operation;
        this.cause = cause;
        this.error = error;
    }

    public ServiceException(String message) {
        super(message);
        this.operation = null;
        this.cause = null;
        this.error = null;
    }

    public String getOperacion() {
        return operation;
    }

    public String getCausa() {
        return cause;
    }

    public Exception getError() {
        return error;
    }
}
