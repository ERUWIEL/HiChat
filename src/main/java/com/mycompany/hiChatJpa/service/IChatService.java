package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

import com.mycompany.hiChatJpa.dto.ChatConMensajesDTO;
import java.util.List;

/**
 * Interfaz de servicio para Chat
 * Utiliza DTOs para encapsular datos
 *
 * @author gatog
 */
public interface IChatService {

    /**
     * Cambia el alias (nombre) de un chat
     * Valida que el chat exista y que el nuevo nombre sea válido
     * 
     * @param chatId ID del chat a modificar
     * @param nuevoNombre Nuevo nombre para el chat
     * @throws Exception si hay error en la validación
     */
    void cambiarAliasDelChat(Long chatId, String nuevoNombre) throws Exception;

    /**
     * Carga todos los chats de un usuario
     * Retorna los chats donde el usuario es participante
     * Ordena los chats por última actividad (más recientes primero)
     * 
     * @param idUsuario ID del usuario del cual cargar chats
     * @return Lista de ChatConMensajesDTO ordenados por actividad
     * @throws Exception si hay error
     */
    List<ChatConMensajesDTO> cargarChatsDelUsuario(Long idUsuario) throws Exception;

    /**
     * Envía un mensaje en un chat validando bloqueos y otros criterios
     * Valida que:
     * - El chat exista
     * - El mensaje sea válido
     * - El usuario emisor sea participante del chat
     * - No exista bloqueo entre participantes
     * 
     * @param idChat ID del chat donde enviar el mensaje
     * @param idUsuarioEmisor ID del usuario que envía el mensaje
     * @param contenidoMensaje Contenido del mensaje
     * @throws Exception si hay error en la validación
     */
    void mandarMensaje(Long idChat, Long idUsuarioEmisor, String contenidoMensaje) throws Exception;
//    /**
//     * Marca un mensaje como visto
//     * @param idMensaje ID del mensaje
//     * @throws Exception si hay error
//     */
//    void marcarMensajeComoVisto(Long idMensaje) throws Exception;
//
//    /**
//     * Elimina (marca como borrado) un mensaje
//     * @param idMensaje ID del mensaje
//     * @throws Exception si hay error
//     */
//    void eliminarMensaje(Long idMensaje) throws Exception;
//
//    /**
//     * Obtiene todos los mensajes de un chat (excepto los borrados)
//     * @param idChat ID del chat
//     * @return Lista de mensajes ordenados por fecha
//     * @throws Exception si hay error
//     */
//    List<Mensaje> obtenerMensajesDelChat(Long idChat) throws Exception;
}
