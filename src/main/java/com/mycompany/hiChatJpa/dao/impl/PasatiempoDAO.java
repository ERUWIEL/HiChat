package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IPasatiempoDAO;
import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Implementación del DAO para la entidad Pasatiempo.
 * Proporciona operaciones CRUD y consultas personalizadas.
 * 
 * @author 
 */
public class PasatiempoDAO implements IPasatiempoDAO {

    private static final int MAX_RESULTS = 100;

    /**
     * Inserta un nuevo pasatiempo en la base de datos.
     * 
     * @param pasatiempo Pasatiempo a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Pasatiempo pasatiempo) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(pasatiempo);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("insertar", "No se pudo insertar el pasatiempo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Actualiza un pasatiempo existente.
     * 
     * @param pasatiempo Pasatiempo con los datos actualizados
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Pasatiempo pasatiempo) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(pasatiempo);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("actualizar", "No se pudo actualizar el pasatiempo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Elimina un pasatiempo por su ID.
     * 
     * @param id ID del pasatiempo a eliminar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Pasatiempo pasatiempo = em.find(Pasatiempo.class, id);
            if (pasatiempo != null) {
                em.remove(pasatiempo);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("eliminar", "No se pudo eliminar el pasatiempo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
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
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Pasatiempo pasatiempo = em.find(Pasatiempo.class, id);
            return pasatiempo;

        } catch (Exception e) {
            throw new PersistenceException("buscar", "No se pudo buscar el pasatiempo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Lista todos los pasatiempos (máximo 100 registros).
     * 
     * @return Lista de pasatiempos
     */
    @Override
    public List<Pasatiempo> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Pasatiempo> query = em.createNamedQuery("Pasatiempo.findAll", Pasatiempo.class);
            query.setMaxResults(MAX_RESULTS);

            List<Pasatiempo> pasatiempos = query.getResultList();
            return pasatiempos;

        } catch (Exception e) {
            throw new PersistenceException("listar", "No se pudo obtener la lista de pasatiempos", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
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
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Pasatiempo> query = em.createNamedQuery("Pasatiempo.findByNombre", Pasatiempo.class);
            query.setParameter("nombre", nombre);

            try {
                Pasatiempo pasatiempo = query.getSingleResult();
                return pasatiempo;
            } catch (NoResultException e) {
                return null;
            }

        } catch (Exception e) {
            throw new PersistenceException("buscarPorNombre", "No se pudo buscar el pasatiempo por nombre", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
