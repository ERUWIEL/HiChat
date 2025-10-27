package com.mycompany.hiChatJpa.dao.interaccion;

import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de una interaccion
 *
 * @author gatog
 */
public interface IInteraccionDAO {

    void insertar(Interaccion interaccion);

    void actualizar(Interaccion interaccion);

    void eliminar(Long id);

    Interaccion buscar(Long id);

    List<Interaccion> listar();

    List<Interaccion> buscarPorEmisor(Usuario usuario);

    List<Interaccion> buscarPorReceptor(Usuario usuario);

    List<Interaccion> buscarPorTipo(TipoInteraccion tipo);
}
