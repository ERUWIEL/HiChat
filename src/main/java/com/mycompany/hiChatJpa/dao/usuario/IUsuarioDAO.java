package com.mycompany.hiChatJpa.dao.usuario;

import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz qye define los metodos crud de un usuario
 *
 * @author gatog
 */
public interface IUsuarioDAO {

    void insertar(Usuario usuario);

    void actualizar(Usuario usuario);

    void eliminar(Long id);

    Usuario buscar(Long id);

    List<Usuario> listar();

    Usuario buscarPorCorreo(String correo);

    List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno);
}
