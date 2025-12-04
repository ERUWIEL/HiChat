
package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz que define los metodos crud de una foto
 * @author gatog
 */
public interface IFotoRepository {
    
    boolean insertar(Foto foto) throws RepositoryException;

    boolean actualizar(Foto foto) throws RepositoryException;

    boolean eliminar(Long id) throws RepositoryException;

    Foto buscar(Long id) throws RepositoryException;

    List<Foto> listar() throws RepositoryException;

    List<Foto> buscarPorUsuario(Usuario usuario) throws RepositoryException;

    List<Foto> buscarPorDescripcion(String descripcion) throws RepositoryException;
}
