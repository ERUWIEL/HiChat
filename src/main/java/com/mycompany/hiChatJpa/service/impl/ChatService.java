
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IBloqueoDAO;
import com.mycompany.hiChatJpa.dao.IChatDAO;
import com.mycompany.hiChatJpa.dao.IMensajeDAO;
import com.mycompany.hiChatJpa.dao.IUsuarioDAO;
import com.mycompany.hiChatJpa.dao.impl.BloqueoDAO;
import com.mycompany.hiChatJpa.dao.impl.ChatDAO;
import com.mycompany.hiChatJpa.dao.impl.MensajeDAO;
import com.mycompany.hiChatJpa.dao.impl.UsuarioDAO;
import com.mycompany.hiChatJpa.dto.ChatConMensajesDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IChatService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación de la capa de servicio para Chat
 * @author gatog
 */
public class ChatService implements IChatService {

    private final IChatDAO chatDAO;
    private final IMensajeDAO mensajeDAO;
    private final IBloqueoDAO bloqueoDAO;
    private final IUsuarioDAO usuarioDAO;

    public ChatService() {
        this.chatDAO = new ChatDAO();
        this.mensajeDAO = new MensajeDAO();
        this.bloqueoDAO = new BloqueoDAO();
        this.usuarioDAO = new UsuarioDAO();
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
     * Convierte Chat a ChatConMensajesDTO
     */
    private ChatConMensajesDTO chatADTO(Chat chat) {
        if (chat == null) return null;

        List<UsuarioPerfilDTO> participantesDTO = chat.getParticipantes().stream()
            .map(this::usuarioADTO)
            .collect(Collectors.toList());

        LocalDateTime fechaUltimoMensaje = LocalDateTime.now();
        String ultimoMensaje = "";
        Boolean hayNoLeidos = false;

        if (chat.getMensajes() != null && !chat.getMensajes().isEmpty()) {
            Mensaje ultimo = chat.getMensajes().stream()
                .max((m1, m2) -> m1.getFechaEnvio().compareTo(m2.getFechaEnvio()))
                .orElse(null);

            if (ultimo != null) {
                fechaUltimoMensaje = ultimo.getFechaEnvio();
                ultimoMensaje = ultimo.getContenido().length() > 50 ?
                    ultimo.getContenido().substring(0, 50) + "..." :
                    ultimo.getContenido();
            }

            hayNoLeidos = chat.getMensajes().stream()
                .anyMatch(m -> !m.getEstaVisto());
        }

        ChatConMensajesDTO dto = new ChatConMensajesDTO();
        dto.setIdChat(chat.getIdChat());
        dto.setNombre(chat.getNombre());
        dto.setIdMatch(chat.getMatch() != null ? chat.getMatch().getIdMatch() : null);
        dto.setParticipantes(participantesDTO);
        dto.setTotalMensajes(chat.getMensajes() != null ? chat.getMensajes().size() : 0);
        dto.setUltimoMensaje(ultimoMensaje);
        dto.setFechaUltimoMensaje(fechaUltimoMensaje);
        dto.setHayNoLeidos(hayNoLeidos);

        return dto;
    }

    /**
     * Obtiene la fecha del último mensaje en un chat
     */
    private LocalDateTime obtenerFechaUltimoMensaje(Chat chat) {
        if (chat.getMensajes() == null || chat.getMensajes().isEmpty()) {
            return LocalDateTime.now();
        }

        return chat.getMensajes().stream()
            .map(Mensaje::getFechaEnvio)
            .max(LocalDateTime::compareTo)
            .orElse(LocalDateTime.now());
    }

    /**
     * Cambia el alias (nombre) de un chat
     */
    @Override
    public void cambiarAliasDelChat(Long chatId, String nuevoNombre) throws Exception {
        // Validar ID del chat
        if (chatId == null || chatId <= 0) {
            throw new Exception("ID del chat inválido.");
        }

        // Validar nuevo nombre
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new Exception("El nuevo nombre del chat no puede estar vacío.");
        }

        nuevoNombre = nuevoNombre.trim();

        // Validar longitud del nombre
        if (nuevoNombre.length() < 1 || nuevoNombre.length() > 100) {
            throw new Exception("El nombre del chat debe tener entre 1 y 100 caracteres.");
        }

        // Buscar el chat existente
        Chat chatExistente = chatDAO.buscar(chatId);
        if (chatExistente == null) {
            throw new Exception("El chat no existe.");
        }

        // Validar que el nombre no sea igual al actual
        if (chatExistente.getNombre().equalsIgnoreCase(nuevoNombre)) {
            throw new Exception("El nuevo nombre es igual al actual.");
        }

        // Actualizar el nombre
        chatExistente.setNombre(nuevoNombre);
        chatDAO.actualizar(chatExistente);
    }

