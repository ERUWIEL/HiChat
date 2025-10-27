
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IFotoDAO;
import com.mycompany.hiChatJpa.dao.impl.FotoDAO;
import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IFotoService;
import java.util.List;



/**
 * 
 * @author gatog
 */
public class FotoService implements IFotoService {

    private final IFotoDAO fotoDAO;

    public FotoService() {
        this.fotoDAO = new FotoDAO();
    }

    /**
     * método que valida y registra una foto
     * @param foto
     * @throws Exception 
     */
    @Override
    public void registrarFoto(Foto foto) throws Exception {
        // validación de datos de entrada
        if (foto == null) {
            throw new Exception("La foto no puede ser nula.");
        }
        if (foto.getUsuario() == null) {
            throw new Exception("Debe especificar el usuario al que pertenece la foto.");
        }
        if (foto.getUrlFoto()== null || foto.getUrlFoto().trim().isEmpty()) {
            throw new Exception("La URL de la foto es obligatoria.");
        }

        // evitar duplicados por URL del mismo usuario
        List<Foto> fotosUsuario = fotoDAO.buscarPorUsuario(foto.getUsuario());
        boolean duplicado = fotosUsuario.stream()
                .anyMatch(item -> item.getUrlFoto().equalsIgnoreCase(foto.getUrlFoto()));
        if (duplicado) {
            throw new Exception("El usuario ya tiene registrada una foto con la misma URL.");
        }

        // ejecutar
        fotoDAO.insertar(foto);
    }

    /**
     * método que valida y actualiza una foto
     * @param foto
     * @throws Exception 
     */
    @Override
    public void actualizarFoto(Foto foto) throws Exception {
        // validaciones de datos de entrada
        if (foto == null || foto.getIdFoto() == null) {
            throw new Exception("Debe especificar una foto válida para actualizar.");
        }

        // verificar que exista
        boolean falta = fotoDAO.buscar(foto.getIdFoto()) == null;
        if (falta) {
            throw new Exception("Debe existir una foto para actualizar.");
        }

        // ejecutar
        fotoDAO.actualizar(foto);
    }

    /**
     * método que valida y elimina una foto
     * @param id
     * @throws Exception 
     */
    @Override
    public void eliminarFoto(Long id) throws Exception {
        // validación de datos de entrada
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar foto.");
        }

        // verificar que exista
        boolean falta = fotoDAO.buscar(id) == null;
        if (falta) {
            throw new Exception("Debe existir una foto para eliminar.");
        }

        // ejecutar
        fotoDAO.eliminar(id);
    }

    /**
     * método que valida y busca una foto por id
     * @param id
     * @return 
     */
    @Override
    public Foto buscarPorId(Long id) {
        // valida datos de entrada
        if (id == null || id <= 0) {
            return null;
        }

        // ejecutar
        return fotoDAO.buscar(id);
    }

    /**
     * método que lista no más de 100 fotos
     * @return 
     */
    @Override
    public List<Foto> listarFotos() {
        // valida límite de registros
        List<Foto> lista = fotoDAO.listar();
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }

    /**
     * método que valida y lista las fotos por usuario
     * @param usuario
     * @return 
     */
    @Override
    public List<Foto> listarPorUsuario(Usuario usuario) {
        // validación de datos de entrada
        if (usuario == null) {
            return null;
        }

        // valida límite de registros
        List<Foto> lista = fotoDAO.buscarPorUsuario(usuario);
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }

    /**
     * método que valida y lista las fotos por descripción
     * @param descripcion
     * @return 
     */
    @Override
    public List<Foto> listarPorDescripcion(String descripcion) {
        // validación de datos de entrada
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return null;
        }

        // valida límite de registros
        List<Foto> lista = fotoDAO.buscarPorDescripcion(descripcion);
        if (lista == null) return lista;

        if (lista.size() > 100) {
            return null;
        }

        // regresa
        return lista;
    }
}

