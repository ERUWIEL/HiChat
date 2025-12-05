package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IFotoRepository;

/**
 * clase que permite manupular las fotos
 *
 * @author gatog
 */
public class FotoRepository implements IFotoRepository {

    private final EntityManager entityManager;

    public FotoRepository(EntityManager em) {
        this.entityManager = em;
    }

    /**
     * Inserta una nueva foto en la base de datos.
     *
     * @param foto Foto a insertar
     * @return
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Foto insertar(Foto foto) throws RepositoryException {
        try {
            entityManager.persist(foto);
            return foto;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar la foto", e);
        }
    }

    /**
     * Actualiza los datos de una foto existente.
     *
     * @param foto Foto con los datos actualizados
     * @return
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Foto actualizar(Foto foto) throws RepositoryException {
        try {
            return entityManager.merge(foto);
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar la foto", e);
        }
    }

    /**
     * Elimina una foto por su ID.
     *
     * @param id ID de la foto a eliminar
     * @return
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Foto eliminar(Long id) throws RepositoryException {
        try {
            Foto foto = entityManager.find(Foto.class, id);
            if (foto == null) {
                throw new EntityNotFoundException("no se pudo encontrar la foto");
            }

            entityManager.remove(foto);
            return foto;
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar la foto", e);
        }
    }

    /**
     * Busca una foto por su ID.
     *
     * @param id ID de la foto
     * @return Foto encontrada o null si no existe
     */
    @Override
    public Foto buscar(Long id) throws RepositoryException {
        try {
            return entityManager.find(Foto.class, id);
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar la foto", e);
        }
    }

    /**
     * Lista todas las fotos registradas (máximo 100 resultados).
     *
     * @param limit
     * @param offset
     * @return Lista de fotos
     */
    @Override
    public List<Foto> listar(int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Foto> query = entityManager.createNamedQuery("Foto.findAll", Foto.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);

            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo listar las fotos", e);
        }
    }

    /**
     * Busca todas las fotos asociadas a un usuario.
     *
     * @param usuario Usuario propietario de las fotos
     * @param limit
     * @param offset
     * @return Lista de fotos del usuario
     */
    @Override
    public List<Foto> buscarPorUsuario(Usuario usuario, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Foto> query = entityManager.createNamedQuery("Foto.findByUsuario", Foto.class);
            query.setParameter("usuario", usuario);
            query.setMaxResults(limit);
            query.setFirstResult(offset);

            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorUsuario", "No se pudieron buscar las fotos por usuario", e);
        }
    }

    /**
     * Busca fotos que contengan una descripción específica o similar.
     *
     * @param descripcion Descripción o parte de ella
     * @param limit
     * @param offset
     * @return Lista de fotos que coinciden con la descripción
     */
    @Override
    public List<Foto> buscarPorDescripcion(String descripcion, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Foto> query = entityManager.createNamedQuery("Foto.findByDescripcion", Foto.class);
            query.setParameter("descripcion", "%" + descripcion + "%");
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorDescripcion", "No se pudieron buscar las fotos por descripción", e);
        }
    }
}
