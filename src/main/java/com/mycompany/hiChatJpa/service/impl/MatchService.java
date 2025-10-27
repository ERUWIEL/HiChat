package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IMatchDAO;
import com.mycompany.hiChatJpa.dao.impl.MatchDAO;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IMatchService;
import java.util.List;

/**
 * Implementación de la capa de servicio para la entidad interaccion author
 * gatog
 */
public class MatchService implements IMatchService {

    private final IMatchDAO matchDAO;

    public MatchService() {
        this.matchDAO = new MatchDAO();
    }

    /**
     * metodo que permite agregar un match validado
     * @param match
     * @throws Exception 
     */
    @Override
    public void registrarMatch(Match match) throws Exception {
        if (match == null) {
            throw new Exception("El match no puede ser nulo.");
        }
        if (match.getUsuarioA() == null || match.getUsuarioB() == null) {
            throw new Exception("Debe especificar ambos usuarios del match.");
        }
        if (match.getUsuarioA().equals(match.getUsuarioB())) {
            throw new Exception("Un usuario no puede hacer match consigo mismo.");
        }

        // evitar duplicados
        List<Match> previos = matchDAO.buscarPorUsuarioA(match.getUsuarioA());
        boolean duplicado = previos.stream().anyMatch(item
                -> item.getUsuarioB().equals(match.getUsuarioB()));
        if (duplicado) {
            throw new Exception("Ya existe un match entre estos usuarios.");
        }

        matchDAO.insertar(match);
    }

    /**
     * metodo que permite actualizar un match validado
     * @param match
     * @throws Exception 
     */
    @Override
    public void actualizarMatch(Match match) throws Exception {
        if (match == null || match.getIdMatch()== null) {
            throw new Exception("Debe especificar un match válido para actualizar.");
        }

        if (matchDAO.buscar(match.getIdMatch()) == null) {
            throw new Exception("El match no existe.");
        }

        matchDAO.actualizar(match);
    }

    /**
     * metodo que perimte eliminar un match validado
     * @param id
     * @throws Exception 
     */
    @Override
    public void eliminarMatch(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar match.");
        }

        if (matchDAO.buscar(id) == null) {
            throw new Exception("El match no existe.");
        }

        matchDAO.eliminar(id);
    }

    /**
     * metodo que permite buscar un match por su id validado
     * @param id
     * @return 
     */
    @Override
    public Match buscarPorId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return matchDAO.buscar(id);
    }

    /**
     * metodo que permite listar los matches de forma validada
     * @return 
     */
    @Override
    public List<Match> listarMatchs() {
        List<Match> lista = matchDAO.listar();
        if (lista == null) {
            return lista;
        }
        return lista.size() > 100 ? null : lista;
    }

    /**
     * metodo que permite buscar un match por su usuario a validado
     * @param usuario
     * @return 
     */
    @Override
    public List<Match> listarPorUsuarioA(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        List<Match> lista = matchDAO.buscarPorUsuarioA(usuario);
        if (lista == null) {
            return lista;
        }
        return lista.size() > 100 ? null : lista;
    }

    /**
     * metodo que permite buscar un match por su usuario b validado
     * @param usuario
     * @return 
     */
    @Override
    public List<Match> listarPorUsuarioB(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        List<Match> lista = matchDAO.buscarPorUsuarioB(usuario);
        if (lista == null) {
            return lista;
        }
        return lista.size() > 100 ? null : lista;
    }
}
