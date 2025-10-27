
package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 *
 * @author gatog
 */
public interface IUsuarioService {

    void registrarUsuario(Usuario usuario) throws Exception;

    void actualizarUsuario(Usuario usuario) throws Exception;

    void eliminarUsuario(Long id) throws Exception;

    Usuario buscarPorId(Long id);

    List<Usuario> listarUsuarios();

    Usuario buscarPorCorreo(String correo);

    List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno);
}
