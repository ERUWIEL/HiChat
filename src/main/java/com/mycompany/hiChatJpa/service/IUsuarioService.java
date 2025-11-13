
package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.List;

/**
 *
 * @author gatog
 */
public interface IUsuarioService {

    /**
     * Registra un nuevo usuario con validaciones
     * @param usuario Datos del nuevo usuario
     * @throws Exception si hay error en la validación
     */
    void registrarUsuario(Usuario usuario) throws Exception;

    /**
     * Actualiza la información de un usuario existente
     * @param usuario Usuario con datos actualizados
     * @throws Exception si hay error en la validación
     */
    void actualizarUsuario(Usuario usuario) throws Exception;

    /**
     * Elimina un usuario por ID
     * @param id ID del usuario
     * @throws Exception si hay error
     */
    void eliminarUsuario(Long id) throws Exception;

    /**
     * Busca un usuario por ID
     * @param id ID del usuario
     * @return Usuario encontrado o null
     */
    Usuario buscarPorId(Long id);

    /**
     * Lista todos los usuarios
     * @return Lista de usuarios
     */
    List<Usuario> listarUsuarios();

    /**
     * Busca un usuario por correo electrónico
     * @param correo Correo electrónico
     * @return Usuario encontrado o null
     */
    Usuario buscarPorCorreo(String correo);

    /**
     * Busca usuarios por nombre completo (nombre y apellido paterno)
     * @param nombre Nombre del usuario
     * @param apellidoPaterno Apellido paterno
     * @return Lista de usuarios encontrados
     */
    List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno);

    /**
     * Inicia sesión validando credenciales
     * @param usuario Usuario con correo y contraseña
     * @return Usuario autenticado
     * @throws Exception si las credenciales son inválidas
     */
    Usuario iniciarSesion(Usuario usuario) throws Exception;

    /**
     * Busca usuarios por nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de usuarios encontrados
     * @throws Exception si hay error
     */
    List<Usuario> fitrarUsuariosPorNombre(String nombre) throws Exception;

    /**
     * Obtiene la lista de pretendientes (usuarios desconocidos)
     * Retorna usuarios con los que no hay bloqueos ni interacciones previas
     * @param idUsuarioActual ID del usuario actual
     * @return Lista de usuarios pretendientes
     * @throws Exception si hay error
     */
    List<Usuario> mostrarPretendientes(Long idUsuarioActual) throws Exception;

    /**
     * Restablece la contraseña de un usuario
     * @param idUsuario ID del usuario
     * @param nuevaContrasenia Nueva contraseña
     * @throws Exception si hay error en la validación
     */
    void reestablecerContrasenia(Long idUsuario, String nuevaContrasenia) throws Exception;
}
