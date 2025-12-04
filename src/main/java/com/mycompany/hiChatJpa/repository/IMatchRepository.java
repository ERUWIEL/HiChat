package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz que define los metodos crud de un match
 *
 * @author gatog
 */
public interface IMatchRepository {

    boolean insertar(Match match) throws RepositoryException;

    boolean actualizar(Match match) throws RepositoryException;

    boolean eliminar(Long id) throws RepositoryException;

    Match buscar(Long id) throws RepositoryException;

    List<Match> listar() throws RepositoryException;

    List<Match> buscarPorUsuarioA(Usuario usuario) throws RepositoryException;

    List<Match> buscarPorUsuarioB(Usuario usuario) throws RepositoryException;
}
