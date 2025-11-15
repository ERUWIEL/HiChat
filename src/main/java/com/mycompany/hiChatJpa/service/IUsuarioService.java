package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.dto.ActualizarUsuarioDTO;
import com.mycompany.hiChatJpa.dto.LoginDTO;
import com.mycompany.hiChatJpa.dto.RegistroDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import java.util.List;

/**
 * Interfaz de servicio para Usuario
 * Utiliza DTOs para encapsular datos
 *
 * @author gatog
 */
public interface IUsuarioService {

    /**
     * Registra un nuevo usuario con validaciones
     * @param registroDTO Datos del nuevo usuario
     * @throws Exception si hay error en la validación
     */
    void registrarUsuario(RegistroDTO registroDTO) throws Exception;

    /**
     * Actualiza la información de un usuario existente
     * @param actualizarDTO Usuario con datos actualizados
     * @throws Exception si hay error en la validación
     */
    void actualizarUsuario(ActualizarUsuarioDTO actualizarDTO) throws Exception;

    /**
     * Elimina un usuario por ID
     * @param id ID del usuario
     * @throws Exception si hay error
     */
    void eliminarUsuario(Long id) throws Exception;

    /**
     * Busca un usuario por ID
     * @param id ID del usuario
     * @return UsuarioPerfilDTO encontrado o null
     */
    UsuarioPerfilDTO buscarPorId(Long id);

    /**
     * Lista todos los usuarios
     * @return Lista de UsuarioPerfilDTO
     */
    List<UsuarioPerfilDTO> listarUsuarios();

    /**
     * Busca un usuario por correo electrónico
     * @param correo Correo electrónico
     * @return UsuarioPerfilDTO encontrado o null
     */
    UsuarioPerfilDTO buscarPorCorreo(String correo);

    /**
     * Busca usuarios por nombre completo (nombre y apellido paterno)
     * @param nombre Nombre del usuario
     * @param apellidoPaterno Apellido paterno
     * @return Lista de UsuarioPerfilDTO encontrados
     */
    List<UsuarioPerfilDTO> buscarPorNombreCompleto(String nombre, String apellidoPaterno);

    /**
     * Inicia sesión validando credenciales
     * @param loginDTO Usuario con correo y contraseña
     * @return UsuarioPerfilDTO autenticado
     * @throws Exception si las credenciales son inválidas
     */
    UsuarioPerfilDTO iniciarSesion(LoginDTO loginDTO) throws Exception;

    /**
     * Busca usuarios por nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de UsuarioPerfilDTO encontrados
     * @throws Exception si hay error
     */
    List<UsuarioPerfilDTO> filtrarUsuariosPorNombre(String nombre) throws Exception;

    /**
     * Obtiene la lista de pretendientes (usuarios desconocidos)
     * Retorna usuarios con los que no hay bloqueos ni interacciones previas
     * @param idUsuarioActual ID del usuario actual
     * @return Lista de UsuarioPerfilDTO pretendientes
     * @throws Exception si hay error
     */
    List<UsuarioPerfilDTO> mostrarPretendientes(Long idUsuarioActual) throws Exception;

    /**
     * Restablece la contraseña de un usuario
     * @param idUsuario ID del usuario
     * @param nuevaContrasenia Nueva contraseña
     * @throws Exception si hay error en la validación
     */
    void reestablecerContrasenia(Long idUsuario, String nuevaContrasenia) throws Exception;
}