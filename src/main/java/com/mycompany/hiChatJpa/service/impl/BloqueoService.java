
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IBloqueoDAO;
import com.mycompany.hiChatJpa.dao.impl.BloqueoDAO;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IBloqueoService;

import java.util.List;

/**
 * Implementación de la capa de servicio para la entidad Bloqueo
 * 
 * @author gatog
 */
public class BloqueoService implements IBloqueoService {

    private final IBloqueoDAO bloqueoDAO;

    public BloqueoService() {
        this.bloqueoDAO = new BloqueoDAO();
    }

    /**
     * metodo que valida y registra un bloqueo
     * @param b
     * @throws Exception 
     */
    @Override
    public void registrarBloqueo(Bloqueo b) throws Exception {
        //validacion de los datos de entrada
        if (b == null) {
            throw new Exception("El bloqueo no puede ser nulo.");
        }
        if (b.getUsuarioBloqueador()== null || b.getUsuarioBloqueado()== null) {
            throw new Exception("Debe especificar el usuario bloqueador y bloqueado.");
        }
        if (b.getUsuarioBloqueador().equals(b.getUsuarioBloqueado())) {
            throw new Exception("Un usuario no puede bloquearse a sí mismo.");
        }

        //evitar duplicados
        List<Bloqueo> bloqueosPrevios = bloqueoDAO.buscarPorBloqueador(b.getUsuarioBloqueador());
        
        boolean existe = bloqueosPrevios.stream()
                .anyMatch(item -> item.getUsuarioBloqueado().equals(b.getUsuarioBloqueado()));
        
        if (existe) {
            throw new Exception("El usuario ya había sido bloqueado anteriormente.");
        }

        //ejecutar
        bloqueoDAO.insertar(b);
    }

    /**
     * metodo que valida y actualiza un bloqueo
     * @param b
     * @throws Exception 
     */
    @Override
    public void actualizarBloqueo(Bloqueo b) throws Exception {
        //validaciones de datos de entrada
        if (b == null || b.getIdBloqueo()== null) {
            throw new Exception("Debe especificar un bloqueo válido para actualizar.");
        }
        
        //verificar que exista
        boolean falta = bloqueoDAO.buscar(b.getIdBloqueo()) == null;
        if(falta){
            throw new Exception("Debe existir un bloqueo para actualizar.");
        }
        
        //ejecuta
        bloqueoDAO.actualizar(b);
    }

    /**
     * metodo que valida y elimina un bloqueo
     * @param id
     * @throws Exception 
     */
    @Override
    public void eliminarBloqueo(Long id) throws Exception {
        //validacion de datos de entrada
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar bloqueo.");
        }
        
        //verificar que exista
        boolean falta = bloqueoDAO.buscar(id) == null;
        if(falta){
            throw new Exception("Debe existir un bloqueo para eliminar.");
        }
        
        //ejecuta
        bloqueoDAO.eliminar(id);
    }

    /**
     * metodo que valida y busca un bloqueo por id
     * @param id
     * @return 
     */
    @Override
    public Bloqueo buscarPorId(Long id) {
        //valida datos de entrada
        if (id == null || id <= 0) {
            return null;
        }
        
        //ejecuta
        return bloqueoDAO.buscar(id);
    }

    /**
     * metodo que lista no mas de 100 bloqueos
     * @return 
     */
    @Override
    public List<Bloqueo> listarBloqueos() {
        // valida límite de registros
        List<Bloqueo> lista = bloqueoDAO.listar();
        if (lista == null) return lista;
        
        if (lista.size() > 100) {
            return null;
        }
        
        //regresa
        return lista;
    }

    /**
     * metodo que valida y lista los bloqueos por bloqueador
     * @param usuario
     * @return 
     */
    @Override
    public List<Bloqueo> listarPorBloqueador(Usuario usuario) {
        //validacion de datos de entrada
        if (usuario == null) {
            return null;
        }
        
        // valida límite de registros
        List<Bloqueo> lista = bloqueoDAO.buscarPorBloqueador(usuario);
        if (lista == null) return lista;
        
        if (lista.size() > 100) {
            return null;
        }
        
        //regresa
        return lista;
    }

    /**
     * metodo que valida y lista los bloqueadores
     * @param usuario
     * @return 
     */
    @Override
    public List<Bloqueo> listarPorBloqueado(Usuario usuario) {
        //validacion de datos de entrada
        if (usuario == null) {
            return null;
        }
        
        // valida límite de registros
        List<Bloqueo> lista = bloqueoDAO.buscarPorBloqueado(usuario);
        if (lista == null) return lista;
        
        if (lista.size() > 100) {
            return null;
        }
        
        //regresa
        return lista;
    }
}

