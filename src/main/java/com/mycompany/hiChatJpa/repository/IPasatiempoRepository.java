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

    boolean insertar(Pasatiempo pasatiempo) throws RepositoryException;

    boolean actualizar(Pasatiempo pasatiempo) throws RepositoryException;

    boolean eliminar(Long id) throws RepositoryException;

    Pasatiempo buscar(Long id) throws RepositoryException;

    List<Pasatiempo> listar() throws RepositoryException;

    Pasatiempo buscarPorNombre(String nombre) throws RepositoryException;
}
