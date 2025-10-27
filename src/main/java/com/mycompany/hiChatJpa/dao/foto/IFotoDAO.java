
package com.mycompany.hiChatJpa.dao.foto;

import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de una foto
 * @author gatog
 */
public interface IFotoDAO {
    
    void insertar(Foto foto);

    void actualizar(Foto foto);

    void eliminar(Long id);

    Foto buscar(Long id);

    List<Foto> listar();

    List<Foto> buscarPorUsuario(Usuario usuario);

    List<Foto> buscarPorDescripcion(String descripcion);
}
