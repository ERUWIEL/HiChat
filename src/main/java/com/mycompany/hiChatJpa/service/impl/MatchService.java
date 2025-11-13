package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IMatchDAO;
import com.mycompany.hiChatJpa.dao.impl.MatchDAO;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IMatchService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la capa de servicio para la entidad interaccion author
 * gatog
 */
public class MatchService implements IMatchService {

    private final IMatchDAO matchDAO;

    public MatchService() {
        this.matchDAO = new MatchDAO();
    }

    /**
     * Muestra todos los matches de un usuario
     * Retorna los matches donde el usuario participa (como usuarioA o usuarioB)
     * Ordena los matches por fecha (más recientes primero)
     * 
     * @param usuario Usuario del cual obtener los matches
     * @return Lista de matches del usuario ordenados por fecha descendente
     * @throws Exception si hay error en la validación
     */
    public List<Match> mostrarMatches(Usuario usuario) throws Exception {
        // ============ VALIDACIONES ============
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new Exception("Usuario inválido.");
        }
        if (usuario.getIdUsuario() <= 0) {
            throw new Exception("ID del usuario inválido.");
        }
        // ============ OBTENER MATCHES COMO USUARIO A ============
        List<Match> matchesComoA = matchDAO.buscarPorUsuarioA(usuario);
        if (matchesComoA == null) {
            matchesComoA = List.of(); // Lista vacía
        }

        // ============ OBTENER MATCHES COMO USUARIO B ============
        List<Match> matchesComoB = matchDAO.buscarPorUsuarioB(usuario);
        if (matchesComoB == null) {
            matchesComoB = List.of(); // Lista vacía
        }

        // ============ COMBINAR AMBOS RESULTADOS ============
        List<Match> todosLosMatches = matchesComoA.stream()
            .collect(Collectors.toList());
        todosLosMatches.addAll(matchesComoB);

        // ============ VALIDAR QUE EXISTAN MATCHES ============
        if (todosLosMatches.isEmpty()) {
            throw new Exception("El usuario no tiene matches.");
        }

        // ============ VALIDAR LÍMITE ============
        if (todosLosMatches.size() > 100) {
            throw new Exception("Demasiados matches. Se limita a 100.");
        }

        // ============ ORDENAR POR FECHA (MÁS RECIENTES PRIMERO) ============
        List<Match> matchesOrdenados = todosLosMatches.stream()
            .sorted((m1, m2) -> m2.getFechaMatch().compareTo(m1.getFechaMatch()))
            .collect(Collectors.toList());
        return matchesOrdenados;
    }
}