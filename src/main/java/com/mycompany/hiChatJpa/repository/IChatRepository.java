
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

    boolean insertar(Chat chat) throws RepositoryException;

    boolean actualizar(Chat chat) throws RepositoryException;

    boolean eliminar(Long id) throws RepositoryException;

    Chat buscar(Long id) throws RepositoryException;

    List<Chat> listar() throws RepositoryException;

    List<Chat> buscarPorNombre(String nombre) throws RepositoryException;

    List<Chat> buscarPorParticipante(Usuario usuario) throws RepositoryException;
}
