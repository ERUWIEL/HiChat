package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de una mensaje
 *
 * @author gatog
 */
public interface IMensajeService {

    void registrarMensaje(Mensaje mensaje) throws Exception;

    void actualizarMensaje(Mensaje mensaje) throws Exception;

    void eliminarMensaje(Long id) throws Exception;

    Mensaje buscarPorId(Long id);

    List<Mensaje> listarMensajes();

    List<Mensaje> listarPorChat(Chat chat);

    List<Mensaje> listarNoVistosPorUsuario(Usuario usuario);
}
