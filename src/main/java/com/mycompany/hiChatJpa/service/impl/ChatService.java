
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IBloqueoDAO;
import com.mycompany.hiChatJpa.dao.IChatDAO;
import com.mycompany.hiChatJpa.dao.IMensajeDAO;
import com.mycompany.hiChatJpa.dao.impl.BloqueoDAO;
import com.mycompany.hiChatJpa.dao.impl.ChatDAO;
import com.mycompany.hiChatJpa.dao.impl.MensajeDAO;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IChatService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Implementación de la capa de servicio para la entidad service
 * @author gatog
 */
public class ChatService implements IChatService {

    private final IChatDAO chatDAO;
    private final IMensajeDAO mensajeDAO;
    private final IBloqueoDAO bloqueoDAO;

    public ChatService() {
        this.chatDAO = new ChatDAO();
        this.mensajeDAO = new MensajeDAO();
        this.bloqueoDAO = new BloqueoDAO();
    }
    /**
     * Obtiene la fecha del último mensaje en un chat
     * Si no hay mensajes, retorna la fecha actual
     * 
     * @param chat Chat
     * @return LocalDateTime del último mensaje
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
     * Valida que el chat exista y que el nuevo nombre sea válido
     * 
     * @param chatId ID del chat a modificar
     * @param nuevoNombre Nuevo nombre para el chat
     * @throws Exception si hay error en la validación
     */
    public void cambiarAliasDelChat(Long chatId, String nuevoNombre) throws Exception {
        
        String nombreLimpio = nuevoNombre.trim();
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

        // Validar que no exista otro chat con el mismo nombre
        List<Chat> chatsConNombre = chatDAO.buscarPorNombre(nombreLimpio);
        boolean nombreEnUso = chatsConNombre.stream()
            .anyMatch(c -> !c.getIdChat().equals(chatId) && 
                          c.getNombre().equalsIgnoreCase(nombreLimpio));
        
        if (nombreEnUso) {
            throw new Exception("Ya existe otro chat con ese nombre.");
        }

        // Actualizar el nombre
        chatExistente.setNombre(nuevoNombre);
        chatDAO.actualizar(chatExistente);
    }

    /**
     * Carga todos los chats de un usuario
     * Retorna los chats donde el usuario es participante
     * 
     * @param usuario Usuario del cual cargar chats
     * @return Lista de chats del usuario
     * @throws Exception si hay error
     */
    public List<Chat> cargarChatsDelUsuario(Usuario usuario) throws Exception {
        // Validar usuario
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new Exception("Usuario inválido.");
        }

        if (usuario.getIdUsuario() <= 0) {
            throw new Exception("ID del usuario inválido.");
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

        // Ordenar por última actividad (si es posible con los datos disponibles)
        // Los chats más recientes primero
        List<Chat> chatsOrdenados = chatsDelUsuario.stream()
            .sorted((c1, c2) -> {
                // Obtener el último mensaje de cada chat
                LocalDateTime fecha1 = obtenerFechaUltimoMensaje(c1);
                LocalDateTime fecha2 = obtenerFechaUltimoMensaje(c2);
                
                // Ordenar descendente (más recientes primero)
                return fecha2.compareTo(fecha1);
            })
            .collect(Collectors.toList());

        return chatsOrdenados;
    }

    /**
     * Envía un mensaje en un chat validando bloqueos y otros criterios
     * Valida que:
     * - El chat exista
     * - El mensaje sea válido
     * - El usuario emisor sea participante del chat
     * - No exista bloqueo entre participantes
     * 
     * @param chat Chat donde enviar el mensaje
     * @param mensaje Mensaje a enviar
     * @throws Exception si hay error en la validación
     */
    public void mandarMensaje(Chat chat, Mensaje mensaje) throws Exception {
        // ============ VALIDACIONES DEL CHAT ============
        if (chat == null || chat.getIdChat() == null) {
            throw new Exception("El chat es inválido.");
        }

        // Verificar que el chat existe
        Chat chatExistente = chatDAO.buscar(chat.getIdChat());
        if (chatExistente == null) {
            throw new Exception("El chat no existe.");
        }

        // ============ VALIDACIONES DEL MENSAJE ============
        if (mensaje == null) {
            throw new Exception("El mensaje no puede ser nulo.");
        }

        if (mensaje.getContenido() == null || mensaje.getContenido().trim().isEmpty()) {
            throw new Exception("El contenido del mensaje no puede estar vacío.");
        }

        String contenido = mensaje.getContenido().trim();

        // Validar longitud del mensaje
        if (contenido.length() > 5000) {
            throw new Exception("El mensaje no puede exceder 5000 caracteres.");
        }

        // ============ VALIDACIONES DEL EMISOR ============
        if (mensaje.getUsuarioEmisor() == null || mensaje.getUsuarioEmisor().getIdUsuario() == null) {
            throw new Exception("Debe especificar el usuario emisor.");
        }

        Long idEmisor = mensaje.getUsuarioEmisor().getIdUsuario();

        // Verificar que el emisor es participante del chat
        boolean esParticipante = chatExistente.getParticipantes().stream()
            .anyMatch(u -> u.getIdUsuario().equals(idEmisor));

        if (!esParticipante) {
            throw new Exception("El usuario no es participante de este chat.");
        }

        // ============ VALIDACIONES DE BLOQUEOS ============
        // Obtener otros participantes del chat
        Set<Usuario> otrosParticipantes = chatExistente.getParticipantes().stream()
            .filter(u -> !u.getIdUsuario().equals(idEmisor))
            .collect(Collectors.toSet());

        // Validar que no exista bloqueo con ningún otro participante
        Usuario emisor = mensaje.getUsuarioEmisor();

        for (Usuario otroParticipante : otrosParticipantes) {
            // Verificar si el emisor bloqueó a este participante
            List<Bloqueo> bloqueosDelEmisor = bloqueoDAO.buscarPorBloqueador(emisor);
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
                    .equals(idEmisor));

            if (otroBloquealEmisor) {
                throw new Exception("No puedes enviar mensajes. Uno de los participantes te ha bloqueado.");
            }
        }

        // ============ ACTUALIZAR DATOS DEL MENSAJE ============
        mensaje.setChat(chatExistente);
        mensaje.setContenido(contenido);
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setEstaVisto(false);

        // ============ GUARDAR EL MENSAJE ============
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

