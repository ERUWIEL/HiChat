package com.mycompany.hiChatJpa.repository;

import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import java.util.List;

/**
 * interfaz que define los metodos crud de un pasatiempo
 *
 * @author gatog
 */
public interface IPasatiempoRepository {

    Pasatiempo insertar(Pasatiempo pasatiempo) throws RepositoryException;

    Pasatiempo actualizar(Pasatiempo pasatiempo) throws RepositoryException;

    Pasatiempo eliminar(Long id) throws RepositoryException;

    Pasatiempo buscar(Long id) throws RepositoryException;

    Pasatiempo buscarPorNombre(String nombre) throws RepositoryException;
            
    List<Pasatiempo> listar(int limit, int offset) throws RepositoryException;
}
