package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IBloqueoDAO;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * clase dao que permite manipular los bloqueos
 *
 * @author gatog
 */
public class BloqueoDAO implements IBloqueoDAO {

    private static final Logger logger = LoggerFactory.getLogger(BloqueoDAO.class);
    private static final int MAX_RESULTS = 100;

    /**
     * Inserta un nuevo bloqueo en la base de datos.
     * 
     * @param b Bloqueo a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Bloqueo b) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(b);

            em.getTransaction().commit();
            logger.info("Bloqueo insertado correctamente: bloqueador={} bloqueado={}",
                        b.getUsuarioBloqueador().getIdUsuario(), b.getUsuarioBloqueado().getIdUsuario());

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en inserción de bloqueo", e);
            }
            logger.error("Error al insertar bloqueo", e);
            throw new PersistenceException("insertar", "No se pudo insertar el bloqueo", e);
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
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Bloqueo e) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(e);

            em.getTransaction().commit();
            logger.info("Bloqueo actualizado correctamente: ID {}", e.getIdBloqueo());

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en actualización de bloqueo", ex);
            }
            logger.error("Error al actualizar bloqueo", ex);
            throw new PersistenceException("actualizar", "No se pudo actualizar el bloqueo", ex);
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
     * @throws PersistenceException si ocurre un error en la operación
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
                logger.info("Bloqueo eliminado correctamente: ID {}", id);
            } else {
                logger.warn("Intento de eliminar bloqueo inexistente: ID {}", id);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en eliminación de bloqueo", e);
            }
            logger.error("Error al eliminar bloqueo con ID: {}", id, e);
            throw new PersistenceException("eliminar", "No se pudo eliminar el bloqueo", e);
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

            if (bloqueo != null) {
                logger.debug("Bloqueo encontrado: ID {}", id);
            } else {
                logger.debug("Bloqueo no encontrado: ID {}", id);
            }

            return bloqueo;

        } catch (Exception e) {
            logger.error("Error al buscar bloqueo por ID: {}", id, e);
            throw new PersistenceException("buscar", "No se pudo buscar el bloqueo", e);
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
            logger.debug("Se listaron {} bloqueos", bloqueos.size());

            return bloqueos;

        } catch (Exception e) {
            logger.error("Error al listar bloqueos", e);
            throw new PersistenceException("listar", "No se pudo listar los bloqueos", e);
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
            logger.debug("Se encontraron {} bloqueos realizados por el usuario ID {}",
                         bloqueos.size(), usuario.getIdUsuario());

            return bloqueos;

        } catch (Exception e) {
            logger.error("Error al buscar bloqueos por bloqueador ID: {}", usuario.getIdUsuario(), e);
            throw new PersistenceException("buscarPorBloqueador",
                                           "No se pudieron buscar los bloqueos por bloqueador", e);
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
            logger.debug("Se encontraron {} bloqueos donde el usuario ID {} fue bloqueado",
                         bloqueos.size(), usuario.getIdUsuario());

            return bloqueos;

        } catch (Exception e) {
            logger.error("Error al buscar bloqueos por bloqueado ID: {}", usuario.getIdUsuario(), e);
            throw new PersistenceException("buscarPorBloqueado",
                                           "No se pudieron buscar los bloqueos por bloqueado", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
