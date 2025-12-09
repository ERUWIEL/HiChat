package com.mycompany.hiChatJpa.repository.impl;

import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IMatchRepository;

/**
 * clase que permite manupular los matches
 *
 * @author gatog
 */
public class MatchRepository implements IMatchRepository {

    private final EntityManager entityManager;
    
    public MatchRepository(EntityManager em){
        this.entityManager = em;
    }
    
    /**
     * Inserta un nuevo match en la base de datos.
     *
     * @param match Match a insertar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Match insertar(Match match) {
        try {
            entityManager.persist(match);
            entityManager.flush();
            return match;
        } catch (Exception e) {
            throw new RepositoryException("insertar", "No se pudo insertar el match", e);
        }
    }

    /**
     * Actualiza un match existente.
     *
     * @param match Match con los datos actualizados
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Match actualizar(Match match) {
        try {
            return entityManager.merge(match);
        } catch (Exception e) {
            throw new RepositoryException("actualizar", "No se pudo actualizar el match", e);
        }
    }

    /**
     * Elimina un match por su ID.
     *
     * @param id ID del match a eliminar
     * @return 
     * @throws RepositoryException si ocurre un error en la operación
     */
    @Override
    public Match eliminar(Long id) {
        try {
            Match match = entityManager.find(Match.class, id);
            if (match == null) {
                throw new EntityNotFoundException("no se encontro el match indicado");
            }
            
            entityManager.remove(match);
            return match;
        } catch (EntityNotFoundException ex) {
            throw ex;
        }catch (Exception e) {
            throw new RepositoryException("eliminar", "No se pudo eliminar el match", e);
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
        try {
            return entityManager.find(Match.class, id);
        } catch (Exception e) {
            throw new RepositoryException("buscar", "No se pudo buscar el match", e);
        }
    }

    /**
     * Lista todos los matches (máximo 100 registros).
     *
     * @param limit
     * @param offset
     * @return Lista de matches
     */
    @Override
    public List<Match> listar(int limit, int offset) throws RepositoryException{
        try {
            TypedQuery<Match> query = entityManager.createNamedQuery("Match.findAll", Match.class);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("listar", "No se pudo obtener la lista de matches", e);
        }
    }

    /**
     * Busca los matches donde el usuario participa como UsuarioA.
     *
     * @param usuario Usuario que actúa como UsuarioA
     * @param limit
     * @param offset
     * @return Lista de matches asociados
     */
    @Override
    public List<Match> buscarPorUsuarioA(Usuario usuario, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Match> query = entityManager.createNamedQuery("Match.findByUsuarioA", Match.class);
            query.setParameter("usuarioA", usuario);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorUsuarioA", "No se pudieron buscar los matches por UsuarioA", e);
        }
    }

    /**
     * Busca los matches donde el usuario participa como UsuarioB.
     *
     * @param usuario Usuario que actúa como UsuarioB
     * @param limit
     * @param offset
     * @return Lista de matches asociados
     */
    @Override
    public List<Match> buscarPorUsuarioB(Usuario usuario, int limit, int offset) throws RepositoryException {
        try {
            TypedQuery<Match> query = entityManager.createNamedQuery("Match.findByUsuarioB", Match.class);
            query.setParameter("usuarioB", usuario);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            
            return query.getResultList();
        } catch (Exception e) {
            throw new RepositoryException("buscarPorUsuarioB", "No se pudieron buscar los matches por UsuarioB", e);
        }
    }
}
