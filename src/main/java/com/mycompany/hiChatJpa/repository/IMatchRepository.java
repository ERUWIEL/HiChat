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

    Match insertar(Match match) throws RepositoryException;

    Match actualizar(Match match) throws RepositoryException;

    Match eliminar(Long id) throws RepositoryException;

    Match buscar(Long id) throws RepositoryException;

    List<Match> listar(int limit, int offset) throws RepositoryException;

    List<Match> buscarPorUsuarioA(Usuario usuario, int limit, int offset) throws RepositoryException;

    List<Match> buscarPorUsuarioB(Usuario usuario, int limit, int offset) throws RepositoryException;
}
