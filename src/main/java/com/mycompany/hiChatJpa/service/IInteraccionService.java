package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de una interaccion
 * @author gatog
 */
public interface IInteraccionService {

    void registrarInteraccion(Interaccion interaccion) throws Exception;

    void actualizarInteraccion(Interaccion interaccion) throws Exception;

    void eliminarInteraccion(Long id) throws Exception;

    Interaccion buscarPorId(Long id);

    List<Interaccion> listarInteracciones();

    List<Interaccion> listarPorEmisor(Usuario usuario);

    List<Interaccion> listarPorReceptor(Usuario usuario);

    List<Interaccion> listarPorTipo(TipoInteraccion tipo);
}
