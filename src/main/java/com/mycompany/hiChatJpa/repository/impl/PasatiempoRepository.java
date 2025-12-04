package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import com.mycompany.hiChatJpa.repository.IPasatiempoRepository;

/**
 * Implementación del DAO para la entidad Pasatiempo.
 * Proporciona operaciones CRUD y consultas personalizadas.
 * 
 * @author 
 */
public class PasatiempoRepository implements IPasatiempoRepository {

    private static final int MAX_RESULTS = 100;
    private final EntityManager entityManager;
    
    public PasatiempoRepository(EntityManager em){
        this.entityManager = em;
    }
    
    
    /**
     * Inserta un nuevo pasatiempo en la base de datos.
     * 
     * @param pasatiempo Pasatiempo a insertar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean insertar(Pasatiempo pasatiempo) {
        try {
            entityManager.persist(pasatiempo);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el pasatiempo", e);
        }
    }

    /**
     * Actualiza un pasatiempo existente.
     * 
     * @param pasatiempo Pasatiempo con los datos actualizados
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean actualizar(Pasatiempo pasatiempo) {
        try {
            entityManager.merge(pasatiempo);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el pasatiempo", e);
        }
    }

    /**
     * Elimina un pasatiempo por su ID.
     * 
     * @param id ID del pasatiempo a eliminar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean eliminar(Long id) {
        try {
            Pasatiempo pasatiempo = entityManager.find(Pasatiempo.class, id);
            if (pasatiempo != null) {
                entityManager.remove(pasatiempo);
                return true;
            } else {
                throw new EntityNotFoundException("no se encontro el pasatiempo indicado");
            }
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el pasatiempo", e);
        }
    }

    /**
     * Busca un pasatiempo por su ID.
     * 
     * @param id ID del pasatiempo
     * @return Pasatiempo encontrado o null si no existe
     */
    @Override
    public Pasatiempo buscar(Long id) {
        try {
            Pasatiempo pasatiempo = entityManager.find(Pasatiempo.class, id);
            return pasatiempo;
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el pasatiempo", e);
        }
    }

    /**
     * Lista todos los pasatiempos (máximo 100 registros).
     * 
     * @return Lista de pasatiempos
     */
    @Override
    public List<Pasatiempo> listar() {
        try {
            TypedQuery<Pasatiempo> query = entityManager.createNamedQuery("Pasatiempo.findAll", Pasatiempo.class);
            query.setMaxResults(MAX_RESULTS);
            List<Pasatiempo> pasatiempos = query.getResultList();
            return pasatiempos;
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de pasatiempos", e);
        }
    }

    /**
     * Busca un pasatiempo por su nombre.
     * 
     * @param nombre Nombre del pasatiempo
     * @return Pasatiempo encontrado o null si no existe
     */
    @Override
    public Pasatiempo buscarPorNombre(String nombre) {
        try {
            TypedQuery<Pasatiempo> query = entityManager.createNamedQuery("Pasatiempo.findByNombre", Pasatiempo.class);
            query.setParameter("nombre", nombre);
            try {
                Pasatiempo pasatiempo = query.getSingleResult();
                return pasatiempo;
            } catch (NoResultException e) {
                return null;
            }
        } catch (Exception e) {
            throw new RepositoryException("buscarPorNombre", "No se pudo buscar el pasatiempo por nombre", e);
        } finally {
            if (entityManager != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
