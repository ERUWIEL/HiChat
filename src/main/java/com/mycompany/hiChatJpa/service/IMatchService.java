package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.dto.MatchDTO;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de una match
 *
 * @author gatog
 */
public interface IMatchService {

    /**
     * Muestra todos los matches de un usuario
     * Retorna los matches donde el usuario participa (como usuarioA o usuarioB)
     * Ordena los matches por fecha (más recientes primero)
     * 
     * Validaciones:
     * - Verifica que el usuario exista
     * - Verifica que tenga al menos un match
     * - Limita a 100 matches máximo
     * 
     * @param usuario Usuario del cual obtener los matches
     * @return Lista de matches del usuario ordenados por fecha descendente
     * @throws Exception si el usuario no existe, no tiene matches, o hay error
     */
    List<MatchDTO> mostrarMatches(Long idUsuario) throws Exception;
}
