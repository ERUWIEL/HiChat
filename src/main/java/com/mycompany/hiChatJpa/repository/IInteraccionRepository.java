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

    Interaccion insertar(Interaccion interaccion) throws RepositoryException;

    Interaccion actualizar(Interaccion interaccion) throws RepositoryException;

    Interaccion eliminar(Long id) throws RepositoryException;

    Interaccion buscar(Long id) throws RepositoryException;

    List<Interaccion> listar(int limit, int offset) throws RepositoryException;

    List<Interaccion> buscarPorEmisor(Usuario usuario, int limit, int offset) throws RepositoryException;

    List<Interaccion> buscarPorReceptor(Usuario usuario, int limit, int offset) throws RepositoryException;

    List<Interaccion> buscarPorTipo(TipoInteraccion tipo, int limit, int offset) throws RepositoryException;
}
