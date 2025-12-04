package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IBloqueoRepository;

/**
 * clase dao que permite manipular los bloqueos
 *
 * @author gatog
 */
public class BloqueoRepository implements IBloqueoRepository {
    private static final int MAX_RESULTS = 100;

    /**
     * Inserta un nuevo bloqueo en la base de datos.
     * 
     * @param b Bloqueo a insertar
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public void insertar(Bloqueo b) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(b);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RepositoryException("insertar", "No se pudo insertar el bloqueo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Actualiza un bloqueo existente.
     * 
     * @param e Bloqueo con los datos actualizados
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Bloqueo e) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(e);

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RepositoryException("actualizar", "No se pudo actualizar el bloqueo", ex);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Elimina un bloqueo por su ID.
     * 
     * @param id ID del bloqueo a eliminar
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Bloqueo bloqueo = em.find(Bloqueo.class, id);
            if (bloqueo != null) {
                em.remove(bloqueo);
            }
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RepositoryException("eliminar", "No se pudo eliminar el bloqueo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca un bloqueo por su ID.
     * 
     * @param id ID del bloqueo
     * @return Bloqueo encontrado o null si no existe
     */
    @Override
    public Bloqueo buscar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Bloqueo bloqueo = em.find(Bloqueo.class, id);
            return bloqueo;

        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el bloqueo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Lista todos los bloqueos (máximo 100 registros).
     * 
     * @return Lista de bloqueos
     */
    @Override
    public List<Bloqueo> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Bloqueo> query = em.createNamedQuery("Bloqueo.findAll", Bloqueo.class);
            query.setMaxResults(MAX_RESULTS);

            List<Bloqueo> bloqueos = query.getResultList();

            return bloqueos;

        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo listar los bloqueos", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca los bloqueos realizados por un usuario.
     * 
     * @param usuario Usuario bloqueador
     * @return Lista de bloqueos hechos por el usuario
     */
    @Override
    public List<Bloqueo> buscarPorBloqueador(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Bloqueo> query = em.createNamedQuery("Bloqueo.findByBloqueador", Bloqueo.class);
            query.setParameter("bloqueador", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Bloqueo> bloqueos = query.getResultList();

            return bloqueos;

        } catch (Exception e) {
            throw new RepositoryException("buscarPorBloqueador","No se pudieron buscar los bloqueos por bloqueador", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca los bloqueos donde un usuario fue bloqueado.
     * 
     * @param usuario Usuario bloqueado
     * @return Lista de bloqueos en los que el usuario fue bloqueado
     */
    @Override
    public List<Bloqueo> buscarPorBloqueado(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Bloqueo> query = em.createNamedQuery("Bloqueo.findByBloqueado", Bloqueo.class);
            query.setParameter("bloqueado", usuario);
            query.setMaxResults(MAX_RESULTS);
            List<Bloqueo> bloqueos = query.getResultList();

            return bloqueos;

        } catch (Exception e) {
            throw new RepositoryException("buscarPorBloqueado","No se pudieron buscar los bloqueos por bloqueado", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
