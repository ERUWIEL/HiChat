package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.repository.impl.MatchRepository;
import com.mycompany.hiChatJpa.repository.impl.UsuarioRepository;
import com.mycompany.hiChatJpa.dto.MatchDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IMatchService;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import com.mycompany.hiChatJpa.repository.IUsuarioRepository;
import com.mycompany.hiChatJpa.repository.IMatchRepository;

/**
 * Implementación de la capa de servicio para Match
 * @author gatog
 */
public class MatchService implements IMatchService {

    private final IMatchRepository matchDAO;
    private final IUsuarioRepository usuarioDAO;

    public MatchService() {
        this.matchDAO = new MatchRepository();
        this.usuarioDAO = new UsuarioRepository();
    }

    /**
     * Convierte Usuario a UsuarioPerfilDTO
     */
    private UsuarioPerfilDTO usuarioADTO(Usuario usuario) {
        if (usuario == null) return null;

        Integer edad = null;
        if (usuario.getFechaNacimiento() != null) {
            edad = Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears();
        }

        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setCarrera(usuario.getCarrera());
        dto.setBiografia(usuario.getBiografia());
        dto.setUrlFotoPerfil(usuario.getUrlFotoPerfil());
        dto.setGenero(usuario.getGenero() != null ? usuario.getGenero().toString() : null);
        dto.setEdad(edad);
        return dto;
    }

    /**
     * Convierte Match a MatchDTO
     */
    private MatchDTO matchADTO(Match match) {
        if (match == null) return null;

        MatchDTO dto = new MatchDTO();
        dto.setIdMatch(match.getIdMatch());
        dto.setUsuarioA(usuarioADTO(match.getUsuarioA()));
        dto.setUsuarioB(usuarioADTO(match.getUsuarioB()));
        dto.setFechaMatch(match.getFechaMatch());
        dto.setIdChat(match.getChat() != null ? match.getChat().getIdChat() : null);
        return dto;
    }

    /**
     * Muestra todos los matches de un usuario
     * Retorna los matches donde el usuario participa (como usuarioA o usuarioB)
     * Ordena los matches por fecha (más recientes primero)
     * 
     * @param idUsuario ID del usuario del cual obtener los matches
     * @return Lista de MatchDTO ordenados por fecha descendente
     * @throws Exception si hay error en la validación
     */
    @Override
    public List<MatchDTO> mostrarMatches(Long idUsuario) throws Exception {
        // ============ VALIDACIONES ============
        if (idUsuario == null || idUsuario <= 0) {
            throw new Exception("ID del usuario inválido.");
        }

        // Verificar que el usuario existe
        Usuario usuario = usuarioDAO.buscar(idUsuario);
        if (usuario == null) {
            throw new Exception("El usuario no existe.");
        }

        // ============ OBTENER MATCHES COMO USUARIO A ============
        List<Match> matchesComoA = matchDAO.buscarPorUsuarioA(usuario);
        if (matchesComoA == null) {
            matchesComoA = List.of();
        }

        // ============ OBTENER MATCHES COMO USUARIO B ============
        List<Match> matchesComoB = matchDAO.buscarPorUsuarioB(usuario);
        if (matchesComoB == null) {
            matchesComoB = List.of();
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
        List<MatchDTO> matchesOrdenados = todosLosMatches.stream()
            .sorted((m1, m2) -> m2.getFechaMatch().compareTo(m1.getFechaMatch()))
            .map(this::matchADTO)
            .collect(Collectors.toList());

        return matchesOrdenados;
    }
}