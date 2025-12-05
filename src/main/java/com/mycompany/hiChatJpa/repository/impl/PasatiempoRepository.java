package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import com.mycompany.hiChatJpa.repository.IPasatiempoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PasatiempoRepository implements IPasatiempoRepository {

    private final EntityManager entityManager;

    public PasatiempoRepository(EntityManager em) {
        this.entityManager = em;
    }

    /**
     * Inserta un nuevo pasatiempo.
     *
     * @param pasatiempo
     * @return
     */
    @Override
    public Pasatiempo insertar(Pasatiempo pasatiempo) throws RepositoryException {
        try {
            entityManager.persist(pasatiempo);
            return pasatiempo;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el pasatiempo", e);
        }
    }

    /**
     * Actualiza un pasatiempo existente.
     *
     * @param pasatiempo
     * @return
     */
    @Override
    public Pasatiempo actualizar(Pasatiempo pasatiempo) throws RepositoryException {
        try {
            return entityManager.merge(pasatiempo);
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el pasatiempo", e);
        }
    }

    /**
     * Elimina un pasatiempo por ID.
     *
     * @param id
     * @return
     */
    @Override
    public Pasatiempo eliminar(Long id) throws RepositoryException {
        try {
            Pasatiempo pasatiempo = entityManager.find(Pasatiempo.class, id);
            if (pasatiempo == null) {
                throw new EntityNotFoundException("No se encontró el pasatiempo indicado");
            }

            entityManager.remove(pasatiempo);
            return pasatiempo;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el pasatiempo", e);
        }
    }

    /**
     * Busca un pasatiempo por ID.
     *
     * @param id
     * @return
     */
    @Override
    public Pasatiempo buscar(Long id) throws RepositoryException {
        try {
            return entityManager.find(Pasatiempo.class, id);
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el pasatiempo", e);
        }
    }

    /**
     * Busca un pasatiempo por nombre.
     *
     * @param nombre
     * @return
     */
    @Override
    public Pasatiempo buscarPorNombre(String nombre) throws RepositoryException {
        try {
            TypedQuery<Pasatiempo> query = entityManager.createNamedQuery("Pasatiempo.findByNombre", Pasatiempo.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorNombre", "No se pudo buscar el pasatiempo por nombre", e);
        }
    }

    /**
     * Lista pasatiempos con paginación.
     * @param limit
     * @param offset
     * @return 
     */
    @Override
    public List<Pasatiempo> listar(int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Pasatiempo> query = entityManager.createNamedQuery("Pasatiempo.findAll", Pasatiempo.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de pasatiempos", e);
        }
    }
}
