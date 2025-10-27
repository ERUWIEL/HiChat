package com.mycompany.hiChatJpa.dao.match;

import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de un match
 *
 * @author gatog
 */
public interface IMatchDAO {

    void insertar(Match match);

    void actualizar(Match match);

    void eliminar(Long id);

    Match buscar(Long id);

    List<Match> listar();

    List<Match> buscarPorUsuarioA(Usuario usuario);

    List<Match> buscarPorUsuarioB(Usuario usuario);
}
