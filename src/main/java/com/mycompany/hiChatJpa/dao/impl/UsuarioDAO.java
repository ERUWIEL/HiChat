package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IUsuarioDAO;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementación del DAO para la entidad Usuario
 * Maneja todas las operaciones de persistencia relacionadas con usuarios
 * 
 * @author gatog
 */
public class UsuarioDAO implements IUsuarioDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);
    private static final int MAX_RESULTS = 100;
    
    /**
     * Inserta un nuevo usuario en la base de datos
     * 
     * @param usuario Usuario a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            em.persist(usuario);
            
            em.getTransaction().commit();
            logger.info("Usuario insertado correctamente: {}", usuario.getCorreoElectronico());
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en inserción de usuario", e);
            }
            logger.error("Error al insertar usuario", e);
            throw new PersistenceException("insertar", "No se pudo insertar el usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
    
    /**
     * Actualiza un usuario existente
     * 
     * @param usuario Usuario con los datos actualizados
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            em.merge(usuario);
            
            em.getTransaction().commit();
            logger.info("Usuario actualizado correctamente: {}", usuario.getCorreoElectronico());
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en actualización de usuario", e);
            }
            logger.error("Error al actualizar usuario", e);
            throw new PersistenceException("actualizar", "No se pudo actualizar el usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
    
    /**
     * Elimina un usuario por su ID
     * 
     * @param id ID del usuario a eliminar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario);
                logger.info("Usuario eliminado correctamente: ID {}", id);
            } else {
                logger.warn("Intento de eliminar usuario inexistente: ID {}", id);
            }
            
            em.getTransaction().commit();
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                logger.error("Rollback ejecutado en eliminación de usuario", e);
            }
            logger.error("Error al eliminar usuario con ID: {}", id, e);
            throw new PersistenceException("eliminar", "No se pudo eliminar el usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
    
    /**
     * Busca un usuario por su ID
     * 
     * @param id ID del usuario
     * @return Usuario encontrado o null si no existe
     */
    @Override
    public Usuario buscar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Usuario usuario = em.find(Usuario.class, id);
            
            if (usuario != null) {
                logger.debug("Usuario encontrado: ID {}", id);
            } else {
                logger.debug("Usuario no encontrado: ID {}", id);
            }
            
            return usuario;
            
        } catch (Exception e) {
            logger.error("Error al buscar usuario por ID: {}", id, e);
            throw new PersistenceException("buscar", "No se pudo buscar el usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
    
    /**
     * Lista todos los usuarios (máximo 100 registros)
     * 
     * @return Lista de usuarios
     */
    @Override
    public List<Usuario> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findAll", Usuario.class);
            query.setMaxResults(MAX_RESULTS);
            
            List<Usuario> usuarios = query.getResultList();
            logger.debug("Se listaron {} usuarios", usuarios.size());
            
            return usuarios;
            
        } catch (Exception e) {
            logger.error("Error al listar usuarios", e);
            throw new PersistenceException("listar", "No se pudo obtener la lista de usuarios", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
    
    /**
     * Busca un usuario por su correo electrónico
     * 
     * @param correo Correo electrónico del usuario
     * @return Usuario encontrado o null si no existe
     */
    @Override
    public Usuario buscarPorCorreo(String correo) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByCorreo", Usuario.class);
            query.setParameter("correo", correo);
            
            try {
                Usuario usuario = query.getSingleResult();
                logger.debug("Usuario encontrado por correo: {}", correo);
                return usuario;
            } catch (NoResultException e) {
                logger.debug("Usuario no encontrado por correo: {}", correo);
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Error al buscar usuario por correo: {}", correo, e);
            throw new PersistenceException("buscarPorCorreo", "No se pudo buscar el usuario por correo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
    
    /**
     * Busca usuarios por nombre completo
     * 
     * @param nombre Nombre del usuario
     * @param apellidoPaterno Apellido paterno del usuario
     * @return Lista de usuarios que coinciden
     */
    @Override
    public List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByNombreCompleto", Usuario.class);
            query.setParameter("nombre", nombre);
            query.setParameter("apellidoPaterno", apellidoPaterno);
            query.setMaxResults(MAX_RESULTS);
            
            List<Usuario> usuarios = query.getResultList();
            logger.debug("Se encontraron {} usuarios con nombre: {} {}", 
                    usuarios.size(), nombre, apellidoPaterno);
            
            return usuarios;
            
        } catch (Exception e) {
            logger.error("Error al buscar usuarios por nombre completo: {} {}", 
                    nombre, apellidoPaterno, e);
            throw new PersistenceException("buscarPorNombreCompleto", 
                    "No se pudo buscar usuarios por nombre", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}