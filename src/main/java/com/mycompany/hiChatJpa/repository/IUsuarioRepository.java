package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz qye define los metodos crud de un usuario
 *
 * @author gatog
 */
public interface IUsuarioRepository {

    boolean insertar(Usuario usuario) throws RepositoryException;

    boolean actualizar(Usuario usuario) throws RepositoryException;

    boolean  eliminar(Long id) throws RepositoryException;

    Usuario buscar(Long id) throws RepositoryException;

    List<Usuario> listar() throws RepositoryException;

    Usuario buscarPorCorreo(String correo) throws RepositoryException;

    List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno) throws RepositoryException;
}
