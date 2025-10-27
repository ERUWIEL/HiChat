package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define las reglas de negocio antes de llamar alos dao
 * @author gatog
 */
public interface IFotoService {

    void registrarFoto(Foto foto) throws Exception;

    void actualizarFoto(Foto foto) throws Exception;

    void eliminarFoto(Long id) throws Exception;

    Foto buscarPorId(Long id);

    List<Foto> listarFotos();

    List<Foto> listarPorUsuario(Usuario usuario);

    List<Foto> listarPorDescripcion(String descripcion);
}
