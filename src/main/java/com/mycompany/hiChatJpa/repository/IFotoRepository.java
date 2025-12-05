
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
    
    Foto insertar(Foto foto) throws RepositoryException;

    Foto actualizar(Foto foto) throws RepositoryException;

    Foto eliminar(Long id) throws RepositoryException;

    Foto buscar(Long id) throws RepositoryException;

    List<Foto> listar(int limit, int offset) throws RepositoryException;

    List<Foto> buscarPorUsuario(Usuario usuario, int limit, int offset) throws RepositoryException;

    List<Foto> buscarPorDescripcion(String descripcion, int limit, int offset) throws RepositoryException;
}
