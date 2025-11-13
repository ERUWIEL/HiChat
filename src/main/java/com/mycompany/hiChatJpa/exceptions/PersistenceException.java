package com.mycompany.hiChatJpa.exceptions;

/**
 * excepcion personalizada lanzada cuando hay un error al interactuar con la
 * base de datos
 *
 * @author gatog
 */
public class PersistenceException extends RuntimeException {

    private final String operation;
    private final String cause;
    private final Exception error;

    public PersistenceException(String operation, String cause, Exception error){
        super(String.format("error al %s debido a %s", operation, cause), error);
        this.operation = operation;
        this.cause = cause;
        this.error = error;
    }

    public PersistenceException(String message){
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
