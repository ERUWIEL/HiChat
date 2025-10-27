package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de una match
 *
 * @author gatog
 */
public interface IMatchService {

    void registrarMatch(Match match) throws Exception;

    void actualizarMatch(Match match) throws Exception;

    void eliminarMatch(Long id) throws Exception;

    Match buscarPorId(Long id);

    List<Match> listarMatchs();

    List<Match> listarPorUsuarioA(Usuario usuario);

    List<Match> listarPorUsuarioB(Usuario usuario);
}
