package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz que define los metodos crud de un bloqueo
 *
 * @author gatog
 */
public interface IBloqueoRepository {

    Bloqueo insertar(Bloqueo bloqueo) throws RepositoryException;

    Bloqueo actualizar(Bloqueo bloqueo) throws RepositoryException;

    Bloqueo eliminar(Long id) throws RepositoryException;

    Bloqueo buscar(Long id) throws RepositoryException;

    List<Bloqueo> listar(int limit, int offset) throws RepositoryException;

    List<Bloqueo> buscarPorBloqueador(Usuario usuario, int limit, int offset) throws RepositoryException;

    List<Bloqueo> buscarPorBloqueado(Usuario usuario, int limit, int offset) throws RepositoryException;
}
