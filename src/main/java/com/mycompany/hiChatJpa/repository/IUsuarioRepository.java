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

    Usuario insertar(Usuario usuario) throws RepositoryException;

    Usuario actualizar(Usuario usuario) throws RepositoryException;

    Usuario eliminar(Long id) throws RepositoryException;

    Usuario buscar(Long id) throws RepositoryException;
    
    Usuario buscarPorCorreo(String correo) throws RepositoryException;

    List<Usuario> listar(int limit, int offset) throws RepositoryException;

    List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno, int limit, int offset) throws RepositoryException;
}