    /**
     * Carga todos los chats de un usuario
     */
    @Override
    public List<ChatConMensajesDTO> cargarChatsDelUsuario(Long idUsuario) throws Exception {
        // Validar ID del usuario
        if (idUsuario == null || idUsuario <= 0) {
            throw new Exception("ID del usuario inválido.");
        }

        // Verificar que el usuario existe
        Usuario usuario = usuarioDAO.buscar(idUsuario);
        if (usuario == null) {
            throw new Exception("El usuario no existe.");
        }

        // Obtener chats del usuario
        List<Chat> chatsDelUsuario = chatDAO.buscarPorParticipante(usuario);

        // Validar que existan chats
        if (chatsDelUsuario == null || chatsDelUsuario.isEmpty()) {
            throw new Exception("El usuario no tiene chats.");
        }

        // Validar límite
        if (chatsDelUsuario.size() > 100) {
            throw new Exception("Demasiados chats. Se limita a 100.");
        }

        // Ordenar por última actividad (más recientes primero)
        List<ChatConMensajesDTO> chatsOrdenados = chatsDelUsuario.stream()
            .sorted((c1, c2) -> {
                LocalDateTime fecha1 = obtenerFechaUltimoMensaje(c1);
                LocalDateTime fecha2 = obtenerFechaUltimoMensaje(c2);
                return fecha2.compareTo(fecha1);
            })
            .map(this::chatADTO)
            .collect(Collectors.toList());

        return chatsOrdenados;
    }

    /**
     * Envía un mensaje en un chat validando bloqueos
     */
    @Override
    public void mandarMensaje(Long idChat, Long idUsuarioEmisor, String contenidoMensaje) throws Exception {
        // ============ VALIDACIONES DEL CHAT ============
        if (idChat == null || idChat <= 0) {
            throw new Exception("El ID del chat es inválido.");
        }

        Chat chatExistente = chatDAO.buscar(idChat);
        if (chatExistente == null) {
            throw new Exception("El chat no existe.");
        }

        // ============ VALIDACIONES DEL USUARIO EMISOR ============
        if (idUsuarioEmisor == null || idUsuarioEmisor <= 0) {
            throw new Exception("El ID del usuario emisor es inválido.");
        }

        Usuario usuarioEmisor = usuarioDAO.buscar(idUsuarioEmisor);
        if (usuarioEmisor == null) {
            throw new Exception("El usuario emisor no existe.");
        }

        // ============ VALIDACIONES DEL MENSAJE ============
        if (contenidoMensaje == null || contenidoMensaje.trim().isEmpty()) {
            throw new Exception("El contenido del mensaje no puede estar vacío.");
        }

        String contenido = contenidoMensaje.trim();

        if (contenido.length() > 5000) {
            throw new Exception("El mensaje no puede exceder 5000 caracteres.");
        }

        // Verificar que el emisor es participante del chat
        boolean esParticipante = chatExistente.getParticipantes().stream()
            .anyMatch(u -> u.getIdUsuario().equals(idUsuarioEmisor));

        if (!esParticipante) {
            throw new Exception("El usuario no es participante de este chat.");
        }

        // ============ VALIDACIONES DE BLOQUEOS ============
        Set<Usuario> otrosParticipantes = chatExistente.getParticipantes().stream()
            .filter(u -> !u.getIdUsuario().equals(idUsuarioEmisor))
            .collect(Collectors.toSet());

        for (Usuario otroParticipante : otrosParticipantes) {
            // Verificar si el emisor bloqueó a este participante
            List<Bloqueo> bloqueosDelEmisor = bloqueoDAO.buscarPorBloqueador(usuarioEmisor);
            boolean emisorBloqueaAlOtro = bloqueosDelEmisor.stream()
                .anyMatch(b -> b.getUsuarioBloqueado().getIdUsuario()
                    .equals(otroParticipante.getIdUsuario()));

            if (emisorBloqueaAlOtro) {
                throw new Exception("No puedes enviar mensajes a usuarios que has bloqueado.");
            }

            // Verificar si este participante bloqueó al emisor
            List<Bloqueo> bloqueosDelOtro = bloqueoDAO.buscarPorBloqueador(otroParticipante);
            boolean otroBloquealEmisor = bloqueosDelOtro.stream()
                .anyMatch(b -> b.getUsuarioBloqueado().getIdUsuario()
                    .equals(idUsuarioEmisor));

            if (otroBloquealEmisor) {
                throw new Exception("No puedes enviar mensajes. Uno de los participantes te ha bloqueado.");
            }
        }

        // ============ CREAR Y GUARDAR EL MENSAJE ============
        Mensaje mensaje = new Mensaje.Builder()
            .chat(chatExistente)
            .usuarioEmisor(usuarioEmisor)
            .contenido(contenido)
            .fechaEnvio(LocalDateTime.now())
            .estaVisto(false)
            .build();

        mensajeDAO.insertar(mensaje);
    }
    /**
     * Obtiene todos los mensajes de un chat (excepto los borrados)
     * @param idChat ID del chat
     * @return Lista de mensajes
     * @throws Exception si hay error
     */
    public List<Mensaje> obtenerMensajesDelChat(Long idChat) throws Exception {
        // Validar ID
        if (idChat == null || idChat <= 0) {
            throw new Exception("ID de chat inválido.");
        }

        // Buscar chat
        Chat chat = chatDAO.buscar(idChat);
        if (chat == null) {
            throw new Exception("El chat no existe.");
        }

        // Obtener mensajes
        List<Mensaje> mensajes = mensajeDAO.buscarPorChat(chat);

        if (mensajes == null || mensajes.isEmpty()) {
            throw new Exception("No hay mensajes en este chat.");
        }

        // ordenar por fecha
        List<Mensaje> mensajesValidos = mensajes.stream()
            .sorted((m1, m2) -> m1.getFechaEnvio().compareTo(m2.getFechaEnvio()))
            .collect(Collectors.toList());

        return mensajesValidos.isEmpty() ? null : mensajesValidos;
    }

}

