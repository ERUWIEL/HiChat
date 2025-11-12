package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 * interfaz que define los metodos crud de una interaccion
 * @author gatog
 */
public interface IInteraccionService {

    /**
     * Da un like a un usuario
     * Si el usuario receptor también dio like, se genera automáticamente un match
     * 
     * @param idUsuarioEmisor ID del usuario que da el like
     * @param idUsuarioReceptor ID del usuario que recibe el like
     * @return true si se crea un match, false si solo se registra el like
     * @throws Exception si hay error en la validación
     */
    boolean darLike(Long idUsuarioEmisor, Long idUsuarioReceptor) throws Exception;

    /**
     * Da un dislike a un usuario
     * 
     * @param idUsuarioEmisor ID del usuario que da el dislike
     * @param idUsuarioReceptor ID del usuario que recibe el dislike
     * @throws Exception si hay error en la validación
     */
    void darDislike(Long idUsuarioEmisor, Long idUsuarioReceptor) throws Exception;

    /**
     * Bloquea a un usuario
     * Después de bloquear, el chat se deshabilita para ambos
     * 
     * @param idUsuarioBloqueador ID del usuario que bloquea
     * @param idUsuarioBloqueado ID del usuario a bloquear
     * @throws Exception si hay error en la validación
     */
    void bloquearUsuario(Long idUsuarioBloqueador, Long idUsuarioBloqueado) throws Exception;

    /**
     * Desbloquea a un usuario
     * Verifica que exista un bloqueo previo
     * 
     * @param idUsuarioBloqueador ID del usuario que desbloquea
     * @param idUsuarioBloqueado ID del usuario a desbloquear
     * @throws Exception si hay error en la validación
     */
    void desbloquearUsuario(Long idUsuarioBloqueador, Long idUsuarioBloqueado) throws Exception;
}
