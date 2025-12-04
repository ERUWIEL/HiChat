package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.dto.ChatConMensajesDTO;
import com.mycompany.hiChatJpa.exceptions.ServiceException;
import java.util.List;

/**
 * Interfaz de servicio para Chat
 * Utiliza DTOs para encapsular datos
 *
 * @author gatog
 */
public interface IChatService {

    boolean cambiarAliasDelChat(Long chatId, String nuevoNombre) throws ServiceException;

    boolean mandarMensaje(Long idChat, Long idUsuarioEmisor, String contenidoMensaje) throws ServiceException;

    void marcarMensajeComoVisto(Long idMensaje) throws ServiceException;

    boolean eliminarMensaje(Long idMensaje) throws ServiceException;

    // BLOQUE PENDIENTE DE PAGINACION
    
    List<ChatConMensajesDTO> cargarChatsDelUsuario(Long idUsuario) throws ServiceException;
    
    List<Mensaje> obtenerMensajesDelChat(Long idChat) throws Exception;
}
