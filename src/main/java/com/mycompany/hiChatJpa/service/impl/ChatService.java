package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dto.ChatConMensajesDTO;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.exceptions.ServiceException;
import com.mycompany.hiChatJpa.service.IChatService;
import java.util.List;

/**
 * Implementación de la capa de servicio para Chat
 *
 * @author gatog
 */
public class ChatService implements IChatService {

    @Override
    public boolean cambiarAliasDelChat(Long chatId, String nuevoNombre) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean mandarMensaje(Long idChat, Long idUsuarioEmisor, String contenidoMensaje) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void marcarMensajeComoVisto(Long idMensaje) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminarMensaje(Long idMensaje) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ChatConMensajesDTO> cargarChatsDelUsuario(Long idUsuario) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Mensaje> obtenerMensajesDelChat(Long idChat) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
