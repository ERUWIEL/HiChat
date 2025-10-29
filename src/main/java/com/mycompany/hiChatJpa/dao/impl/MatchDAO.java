package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IMatchDAO;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * clase que permite manupular los matches
 * @author gatog
 */
public class MatchDAO implements IMatchDAO {

    private static final Logger logger = LoggerFactory.getLogger(MatchDAO.class);
    private static final int MAX_RESULTS = 100;

    /**
     * Inserta un nuevo match en la base de datos.
     * 
     * @param match Match a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Match match) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(match);

            em.getTransaction().commit();
            logger.info("Match insertado correctamente: UsuarioA={} UsuarioB={}",
                        match.getUsuarioA().getIdUsuario(), match.getUsuarioB().getIdUsuario());

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en inserción de match", e);
            }
            logger.error("Error al insertar match", e);
            throw new PersistenceException("insertar", "No se pudo insertar el match", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Actualiza un match existente.
     * 
     * @param match Match con los datos actualizados
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Match match) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(match);

            em.getTransaction().commit();
            logger.info("Match actualizado correctamente: ID {}", match.getIdMatch());

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en actualización de match", e);
            }
            logger.error("Error al actualizar match", e);
            throw new PersistenceException("actualizar", "No se pudo actualizar el match", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Elimina un match por su ID.
     * 
     * @param id ID del match a eliminar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Match match = em.find(Match.class, id);
            if (match != null) {
                em.remove(match);
                logger.info("Match eliminado correctamente: ID {}", id);
            } else {
                logger.warn("Intento de eliminar match inexistente: ID {}", id);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en eliminación de match", e);
            }
            logger.error("Error al eliminar match con ID: {}", id, e);
            throw new PersistenceException("eliminar", "No se pudo eliminar el match", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca un match por su ID.
     * 
     * @param id ID del match
     * @return Match encontrado o null si no existe
     */
    @Override
    public Match buscar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Match match = em.find(Match.class, id);

            if (match != null) {
                logger.debug("Match encontrado: ID {}", id);
            } else {
                logger.debug("Match no encontrado: ID {}", id);
            }

            return match;

        } catch (Exception e) {
            logger.error("Error al buscar match por ID: {}", id, e);
            throw new PersistenceException("buscar", "No se pudo buscar el match", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Lista todos los matches (máximo 100 registros).
     * 
     * @return Lista de matches
     */
    @Override
    public List<Match> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Match> query = em.createNamedQuery("Match.findAll", Match.class);
            query.setMaxResults(MAX_RESULTS);

            List<Match> matches = query.getResultList();
            logger.debug("Se listaron {} matches", matches.size());

            return matches;

        } catch (Exception e) {
            logger.error("Error al listar matches", e);
            throw new PersistenceException("listar", "No se pudo obtener la lista de matches", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca los matches donde el usuario participa como UsuarioA.
     * 
     * @param usuario Usuario que actúa como UsuarioA
     * @return Lista de matches asociados
     */
    @Override
    public List<Match> buscarPorUsuarioA(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Match> query = em.createNamedQuery("Match.findByUsuarioA", Match.class);
            query.setParameter("usuarioA", usuario);
            query.setMaxResults(MAX_RESULTS);

            List<Match> matches = query.getResultList();
            logger.debug("Se encontraron {} matches donde UsuarioA = {}", matches.size(), usuario.getIdUsuario());

            return matches;

        } catch (Exception e) {
            logger.error("Error al buscar matches por UsuarioA ID: {}", usuario.getIdUsuario(), e);
            throw new PersistenceException("buscarPorUsuarioA", 
                                           "No se pudieron buscar los matches por UsuarioA", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca los matches donde el usuario participa como UsuarioB.
     * 
     * @param usuario Usuario que actúa como UsuarioB
     * @return Lista de matches asociados
     */
    @Override
    public List<Match> buscarPorUsuarioB(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Match> query = em.createNamedQuery("Match.findByUsuarioB", Match.class);
            query.setParameter("usuarioB", usuario);
            query.setMaxResults(MAX_RESULTS);

            List<Match> matches = query.getResultList();
            logger.debug("Se encontraron {} matches donde UsuarioB = {}", matches.size(), usuario.getIdUsuario());

            return matches;

        } catch (Exception e) {
            logger.error("Error al buscar matches por UsuarioB ID: {}", usuario.getIdUsuario(), e);
            throw new PersistenceException("buscarPorUsuarioB", 
                                           "No se pudieron buscar los matches por UsuarioB", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
