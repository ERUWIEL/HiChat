package com.mycompany.hiChatJpa.exceptions;

/**
 * excepcion personalizada lanzada cuando una entidad no se encuentra en la base
 * de datos
 *
 * @author gatog
 */
public class EntityNotFoundException extends RuntimeException {

    private final String entityName;
    private final Object entityId;

    public EntityNotFoundException(String entityName, Object entityId) {
        super(String.format("%s con ID %s no encontrado", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public EntityNotFoundException(String message) {
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
