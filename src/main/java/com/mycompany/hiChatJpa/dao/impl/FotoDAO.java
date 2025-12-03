
package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dao.IFotoDAO;
import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupular las fotos
 * @author gatog
 */
public class FotoDAO implements IFotoDAO {

    private static final int MAX_RESULTS = 100;

    /**
     * Inserta una nueva foto en la base de datos.
     * 
     * @param foto Foto a insertar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void insertar(Foto foto) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.persist(foto);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("insertar", "No se pudo insertar la foto", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Actualiza los datos de una foto existente.
     * 
     * @param foto Foto con los datos actualizados
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void actualizar(Foto foto) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            em.merge(foto);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("actualizar", "No se pudo actualizar la foto", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Elimina una foto por su ID.
     * 
     * @param id ID de la foto a eliminar
     * @throws PersistenceException si ocurre un error en la operación
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            Foto foto = em.find(Foto.class, id);
            if (foto != null) {
                em.remove(foto);
            } 

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenceException("eliminar", "No se pudo eliminar la foto", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca una foto por su ID.
     * 
     * @param id ID de la foto
     * @return Foto encontrada o null si no existe
     */
    @Override
    public Foto buscar(Long id) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Foto foto = em.find(Foto.class, id);
            return foto;

        } catch (Exception e) {
            throw new PersistenceException("buscar", "No se pudo buscar la foto", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Lista todas las fotos registradas (máximo 100 resultados).
     * 
     * @return Lista de fotos
     */
    @Override
    public List<Foto> listar() {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Foto> query = em.createNamedQuery("Foto.findAll", Foto.class);
            query.setMaxResults(MAX_RESULTS);

            List<Foto> fotos = query.getResultList();

            return fotos;

        } catch (Exception e) {
            throw new PersistenceException("listar", "No se pudo listar las fotos", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca todas las fotos asociadas a un usuario.
     * 
     * @param usuario Usuario propietario de las fotos
     * @return Lista de fotos del usuario
     */
    @Override
    public List<Foto> buscarPorUsuario(Usuario usuario) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Foto> query = em.createNamedQuery("Foto.findByUsuario", Foto.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(MAX_RESULTS);

            List<Foto> fotos = query.getResultList();
            return fotos;

        } catch (Exception e) {
            throw new PersistenceException("buscarPorUsuario", "No se pudieron buscar las fotos por usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * Busca fotos que contengan una descripción específica o similar.
     * 
     * @param descripcion Descripción o parte de ella
     * @return Lista de fotos que coinciden con la descripción
     */
    @Override
    public List<Foto> buscarPorDescripcion(String descripcion) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Foto> query = em.createNamedQuery("Foto.findByDescripcion", Foto.class);
            query.setParameter("descripcion", "%" + descripcion + "%");
            query.setMaxResults(MAX_RESULTS);

            List<Foto> fotos = query.getResultList();
            return fotos;

        } catch (Exception e) {
            throw new PersistenceException("buscarPorDescripcion","No se pudieron buscar las fotos por descripción", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }
}
