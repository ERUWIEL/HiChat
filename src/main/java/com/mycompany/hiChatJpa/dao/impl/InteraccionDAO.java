package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IInteraccionDAO;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupular las interacciones
 * @author gatog
 */
public class InteraccionDAO implements IInteraccionDAO {
    
    private static final int MAX_RESULTS = 100;

    /**
     * Inserta una nueva interacción en la base de datos.
     * 
     * @param interaccion Interacción a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Interaccion interaccion) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(interaccion);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("insertar", "No se pudo insertar la interacción", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Actualiza una interacción existente.
     * 
     * @param interaccion Interacción con los datos actualizados
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Interaccion interaccion) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(interaccion);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("actualizar", "No se pudo actualizar la interacción", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Elimina una interacción por su ID.
     * 
     * @param id ID de la interacción a eliminar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Interaccion interaccion = em.find(Interaccion.class, id);
            if (interaccion != null) {
                em.remove(interaccion);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("eliminar", "No se pudo eliminar la interacción", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca una interacción por su ID.
     * 
     * @param id ID de la interacción
     * @return Interacción encontrada o null si no existe
     */
    @Override
    public Interaccion buscar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Interaccion interaccion = em.find(Interaccion.class, id);

            return interaccion;

        } catch (Exception e) {
            throw new PersistenceException("buscar", "No se pudo buscar la interacción", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Lista todas las interacciones (máximo 100 registros).
     * 
     * @return Lista de interacciones
     */
    @Override
    public List<Interaccion> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findAll", Interaccion.class);
            query.setMaxResults(MAX_RESULTS);

            List<Interaccion> interacciones = query.getResultList();

            return interacciones;

        } catch (Exception e) {
            throw new PersistenceException("listar", "No se pudo obtener la lista de interacciones", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca las interacciones enviadas por un usuario (emisor).
     * 
     * @param usuario Usuario emisor
     * @return Lista de interacciones enviadas
     */
    @Override
    public List<Interaccion> buscarPorEmisor(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findByEmisor", Interaccion.class);
            query.setParameter("emisor", usuario);
            query.setMaxResults(MAX_RESULTS);

            List<Interaccion> interacciones = query.getResultList();
            return interacciones;

        } catch (Exception e) {
            throw new PersistenceException("buscarPorEmisor", "No se pudieron buscar las interacciones por emisor", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca las interacciones recibidas por un usuario (receptor).
     * 
     * @param usuario Usuario receptor
     * @return Lista de interacciones recibidas
     */
    @Override
    public List<Interaccion> buscarPorReceptor(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findByReceptor", Interaccion.class);
            query.setParameter("receptor", usuario);
            query.setMaxResults(MAX_RESULTS);

            List<Interaccion> interacciones = query.getResultList();
            return interacciones;

        } catch (Exception e) {
            throw new PersistenceException("buscarPorReceptor","No se pudieron buscar las interacciones por receptor", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca las interacciones por tipo.
     * 
     * @param tipo Tipo de interacción a filtrar
     * @return Lista de interacciones del tipo especificado
     */
    @Override
    public List<Interaccion> buscarPorTipo(TipoInteraccion tipo) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Interaccion> query = em.createNamedQuery("Interaccion.findByTipo", Interaccion.class);
            query.setParameter("tipo", tipo);
            query.setMaxResults(MAX_RESULTS);

            List<Interaccion> interacciones = query.getResultList();
            return interacciones;

        } catch (Exception e) {
            throw new PersistenceException("buscarPorTipo", "No se pudieron buscar las interacciones por tipo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
