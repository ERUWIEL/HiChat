
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IUsuarioDAO;
import com.mycompany.hiChatJpa.dao.impl.UsuarioDAO;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IUsuarioService;
import java.util.List;


/**
 * 
 * author gatog
 */
public class UsuarioService implements IUsuarioService {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    public void registrarUsuario(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("El usuario no puede ser nulo.");
        }
        if (usuario.getCorreoElectronico()== null || usuario.getCorreoElectronico().isEmpty()) {
            throw new Exception("El usuario debe tener un correo válido.");
        }

        // evitar duplicados por correo
        Usuario existente = usuarioDAO.buscarPorCorreo(usuario.getCorreoElectronico());
        if (existente != null) {
            throw new Exception("Ya existe un usuario con ese correo.");
        }

        usuarioDAO.insertar(usuario);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) throws Exception {
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new Exception("Debe especificar un usuario válido para actualizar.");
        }

        if (usuarioDAO.buscar(usuario.getIdUsuario()) == null) {
            throw new Exception("El usuario no existe.");
        }

        usuarioDAO.actualizar(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar usuario.");
        }

        if (usuarioDAO.buscar(id) == null) {
            throw new Exception("El usuario no existe.");
        }

        usuarioDAO.eliminar(id);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        if (id == null || id <= 0) return null;
        return usuarioDAO.buscar(id);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = usuarioDAO.listar();
        if (lista == null) return lista;
        return lista.size() > 100 ? null : lista;
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        if (correo == null || correo.isEmpty()) return null;
        return usuarioDAO.buscarPorCorreo(correo);
    }

    @Override
    public List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno) {
        if (nombre == null || nombre.isEmpty() || apellidoPaterno == null || apellidoPaterno.isEmpty()) {
            return null;
        }
        List<Usuario> lista = usuarioDAO.buscarPorNombreCompleto(nombre, apellidoPaterno);
        if (lista == null) return lista;
        return lista.size() > 100 ? null : lista;
    }
}
