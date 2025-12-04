package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import com.mycompany.hiChatJpa.repository.IUsuarioRepository;

/**
 * Implementación del DAO para la entidad Usuario
 * Maneja todas las operaciones de persistencia relacionadas con usuarios
 * 
 * @author gatog
 */
public class UsuarioRepository implements IUsuarioRepository {
    
    private static final int MAX_RESULTS = 100;
    private final EntityManager entityManager;
    
    public UsuarioRepository(EntityManager em) {
        this.entityManager = em;
    }
    
    
    /**
     * Inserta un nuevo usuario en la base de datos
     * 
     * @param usuario Usuario a insertar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean insertar(Usuario usuario) {
        try {         
            entityManager.persist(usuario);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el usuario", e);
        }
    }
    
    /**
     * Actualiza un usuario existente
     * 
     * @param usuario Usuario con los datos actualizados
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean actualizar(Usuario usuario) {
        try {
            entityManager.merge(usuario);
            return true;
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el usuario", e);
        } 
    }
    
    /**
     * Elimina un usuario por su ID
     * 
     * @param id ID del usuario a eliminar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public boolean eliminar(Long id) {
        try {            
            Usuario usuario = entityManager.find(Usuario.class, id);
            if (usuario != null) {
                entityManager.remove(usuario);
                return true;
            } else {
                throw new EntityNotFoundException("no se encontro el usuario indicado");
            }
        } catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el usuario", e);
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
        try {
            Usuario usuario = entityManager.find(Usuario.class, id);          
            return usuario;
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el usuario", e);
        }
    }
    
    /**
     * Lista todos los usuarios (máximo 100 registros)
     * 
     * @return Lista de usuarios
     */
    @Override
    public List<Usuario> listar() {
        try {
            TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.findAll", Usuario.class);
            query.setMaxResults(MAX_RESULTS);
            List<Usuario> usuarios = query.getResultList();
            return usuarios;
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de usuarios", e);
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
        try {
            TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.findByCorreo", Usuario.class);
            query.setParameter("correo", correo);
            try {
                Usuario usuario = query.getSingleResult();
                return usuario;
            } catch (NoResultException e) {
                return null;
            }   
        } catch (Exception e) {
            throw new RepositoryException("buscarPorCorreo", "No se pudo buscar el usuario por correo", e);
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
        try {
            TypedQuery<Usuario> query = entityManager.createNamedQuery("Usuario.findByNombreCompleto", Usuario.class);
            query.setParameter("nombre", nombre);
            query.setParameter("apellidoPaterno", apellidoPaterno);
            query.setMaxResults(MAX_RESULTS);
            List<Usuario> usuarios = query.getResultList();
            return usuarios;
        } catch (Exception e) {
            throw new RepositoryException("buscarPorNombreCompleto","No se pudo buscar usuarios por nombre", e);
        }
    }
}