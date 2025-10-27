
package com.mycompany.hiChatJpa.dao.foto;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * clase que permite manupular las fotos
 * @author gatog
 */
public class FotoDAO implements IFotoDAO {

    /**
     * metodo que permite insertar una foto
     * @param foto 
     */
    @Override
    public void insertar(Foto foto) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(foto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite actualizar una foto
     * @param foto 
     */
    @Override
    public void actualizar(Foto foto) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(foto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite eliminar una foto
     * @param id 
     */
    @Override
    public void eliminar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Foto foto = em.find(Foto.class, id);
            if (foto != null) em.remove(foto);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite buscra por id una foto
     * @param id
     * @return 
     */
    @Override
    public Foto buscar(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Foto.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite listar todas las fotos
     * @return 
     */
    @Override
    public List<Foto> listar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Foto> query = em.createNamedQuery("Foto.findAll", Foto.class);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar las fotos por usuario
     * @param usuario
     * @return 
     */
    @Override
    public List<Foto> buscarPorUsuario(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Foto> query = em.createNamedQuery("Foto.findByUsuario", Foto.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * metodo que permite consultar las fotos por su descripcion
     * @param descripcion
     * @return 
     */
    @Override
    public List<Foto> buscarPorDescripcion(String descripcion) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Foto> query = em.createNamedQuery("Foto.findByDescripcion", Foto.class);
            query.setParameter("descripcion", "%" + descripcion + "%");
            query.setMaxResults(100);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

