package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define las reglas de negocio antes de llamar alos dao
 * @author gatog
 */
public interface IChatService {

    void registrarChat(Chat chat) throws Exception;

    void actualizarChat(Chat chat) throws Exception;

    void eliminarChat(Long id) throws Exception;

    Chat buscarPorId(Long id);

    List<Chat> listarChats();

    List<Chat> listarPorNombre(String nombre);

    List<Chat> listarPorParticipante(Usuario usuario);
}
