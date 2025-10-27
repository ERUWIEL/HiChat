
package com.mycompany.hiChatJpa.dao.chat;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de un chat
 * @author gatog
 */
public interface IChatDAO {

    void insertar(Chat chat);

    void actualizar(Chat chat);

    void eliminar(Long id);

    Chat buscar(Long id);

    List<Chat> listar();

    List<Chat> buscarPorNombre(String nombre);

    List<Chat> buscarPorParticipante(Usuario usuario);
}
