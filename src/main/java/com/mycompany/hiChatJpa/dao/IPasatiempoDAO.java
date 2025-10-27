package com.mycompany.hiChatJpa.dao;

import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import java.util.List;

/**
 * interfaz que define los metodos crud de un pasatiempo
 *
 * @author gatog
 */
public interface IPasatiempoDAO {

    void insertar(Pasatiempo pasatiempo);

    void actualizar(Pasatiempo pasatiempo);

    void eliminar(Long id);

    Pasatiempo buscar(Long id);

    List<Pasatiempo> listar();

    Pasatiempo buscarPorNombre(String nombre);
}
