package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz que define los metodos crud de una interaccion
 *
 * @author gatog
 */
public interface IInteraccionRepository {

    boolean insertar(Interaccion interaccion) throws RepositoryException;

    boolean actualizar(Interaccion interaccion) throws RepositoryException;

    boolean eliminar(Long id) throws RepositoryException;

    Interaccion buscar(Long id) throws RepositoryException;

    List<Interaccion> listar() throws RepositoryException;

    List<Interaccion> buscarPorEmisor(Usuario usuario) throws RepositoryException;

    List<Interaccion> buscarPorReceptor(Usuario usuario) throws RepositoryException;

    List<Interaccion> buscarPorTipo(TipoInteraccion tipo) throws RepositoryException;
}
