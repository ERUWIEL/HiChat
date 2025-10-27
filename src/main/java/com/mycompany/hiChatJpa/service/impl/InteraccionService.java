
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IInteraccionDAO;
import com.mycompany.hiChatJpa.dao.impl.InteraccionDAO;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IInteraccionService;
import java.util.List;

/**
 * Implementación de la capa de servicio para la entidad interaccion
 * 
 * @author gatog
 */
public class InteraccionService implements IInteraccionService {

    private final IInteraccionDAO interaccionDAO;

    public InteraccionService() {
        this.interaccionDAO = new InteraccionDAO();
    }

    /**
     * método que valida y registra una interaccion
     * @param interaccion
     * @throws Exception 
     */
    @Override
    public void registrarInteraccion(Interaccion interaccion) throws Exception {
        // validación de datos de entrada
        if (interaccion == null) {
            throw new Exception("La interacción no puede ser nula.");
        }
        if (interaccion.getUsuarioEmisor() == null || interaccion.getUsuarioReceptor() == null) {
            throw new Exception("Debe especificar los usuarios emisor y receptor.");
        }
        if (interaccion.getUsuarioEmisor().equals(interaccion.getUsuarioReceptor())) {
            throw new Exception("Un usuario no puede interactuar consigo mismo.");
        }
        if (interaccion.getTipo()== null) {
            throw new Exception("Debe especificar el tipo de interacción.");
        }

        // evitar duplicados de interacción reciente del mismo tipo
        List<Interaccion> previas = interaccionDAO.buscarPorEmisor(interaccion.getUsuarioEmisor());
        boolean duplicada = previas.stream().anyMatch(item ->
                item.getUsuarioReceptor().equals(interaccion.getUsuarioReceptor()) &&
                item.getTipo().equals(interaccion.getTipo()));
        if (duplicada) {
            throw new Exception("Ya existe una interacción de este tipo entre los mismos usuarios.");
        }

        // ejecutar
        interaccionDAO.insertar(interaccion);
    }

    /**
     * método que valida y actualiza una interaccion
     * @param interaccion
     * @throws Exception 
     */
    @Override
    public void actualizarInteraccion(Interaccion interaccion) throws Exception {
        // validaciones de datos de entrada
        if (interaccion == null || interaccion.getIdInteraccion() == null) {
            throw new Exception("Debe especificar una interacción válida para actualizar.");
        }

        // verificar que exista
        boolean falta = interaccionDAO.buscar(interaccion.getIdInteraccion()) == null;
        if (falta) {
            throw new Exception("Debe existir una interacción para actualizar.");
        }

        // ejecutar
        interaccionDAO.actualizar(interaccion);
    }

    /**
     * método que valida y elimina una interaccion
     * @param id
     * @throws Exception 
     */
    @Override
    public void eliminarInteraccion(Long id) throws Exception {
        // validación de datos de entrada
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar interacción.");
        }

        // verificar que exista
        boolean falta = interaccionDAO.buscar(id) == null;
        if (falta) {
            throw new Exception("Debe existir una interacción para eliminar.");
        }

        // ejecutar
        interaccionDAO.eliminar(id);
    }

    /**
     * método que valida y busca una interaccion por id
     * @param id
     * @return 
     */
    @Override
    public Interaccion buscarPorId(Long id) {
        // valida datos de entrada
        if (id == null || id <= 0) {
            return null;
        }

        // ejecutar
        return interaccionDAO.buscar(id);
    }

    /**
     * método que lista no más de 100 interacciones
     * @return 
     */
    @Override
    public List<Interaccion> listarInteracciones() {
        // valida límite de registros
        List<Interaccion> lista = interaccionDAO.listar();
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }

    /**
     * método que valida y lista las interacciones por emisor
     * @param usuario
     * @return 
     */
    @Override
    public List<Interaccion> listarPorEmisor(Usuario usuario) {
        // validación de datos de entrada
        if (usuario == null) {
            return null;
        }

        // valida límite de registros
        List<Interaccion> lista = interaccionDAO.buscarPorEmisor(usuario);
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }

    /**
     * método que valida y lista las interacciones por receptor
     * @param usuario
     * @return 
     */
    @Override
    public List<Interaccion> listarPorReceptor(Usuario usuario) {
        // validación de datos de entrada
        if (usuario == null) {
            return null;
        }

        // valida límite de registros
        List<Interaccion> lista = interaccionDAO.buscarPorReceptor(usuario);
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }

    /**
     * método que valida y lista las interacciones por tipo
     * @param tipo
     * @return 
     */
    @Override
    public List<Interaccion> listarPorTipo(TipoInteraccion tipo) {
        // validación de datos de entrada
        if (tipo == null) {
            return null;
        }

        // valida límite de registros
        List<Interaccion> lista = interaccionDAO.buscarPorTipo(tipo);
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }
}

