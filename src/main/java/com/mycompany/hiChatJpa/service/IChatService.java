package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.dto.ChatConMensajesDTO;
import com.mycompany.hiChatJpa.dto.MensajeDTO;
import com.mycompany.hiChatJpa.exceptions.ServiceException;
import java.util.List;

/**
 * Interfaz de servicio para Chat Utiliza DTOs para encapsular datos
 *
 * @author gatog
 */
public interface IChatService {

    boolean cambiarAliasDelChat(Long chatId, String nuevoNombre) throws ServiceException;

    boolean enviarMensaje(Long idChat, Long idUsuarioEmisor, String contenidoMensaje) throws ServiceException;

    void marcarMensajeComoVisto(Long idMensaje) throws ServiceException;

    void marcarMensajesDelChatComoVistos(Long idChat, Long idUsuarioReceptor) throws ServiceException;

    boolean eliminarMensaje(Long idMensaje) throws ServiceException;

    List<ChatConMensajesDTO> cargarChatsDelUsuario(Long idUsuario) throws ServiceException;

    List<MensajeDTO> obtenerMensajesDelChat(Long idChat, int limit, int offset) throws ServiceException;

    ChatConMensajesDTO obtenerDetallesChat(Long idChat) throws ServiceException;
}
