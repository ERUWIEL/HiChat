
package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz que define los metodos crud de un chat
 * @author gatog
 */
public interface IChatRepository {

    Chat insertar(Chat chat) throws RepositoryException;

    Chat actualizar(Chat chat) throws RepositoryException;

    Chat eliminar(Long id) throws RepositoryException;

    Chat buscar(Long id) throws RepositoryException;

    List<Chat> listar(int limit, int offset) throws RepositoryException;

    List<Chat> buscarPorNombre(String nombre, int limit, int offset) throws RepositoryException;

    List<Chat> buscarPorParticipante(Usuario usuario, int limit, int offset) throws RepositoryException;
}
