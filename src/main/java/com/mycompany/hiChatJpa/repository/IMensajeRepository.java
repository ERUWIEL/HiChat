package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz que define los metodos crud de un mensaje
 *
 * @author gatog
 */
public interface IMensajeRepository {

    boolean insertar(Mensaje mensaje) throws RepositoryException;

    boolean actualizar(Mensaje mensaje) throws RepositoryException;

    boolean eliminar(Long id) throws RepositoryException;

    Mensaje buscar(Long id) throws RepositoryException;

    List<Mensaje> listar() throws RepositoryException;

    List<Mensaje> buscarPorChat(Chat chat) throws RepositoryException;

    List<Mensaje> buscarNoVistosPorUsuario(Usuario usuario) throws RepositoryException;
}
