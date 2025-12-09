package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.dto.ActualizarUsuarioDTO;
import com.mycompany.hiChatJpa.dto.LoginDTO;
import com.mycompany.hiChatJpa.dto.MatchDTO;
import com.mycompany.hiChatJpa.dto.RegistroDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;

import com.mycompany.hiChatJpa.exceptions.ServiceException;
import java.util.List;

/**
 * Interfaz de servicio para Usuario Utiliza DTOs para encapsular datos
 *
 * @author gatog
 */
public interface IUsuarioService {

    /**
     * Inicia sesión validando credenciales
     *
     * @param loginDTO Usuario con correo y contraseña
     * @return UsuarioPerfilDTO autenticado
     */
    UsuarioPerfilDTO iniciarSesion(LoginDTO loginDTO) throws ServiceException;

    /**
     * metodo que permite a un usuario interactuar con otro
     *
     * @param idEmisor
     * @param idReceptor
     * @param tipo
     * @return
     * @throws ServiceException
     */
    boolean registrarInteraccion(Long idEmisor, Long idReceptor, TipoInteraccion tipo) throws ServiceException;

    /**
     * metodo que permite al usuario a bloquear al usuario b
     * @param idEmisor
     * @param idReceptor
     * @return
     * @throws ServiceException 
     */
    boolean bloquearUsuario(Long idEmisor, Long idReceptor) throws ServiceException;
    
    /**
     * Registra un nuevo usuario con validaciones
     *
     * @param registroDTO Datos del nuevo usuario
     * @return
     */
    boolean registrarUsuario(RegistroDTO registroDTO) throws ServiceException;

    /**
     * Actualiza la información de un usuario existente
     *
     * @param actualizarDTO Usuario con datos actualizados
     * @return
     */
    boolean actualizarUsuario(ActualizarUsuarioDTO actualizarDTO) throws ServiceException;

    /**
     * Restablece la contraseña de un usuario
     *
     * @param idUsuario ID del usuario
     * @param nuevaContrasenia Nueva contraseña
     * @return
     */
    boolean reestablecerContrasenia(Long idUsuario, String nuevaContrasenia) throws ServiceException;

    /**
     * Elimina un usuario por ID
     *
     * @param id ID del usuario
     * @return
     */
    boolean eliminarUsuario(Long id) throws ServiceException;

    /**
     * Busca un usuario por ID
     *
     * @param id ID del usuario
     * @return UsuarioPerfilDTO encontrado o null
     */
    UsuarioPerfilDTO buscarPorId(Long id) throws ServiceException;

    /**
     * Busca un usuario por correo electrónico
     *
     * @param correo Correo electrónico
     * @return UsuarioPerfilDTO encontrado o null
     */
    UsuarioPerfilDTO buscarPorCorreo(String correo) throws ServiceException;

    // BLOQUE PENDIENTE DE PAGINACION
    List<UsuarioPerfilDTO> listarUsuarios() throws ServiceException;

    List<UsuarioPerfilDTO> buscarPorNombreCompleto(String nombre, String apellidoPaterno) throws ServiceException;

    List<UsuarioPerfilDTO> filtrarUsuariosPorNombre(String nombre) throws ServiceException;

    List<UsuarioPerfilDTO> mostrarPretendientes(Long idUsuarioActual) throws ServiceException;

    List<MatchDTO> mostrarMatches(Long idUsuario) throws ServiceException;

}
