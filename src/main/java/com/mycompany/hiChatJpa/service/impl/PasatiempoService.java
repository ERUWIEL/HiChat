
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IPasatiempoDAO;
import com.mycompany.hiChatJpa.dao.impl.PasatiempoDAO;
import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import com.mycompany.hiChatJpa.service.IPasatiempoService;
import java.util.List;


/**
 * 
 * 
 * author gatog
 */
public class PasatiempoService implements IPasatiempoService {

    private final IPasatiempoDAO pasatiempoDAO;

    public PasatiempoService() {
        this.pasatiempoDAO = new PasatiempoDAO();
    }

    @Override
    public void registrarPasatiempo(Pasatiempo pasatiempo) throws Exception {
        if (pasatiempo == null) {
            throw new Exception("El pasatiempo no puede ser nulo.");
        }
        if (pasatiempo.getNombre() == null || pasatiempo.getNombre().isEmpty()) {
            throw new Exception("El pasatiempo debe tener un nombre válido.");
        }

        // evitar duplicados por nombre
        Pasatiempo existente = pasatiempoDAO.buscarPorNombre(pasatiempo.getNombre());
        if (existente != null) {
            throw new Exception("Ya existe un pasatiempo con ese nombre.");
        }

        pasatiempoDAO.insertar(pasatiempo);
    }

    @Override
    public void actualizarPasatiempo(Pasatiempo pasatiempo) throws Exception {
        if (pasatiempo == null || pasatiempo.getIdPasatiempo() == null) {
            throw new Exception("Debe especificar un pasatiempo válido para actualizar.");
        }

        if (pasatiempoDAO.buscar(pasatiempo.getIdPasatiempo()) == null) {
            throw new Exception("El pasatiempo no existe.");
        }

        pasatiempoDAO.actualizar(pasatiempo);
    }

    @Override
    public void eliminarPasatiempo(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar pasatiempo.");
        }

        if (pasatiempoDAO.buscar(id) == null) {
            throw new Exception("El pasatiempo no existe.");
        }

        pasatiempoDAO.eliminar(id);
    }

    @Override
    public Pasatiempo buscarPorId(Long id) {
        if (id == null || id <= 0) return null;
        return pasatiempoDAO.buscar(id);
    }

    @Override
    public List<Pasatiempo> listarPasatiempos() {
        List<Pasatiempo> lista = pasatiempoDAO.listar();
        if (lista == null) return lista;
        return lista.size() > 100 ? null : lista;
    }

    @Override
    public Pasatiempo buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) return null;
        return pasatiempoDAO.buscarPorNombre(nombre);
    }
}

