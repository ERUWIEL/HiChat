
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IMensajeDAO;
import com.mycompany.hiChatJpa.dao.impl.MensajeDAO;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IMensajeService;
import java.util.List;

/**
 * Implementación de la capa de servicio para la entidad mensaje
 * 
 * author gatog
 */
public class MensajeService implements IMensajeService {

    private final IMensajeDAO mensajeDAO;

    public MensajeService() {
        this.mensajeDAO = new MensajeDAO();
    }

    @Override
    public void registrarMensaje(Mensaje mensaje) throws Exception {
        if (mensaje == null) {
            throw new Exception("El mensaje no puede ser nulo.");
        }
        if (mensaje.getChat() == null) {
            throw new Exception("El mensaje debe pertenecer a un chat.");
        }
        if (mensaje.getUsuarioEmisor()== null) {
            throw new Exception("Debe especificar un emisor para el mensaje.");
        }
        if (mensaje.getContenido() == null || mensaje.getContenido().isEmpty()) {
            throw new Exception("El mensaje no puede estar vacío.");
        }

        // evitar duplicados recientes en el mismo chat
        List<Mensaje> previos = mensajeDAO.buscarPorChat(mensaje.getChat());
        boolean duplicado = previos.stream().anyMatch(item ->
                item.getUsuarioEmisor().equals(mensaje.getUsuarioEmisor()) &&
                item.getContenido().equals(mensaje.getContenido()));
        if (duplicado) {
            throw new Exception("Ya existe un mensaje idéntico reciente en este chat.");
        }

        mensajeDAO.insertar(mensaje);
    }

    @Override
    public void actualizarMensaje(Mensaje mensaje) throws Exception {
        if (mensaje == null || mensaje.getIdMensaje() == null) {
            throw new Exception("Debe especificar un mensaje válido para actualizar.");
        }

        if (mensajeDAO.buscar(mensaje.getIdMensaje()) == null) {
            throw new Exception("El mensaje no existe.");
        }

        mensajeDAO.actualizar(mensaje);
    }

    @Override
    public void eliminarMensaje(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar mensaje.");
        }

        if (mensajeDAO.buscar(id) == null) {
            throw new Exception("El mensaje no existe.");
        }

        mensajeDAO.eliminar(id);
    }

    @Override
    public Mensaje buscarPorId(Long id) {
        if (id == null || id <= 0) return null;
        return mensajeDAO.buscar(id);
    }

    @Override
    public List<Mensaje> listarMensajes() {
        List<Mensaje> lista = mensajeDAO.listar();
        if (lista == null) return lista;
        return lista.size() > 100 ? null : lista;
    }

    @Override
    public List<Mensaje> listarPorChat(Chat chat) {
        if (chat == null) return null;
        List<Mensaje> lista = mensajeDAO.buscarPorChat(chat);
        if (lista == null) return lista;
        return lista.size() > 100 ? null : lista;
    }

    @Override
    public List<Mensaje> listarNoVistosPorUsuario(Usuario usuario) {
        if (usuario == null) return null;
        List<Mensaje> lista = mensajeDAO.buscarNoVistosPorUsuario(usuario);
        if (lista == null) return lista;
        return lista.size() > 100 ? null : lista;
    }
}
