package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define las reglas de negocio antes de llamar alos dao
 * @author gatog
 */
public interface IBloqueoService {

    void registrarBloqueo(Bloqueo b) throws Exception;

    void actualizarBloqueo(Bloqueo b) throws Exception;

    void eliminarBloqueo(Long id) throws Exception;

    Bloqueo buscarPorId(Long id);

    List<Bloqueo> listarBloqueos();

    List<Bloqueo> listarPorBloqueador(Usuario usuario);

    List<Bloqueo> listarPorBloqueado(Usuario usuario);
}
