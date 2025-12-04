package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.repository.impl.BloqueoRepository;
import com.mycompany.hiChatJpa.repository.impl.ChatRepository;
import com.mycompany.hiChatJpa.repository.impl.InteraccionRepository;
import com.mycompany.hiChatJpa.repository.impl.MatchRepository;
import com.mycompany.hiChatJpa.repository.impl.UsuarioRepository;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IInteraccionService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IUsuarioRepository;
import com.mycompany.hiChatJpa.repository.IMatchRepository;
import com.mycompany.hiChatJpa.repository.IInteraccionRepository;
import com.mycompany.hiChatJpa.repository.IChatRepository;
import com.mycompany.hiChatJpa.repository.IBloqueoRepository;

/**
 * Implementación de la capa de servicio para Interacción
 * @author gatog
 */
public class InteraccionService implements IInteraccionService {

    private final IInteraccionRepository interaccionDAO;
    private final IUsuarioRepository usuarioDAO;
    private final IMatchRepository matchDAO;
    private final IBloqueoRepository bloqueoDAO;
    private final IChatRepository chatDAO;

    public InteraccionService() {
        this.interaccionDAO = new InteraccionRepository();
        this.usuarioDAO = new UsuarioRepository();
        this.matchDAO = new MatchRepository();
        this.bloqueoDAO = new BloqueoRepository();
        this.chatDAO = new ChatRepository();
    }

    /**
     * Da un like a un usuario
     * Si el usuario receptor también dio like, se genera automáticamente un match
     */
    @Override
    public boolean darLike(Long idUsuarioEmisor, Long idUsuarioReceptor) throws Exception {
        // ============ VALIDACIONES ============
        if (idUsuarioEmisor == null || idUsuarioEmisor <= 0) {
            throw new Exception("ID del usuario emisor inválido.");
        }

        if (idUsuarioReceptor == null || idUsuarioReceptor <= 0) {
            throw new Exception("ID del usuario receptor inválido.");
        }

        if (idUsuarioEmisor.equals(idUsuarioReceptor)) {
            throw new Exception("No puedes dar like a ti mismo.");
        }

        // Buscar usuarios
        Usuario usuarioEmisor = usuarioDAO.buscar(idUsuarioEmisor);
        if (usuarioEmisor == null) {
            throw new Exception("El usuario emisor no existe.");
        }

        Usuario usuarioReceptor = usuarioDAO.buscar(idUsuarioReceptor);
        if (usuarioReceptor == null) {
            throw new Exception("El usuario receptor no existe.");
        }

        // Validar que no exista un bloqueo
        validarNoExistaBloqueo(usuarioEmisor, usuarioReceptor);

        // Validar que no exista un like previo
        List<Interaccion> likesDelEmisor = interaccionDAO.buscarPorEmisor(usuarioEmisor);
        boolean likeExistente = likesDelEmisor.stream()
            .anyMatch(i -> i.getUsuarioReceptor().getIdUsuario().equals(idUsuarioReceptor) &&
                          i.getTipo().equals(TipoInteraccion.ME_GUSTA));

        if (likeExistente) {
            throw new Exception("Ya habías dado like a este usuario.");
        }

        // ============ REGISTRAR EL LIKE ============
        Interaccion like = new Interaccion.Builder()
            .usuarioEmisor(usuarioEmisor)
            .usuarioReceptor(usuarioReceptor)
            .tipo(TipoInteraccion.ME_GUSTA)
            .fechaInteraccion(LocalDateTime.now())
            .build();

        interaccionDAO.insertar(like);

        // ============ VERIFICAR SI EXISTE UN LIKE RECÍPROCO ============
        List<Interaccion> likesDelReceptor = interaccionDAO.buscarPorEmisor(usuarioReceptor);
        boolean likeRecíproco = likesDelReceptor.stream()
            .anyMatch(i -> i.getUsuarioReceptor().getIdUsuario().equals(idUsuarioEmisor) &&
                          i.getTipo().equals(TipoInteraccion.ME_GUSTA));

        if (likeRecíproco) {
            // ============ CREAR MATCH ============
            crearMatch(usuarioEmisor, usuarioReceptor);
            return true;
        }

        return false;
    }

