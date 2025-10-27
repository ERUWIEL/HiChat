package com.mycompany.hiChatJpa.dao;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de un mensaje
 *
 * @author gatog
 */
public interface IMensajeDAO {

    void insertar(Mensaje mensaje);

    void actualizar(Mensaje mensaje);

    void eliminar(Long id);

    Mensaje buscar(Long id);

    List<Mensaje> listar();

    List<Mensaje> buscarPorChat(Chat chat);

    List<Mensaje> buscarNoVistosPorUsuario(Usuario usuario);
}
