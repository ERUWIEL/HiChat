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

    boolean insertar(Bloqueo bloqueo) throws RepositoryException;

    boolean actualizar(Bloqueo bloqueo) throws RepositoryException;

    boolean eliminar(Long id) throws RepositoryException;

    Bloqueo buscar(Long id) throws RepositoryException;

    List<Bloqueo> listar() throws RepositoryException;

    List<Bloqueo> buscarPorBloqueador(Usuario usuario) throws RepositoryException;

    List<Bloqueo> buscarPorBloqueado(Usuario usuario) throws RepositoryException;
}