    /**
     * Da un dislike a un usuario
     */
    @Override
    public void darDislike(Long idUsuarioEmisor, Long idUsuarioReceptor) throws Exception {
        // ============ VALIDACIONES ============
        if (idUsuarioEmisor == null || idUsuarioEmisor <= 0) {
            throw new Exception("ID del usuario emisor inválido.");
        }

        if (idUsuarioReceptor == null || idUsuarioReceptor <= 0) {
            throw new Exception("ID del usuario receptor inválido.");
        }

        if (idUsuarioEmisor.equals(idUsuarioReceptor)) {
            throw new Exception("No puedes dar dislike a ti mismo.");
        }

        // Buscar usuarios
        Usuario usuarioEmisor = usuarioDAO.buscar(idUsuarioEmisor);
        if (usuarioEmisor == null) {
            throw new Exception("El usuario emisor no existe.");
        }

        Usuario usuarioReceptor = usuarioDAO.buscar(idUsuarioReceptor);
        if (usuarioReceptor == null) {
            throw new Exception("El usuario receptor no existe.");
        }

        // Validar que no exista un bloqueo
        validarNoExistaBloqueo(usuarioEmisor, usuarioReceptor);

        // Validar que no exista un dislike previo
        List<Interaccion> dislikesDelEmisor = interaccionDAO.buscarPorEmisor(usuarioEmisor);
        boolean dislikeExistente = dislikesDelEmisor.stream()
            .anyMatch(i -> i.getUsuarioReceptor().getIdUsuario().equals(idUsuarioReceptor) &&
                          i.getTipo().equals(TipoInteraccion.NO_ME_INTERESA));

        if (dislikeExistente) {
            throw new Exception("Ya habías dado dislike a este usuario.");
        }

        // ============ REGISTRAR EL DISLIKE ============
        Interaccion dislike = new Interaccion.Builder()
            .usuarioEmisor(usuarioEmisor)
            .usuarioReceptor(usuarioReceptor)
            .tipo(TipoInteraccion.NO_ME_INTERESA)
            .fechaInteraccion(LocalDateTime.now())
            .build();

        interaccionDAO.insertar(dislike);
    }

    /**
     * Bloquea a un usuario
     */
    @Override
    public void bloquearUsuario(Long idUsuarioBloqueador, Long idUsuarioBloqueado) throws Exception {
        // ============ VALIDACIONES ============
        if (idUsuarioBloqueador == null || idUsuarioBloqueador <= 0) {
            throw new Exception("ID del usuario bloqueador inválido.");
        }

        if (idUsuarioBloqueado == null || idUsuarioBloqueado <= 0) {
            throw new Exception("ID del usuario a bloquear inválido.");
        }

        if (idUsuarioBloqueador.equals(idUsuarioBloqueado)) {
            throw new Exception("No puedes bloquearte a ti mismo.");
        }

        // Buscar usuarios
        Usuario usuarioBloqueador = usuarioDAO.buscar(idUsuarioBloqueador);
        if (usuarioBloqueador == null) {
            throw new Exception("El usuario bloqueador no existe.");
        }

        Usuario usuarioBloqueado = usuarioDAO.buscar(idUsuarioBloqueado);
        if (usuarioBloqueado == null) {
            throw new Exception("El usuario a bloquear no existe.");
        }

        // Validar que no exista un bloqueo previo
        List<Bloqueo> bloqueosExistentes = bloqueoDAO.buscarPorBloqueador(usuarioBloqueador);
        boolean yaBloqueado = bloqueosExistentes.stream()
            .anyMatch(b -> b.getUsuarioBloqueado().getIdUsuario().equals(idUsuarioBloqueado));

        if (yaBloqueado) {
            throw new Exception("Ya habías bloqueado a este usuario.");
        }

        // ============ REGISTRAR EL BLOQUEO ============
        Bloqueo bloqueo = new Bloqueo.Builder()
            .usuarioBloqueador(usuarioBloqueador)
            .usuarioBloqueado(usuarioBloqueado)
            .fechaBloqueo(LocalDateTime.now())
            .build();

        bloqueoDAO.insertar(bloqueo);

        // ============ DESHABILITAR CHAT ============
        deshabilitarChatEntre(usuarioBloqueador, usuarioBloqueado);
    }

