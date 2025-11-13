package com.mycompany.hiChatJpa.exceptions;

/**
 * excepcion personalizada lanzada cuando una entidad no se encuentra repetida
 * en la base de datos
 *
 * @author gatog
 */
public class DuplicateEntityException extends RuntimeException {

    private final String entityName;
    private final Object entityId;

    public DuplicateEntityException(String entityName, Object entityId) {
        super(String.format("%s con ID %s no encontrado", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public DuplicateEntityException(String message) {
        super(message);
        this.entityName = null;
        this.entityId = null;
    }

    public String getEntityName() {
        return entityName;
    }

    public Object getEntityId() {
        return entityId;
    }
}
