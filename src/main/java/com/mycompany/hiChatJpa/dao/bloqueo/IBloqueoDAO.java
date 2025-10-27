package com.mycompany.hiChatJpa.dao.bloqueo;

import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de un bloqueo
 *
 * @author gatog
 */
public interface IBloqueoDAO {

    void insertar(Bloqueo b);

    void actualizar(Bloqueo e);

    void eliminar(Long id);

    Bloqueo buscar(Long id);

    List<Bloqueo> listar();

    List<Bloqueo> buscarPorBloqueador(Usuario usuario);

    List<Bloqueo> buscarPorBloqueado(Usuario usuario);
}