    /**
     * Desbloquea a un usuario
     */
    @Override
    public void desbloquearUsuario(Long idUsuarioBloqueador, Long idUsuarioBloqueado) throws Exception {
        // ============ VALIDACIONES ============
        if (idUsuarioBloqueador == null || idUsuarioBloqueador <= 0) {
            throw new Exception("ID del usuario bloqueador inválido.");
        }

        if (idUsuarioBloqueado == null || idUsuarioBloqueado <= 0) {
            throw new Exception("ID del usuario a desbloquear inválido.");
        }

        if (idUsuarioBloqueador.equals(idUsuarioBloqueado)) {
            throw new Exception("No puedes desbloquearte a ti mismo.");
        }

        // Buscar usuarios
        Usuario usuarioBloqueador = usuarioDAO.buscar(idUsuarioBloqueador);
        if (usuarioBloqueador == null) {
            throw new Exception("El usuario bloqueador no existe.");
        }

        Usuario usuarioBloqueado = usuarioDAO.buscar(idUsuarioBloqueado);
        if (usuarioBloqueado == null) {
            throw new Exception("El usuario a desbloquear no existe.");
        }

        // ============ BUSCAR EL BLOQUEO ============
        List<Bloqueo> bloqueosExistentes = bloqueoDAO.buscarPorBloqueador(usuarioBloqueador);
        Bloqueo bloqueoAEliminar = bloqueosExistentes.stream()
            .filter(b -> b.getUsuarioBloqueado().getIdUsuario().equals(idUsuarioBloqueado))
            .findFirst()
            .orElse(null);

        if (bloqueoAEliminar == null) {
            throw new Exception("No existe un bloqueo previo con este usuario.");
        }

        // ============ ELIMINAR EL BLOQUEO ============
        bloqueoDAO.eliminar(bloqueoAEliminar.getIdBloqueo());
    }

    /**
     * Crea un match entre dos usuarios
     */
    private void crearMatch(Usuario usuarioA, Usuario usuarioB) throws Exception {
        // Validar que no exista un match previo
        List<Match> matchesExistentes = matchDAO.buscarPorUsuarioA(usuarioA);
        boolean matchExistente = matchesExistentes.stream()
            .anyMatch(m -> m.getUsuarioB().getIdUsuario().equals(usuarioB.getIdUsuario()));

        if (matchExistente) {
            throw new Exception("Ya existe un match entre estos usuarios.");
        }

        // ============ CREAR EL MATCH ============
        Match nuevoMatch = new Match.Builder()
            .usuarioA(usuarioA)
            .usuarioB(usuarioB)
            .fechaMatch(LocalDateTime.now())
            .build();

        matchDAO.insertar(nuevoMatch);

        // ============ CREAR CHAT ASOCIADO ============
        Chat nuevoChat = new Chat.Builder()
            .nombre(usuarioA.getNombre() + " & " + usuarioB.getNombre())
            .match(nuevoMatch)
            .participantes(new HashSet<Usuario>() {{
                add(usuarioA);
                add(usuarioB);
            }})
            .build();

        chatDAO.insertar(nuevoChat);
    }

    /**
     * Valida que no exista un bloqueo entre dos usuarios
     */
    private void validarNoExistaBloqueo(Usuario usuarioA, Usuario usuarioB) throws Exception {
        // Validar si A bloqueó a B
        List<Bloqueo> bloqueosDeA = bloqueoDAO.buscarPorBloqueador(usuarioA);
        boolean ABloqueaAB = bloqueosDeA.stream()
            .anyMatch(b -> b.getUsuarioBloqueado().getIdUsuario().equals(usuarioB.getIdUsuario()));

        if (ABloqueaAB) {
            throw new Exception("Has bloqueado a este usuario.");
        }

        // Validar si B bloqueó a A
        List<Bloqueo> bloqueosDeB = bloqueoDAO.buscarPorBloqueador(usuarioB);
        boolean BBloquearA = bloqueosDeB.stream()
            .anyMatch(b -> b.getUsuarioBloqueado().getIdUsuario().equals(usuarioA.getIdUsuario()));

        if (BBloquearA) {
            throw new Exception("Este usuario te ha bloqueado.");
        }
    }

    /**
     * Deshabilita el chat entre dos usuarios después de un bloqueo
     */
    private void deshabilitarChatEntre(Usuario usuarioBloqueador, Usuario usuarioBloqueado) {
        try {
            // Obtener chats del bloqueador
            List<Chat> chatsDelBloqueador = chatDAO.buscarPorParticipante(usuarioBloqueador);

            if (chatsDelBloqueador != null) {
                // Encontrar chats que incluyan a ambos usuarios
                for (Chat chat : chatsDelBloqueador) {
                    boolean tieneAlOtro = chat.getParticipantes().stream()
                        .anyMatch(u -> u.getIdUsuario().equals(usuarioBloqueado.getIdUsuario()));

                    if (tieneAlOtro) {
                        // Eliminar el chat
                        chatDAO.eliminar(chat.getIdChat());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al deshabilitar chat: " + e.getMessage());
        }
    }
}