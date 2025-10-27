
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IChatDAO;
import com.mycompany.hiChatJpa.dao.impl.ChatDAO;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IChatService;
import java.util.List;


/**
 * Implementación de la capa de servicio para la entidad service
 * @author gatog
 */
public class ChatService implements IChatService {

    private final IChatDAO chatDAO;

    public ChatService() {
        this.chatDAO = new ChatDAO();
    }

    /**
     * metodo que valida y registra un chat
     * @param chat
     * @throws Exception 
     */
    @Override
    public void registrarChat(Chat chat) throws Exception {
        // validación de datos de entrada
        if (chat == null) {
            throw new Exception("El chat no puede ser nulo.");
        }
        if (chat.getNombre() == null || chat.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del chat es obligatorio.");
        }
        if (chat.getParticipantes() == null || chat.getParticipantes().isEmpty()) {
            throw new Exception("Debe haber al menos un participante en el chat.");
        }

        // evitar duplicados por nombre
        List<Chat> existentes = chatDAO.buscarPorNombre(chat.getNombre());
        boolean duplicado = existentes.stream()
                .anyMatch(item -> item.getNombre().equalsIgnoreCase(chat.getNombre()));
        if (duplicado) {
            throw new Exception("Ya existe un chat con ese nombre.");
        }

        // ejecutar
        chatDAO.insertar(chat);
    }

    /**
     * metodo que valida y actualiza un chat
     * @param chat
     * @throws Exception 
     */
    @Override
    public void actualizarChat(Chat chat) throws Exception {
        // validaciones de datos de entrada
        if (chat == null || chat.getIdChat() == null) {
            throw new Exception("Debe especificar un chat válido para actualizar.");
        }

        // verificar que exista
        boolean falta = chatDAO.buscar(chat.getIdChat()) == null;
        if (falta) {
            throw new Exception("Debe existir un chat para actualizar.");
        }

        // ejecutar
        chatDAO.actualizar(chat);
    }

    /**
     * metodo que valida y elimina un chat
     * @param id
     * @throws Exception 
     */
    @Override
    public void eliminarChat(Long id) throws Exception {
        // validación de datos de entrada
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar chat.");
        }

        // verificar que exista
        boolean falta = chatDAO.buscar(id) == null;
        if (falta) {
            throw new Exception("Debe existir un chat para eliminar.");
        }

        // ejecutar
        chatDAO.eliminar(id);
    }

    /**
     * metodo que valida y busca un chat por id
     * @param id
     * @return 
     */
    @Override
    public Chat buscarPorId(Long id) {
        // valida datos de entrada
        if (id == null || id <= 0) {
            return null;
        }

        // ejecutar
        return chatDAO.buscar(id);
    }

    /**
     * metodo que lista no más de 100 chats
     * @return 
     */
    @Override
    public List<Chat> listarChats() {
        // valida límite de registros
        List<Chat> lista = chatDAO.listar();
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }

    /**
     * metodo que valida y lista los chats por nombre
     * @param nombre
     * @return 
     */
    @Override
    public List<Chat> listarPorNombre(String nombre) {
        // validación de datos de entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        // valida límite de registros
        List<Chat> lista = chatDAO.buscarPorNombre(nombre);
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }

    /**
     * metodo que valida y lista los chats por participante
     * @param usuario
     * @return 
     */
    @Override
    public List<Chat> listarPorParticipante(Usuario usuario) {
        // validación de datos de entrada
        if (usuario == null) {
            return null;
        }

        // valida límite de registros
        List<Chat> lista = chatDAO.buscarPorParticipante(usuario);
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }
}

