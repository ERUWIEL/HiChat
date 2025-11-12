package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define las reglas de negocio antes de llamar alos dao
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
     * @param usuario Usuario del cual cargar chats
     * @return Lista de chats del usuario ordenados
     * @throws Exception si hay error
     */
    List<Chat> cargarChatsDelUsuario(Usuario usuario) throws Exception;

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
    void mandarMensaje(Chat chat, Mensaje mensaje) throws Exception;

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
