
package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IBloqueoDAO;
import com.mycompany.hiChatJpa.dao.IInteraccionDAO;
import com.mycompany.hiChatJpa.dao.IUsuarioDAO;
import com.mycompany.hiChatJpa.dao.impl.BloqueoDAO;
import com.mycompany.hiChatJpa.dao.impl.InteraccionDAO;
import com.mycompany.hiChatJpa.dao.impl.UsuarioDAO;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IUsuarioService;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.JLabel;


/**
 * 
 * author gatog
 */
public class UsuarioService implements IUsuarioService {


    private final IUsuarioDAO usuarioDAO;
    private final IBloqueoDAO bloqueoDAO;
    private final IInteraccionDAO interaccionDAO;

    // Patrón para validar email
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.bloqueoDAO = new BloqueoDAO();
        this.interaccionDAO = new InteraccionDAO();
    }
    
    // ==================== MÉTODOS PRIVADOS DE VALIDACIÓN ====================

    /**
     * Valida el formato de un email
     * @param email Correo a validar
     * @return true si es válido
     */
    private boolean validarEmail(String email) {
        return PATTERN.matcher(email).matches();
    }

    /**
     * Valida que el usuario tenga al menos 18 años
     * @param fechaNacimiento Fecha de nacimiento
     * @return true si es mayor de edad
     */
    private boolean validarEdadMinima(java.time.LocalDate fechaNacimiento) {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.Period periodo = java.time.Period.between(fechaNacimiento, hoy);
        return periodo.getYears() >= 18;
    }

    /**
     * Inicia sesión de un usuario validando credenciales
     * @param usuario Usuario con correo y contraseña
     * @return Usuario autenticado o null si falla
     * @throws Exception si hay error en la validación
     */
    @Override
    public Usuario iniciarSesion(Usuario usuario) throws Exception {
        // Validar datos de entrada
        if (usuario == null) {
            throw new Exception("El usuario no puede ser nulo.");
        }
        if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().isEmpty()) {
            throw new Exception("El correo electrónico es obligatorio.");
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        // Buscar usuario por correo
        Usuario usuarioRegistrado = usuarioDAO.buscarPorCorreo(usuario.getCorreoElectronico());
        
        // Validar que existe
        if (usuarioRegistrado == null) {
            throw new Exception("El usuario no existe.");
        }

        // Validar que la contraseña coincida
        if (!usuarioRegistrado.getContrasena().equals(usuario.getContrasena())) {
            throw new Exception("Contraseña incorrecta.");
        }

        return usuarioRegistrado;
    }

    /**
     * Registra un nuevo usuario validando unicidad y formato
     * @param usuario Datos del nuevo usuario
     * @return true si se registra correctamente
     * @throws Exception si hay error en la validación
     */
    @Override
    public void registrarUsuario(Usuario usuario) throws Exception {
        // Validar datos de entrada
        if (usuario == null) {
            throw new Exception("El usuario no puede ser nulo.");
        }

        // Validar nombre
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre es obligatorio.");
        }
        if (usuario.getNombre().length() < 2 || usuario.getNombre().length() > 50) {
            throw new Exception("El nombre debe tener entre 2 y 50 caracteres.");
        }

        // Validar apellido paterno
        if (usuario.getApellidoPaterno() == null || usuario.getApellidoPaterno().trim().isEmpty()) {
            throw new Exception("El apellido paterno es obligatorio.");
        }
        if (usuario.getApellidoPaterno().length() < 2 || usuario.getApellidoPaterno().length() > 50) {
            throw new Exception("El apellido paterno debe tener entre 2 y 50 caracteres.");
        }

        // Validar correo electrónico
        if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().isEmpty()) {
            throw new Exception("El correo electrónico es obligatorio.");
        }
        if (!validarEmail(usuario.getCorreoElectronico())) {
            throw new Exception("El formato del correo electrónico es inválido.");
        }
        if (usuario.getCorreoElectronico().length() > 100) {
            throw new Exception("El correo no puede exceder 100 caracteres.");
        }

        // Validar que no exista duplicado
        Usuario existente = usuarioDAO.buscarPorCorreo(usuario.getCorreoElectronico());
        if (existente != null) {
            throw new Exception("Ya existe un usuario registrado con ese correo.");
        }

        // Validar contraseña
        if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        if (usuario.getContrasena().length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres.");
        }
        if (usuario.getContrasena().length() > 255) {
            throw new Exception("La contraseña no puede exceder 255 caracteres.");
        }

        // Validar fecha de nacimiento si está presente
        if (usuario.getFechaNacimiento() != null) {
            if (!validarEdadMinima(usuario.getFechaNacimiento())) {
                throw new Exception("Debes tener al menos 18 años para registrarte.");
            }
        }

        // Insertar usuario
        usuarioDAO.insertar(usuario);
    }

    /**
     * Actualiza la información de un usuario existente
     * @param usuario Usuario con datos actualizados
     * @throws Exception si hay error en la validación
     */
    @Override
    public void actualizarUsuario(Usuario usuario) throws Exception {
        // Validar datos de entrada
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new Exception("Debe especificar un usuario válido para actualizar.");
        }

        // Verificar que existe
        Usuario usuarioExistente = usuarioDAO.buscar(usuario.getIdUsuario());
        if (usuarioExistente == null) {
            throw new Exception("El usuario no existe.");
        }

        // Validar nombre si fue modificado
        if (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
            if (usuario.getNombre().length() < 2 || usuario.getNombre().length() > 50) {
                throw new Exception("El nombre debe tener entre 2 y 50 caracteres.");
            }
        }

        // Validar apellido paterno si fue modificado
        if (usuario.getApellidoPaterno() != null && !usuario.getApellidoPaterno().isEmpty()) {
            if (usuario.getApellidoPaterno().length() < 2 || usuario.getApellidoPaterno().length() > 50) {
                throw new Exception("El apellido paterno debe tener entre 2 y 50 caracteres.");
            }
        }

        // Validar correo si fue modificado
        if (usuario.getCorreoElectronico() != null && !usuario.getCorreoElectronico().isEmpty()) {
            if (!validarEmail(usuario.getCorreoElectronico())) {
                throw new Exception("El formato del correo electrónico es inválido.");
            }
            
            // Verificar que no exista otro usuario con el mismo correo
            Usuario otroUsuario = usuarioDAO.buscarPorCorreo(usuario.getCorreoElectronico());
            if (otroUsuario != null && !otroUsuario.getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new Exception("Ya existe otro usuario con ese correo.");
            }
        }

        // Validar fecha de nacimiento si fue modificada
        if (usuario.getFechaNacimiento() != null) {
            if (!validarEdadMinima(usuario.getFechaNacimiento())) {
                throw new Exception("Debes tener al menos 18 años.");
            }
        }

        // Validar carrera si fue modificada
        if (usuario.getCarrera() != null && usuario.getCarrera().length() > 100) {
            throw new Exception("La carrera no puede exceder 100 caracteres.");
        }

        // Validar biografía
        if (usuario.getBiografia() != null && usuario.getBiografia().length() > 500) {
            throw new Exception("La biografía no puede exceder 500 caracteres.");
        }

        // Actualizar
        usuarioDAO.actualizar(usuario);
    }

    /**
     * Restablece la contraseña de un usuario
     * @param idUsuario ID del usuario
     * @param nuevaContrasenia Nueva contraseña
     * @throws Exception si hay error en la validación
     */
    @Override
    public void reestablecerContrasenia(Long idUsuario, String nuevaContrasenia) throws Exception {
        // Validar ID
        if (idUsuario == null || idUsuario <= 0) {
            throw new Exception("ID de usuario inválido.");
        }

        // Validar nueva contraseña
        if (nuevaContrasenia == null || nuevaContrasenia.isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía.");
        }
        if (nuevaContrasenia.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres.");
        }
        if (nuevaContrasenia.length() > 255) {
            throw new Exception("La contraseña no puede exceder 255 caracteres.");
        }

        // Verificar que existe
        Usuario usuario = usuarioDAO.buscar(idUsuario);
        if (usuario == null) {
            throw new Exception("El usuario no existe.");
        }

        // Validar que no sea la misma contraseña actual
        if (usuario.getContrasena().equals(nuevaContrasenia)) {
            throw new Exception("La nueva contraseña no puede ser igual a la actual.");
        }

        // Actualizar contraseña
        usuario.setContrasena(nuevaContrasenia);
        usuarioDAO.actualizar(usuario);
    }

    /**
     * Busca usuarios por nombre
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de usuarios encontrados
     */
    @Override
    public List<Usuario> buscarPorNombreCompleto(String nombre, String apellidoPaterno) {
        // Validar datos de entrada
        if (nombre == null || nombre.isEmpty() || apellidoPaterno == null || apellidoPaterno.isEmpty()) {
            return null;
        }

        List<Usuario> lista = usuarioDAO.buscarPorNombreCompleto(nombre, apellidoPaterno);
        if (lista == null) return lista;

        // Limitar a 100 resultados
        return lista.size() > 100 ? lista.subList(0, 100) : lista;
    }

    /**
     * Busca usuarios por nombre (parcial)
     * @param nombre Nombre o parte del nombre
     * @return Lista de usuarios encontrados
     */
    @Override
    public List<Usuario> fitrarUsuariosPorNombre(String nombre) throws Exception {
        // Validar datos de entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre de búsqueda es obligatorio.");
        }

        if (nombre.length() < 2) {
            throw new Exception("Ingresa al menos 2 caracteres para buscar.");
        }

        // Listar todos y filtrar por nombre (alternativa si no hay DAO específico)
        List<Usuario> usuarios = usuarioDAO.listar();
        
        if (usuarios == null) {
            return null;
        }

        // Filtrar por nombre que contenga la búsqueda
        List<Usuario> resultados = usuarios.stream()
            .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                        u.getApellidoPaterno().toLowerCase().contains(nombre.toLowerCase()))
            .limit(100)
            .collect(Collectors.toList());

        return resultados.isEmpty() ? null : resultados;
    }

    /**
     * Obtiene la lista de pretendientes (usuarios desconocidos)
     * Retorna usuarios con los que no hay bloqueos ni interacciones previas
     * @param idUsuarioActual ID del usuario actual
     * @return Lista de usuarios pretendientes
     * @throws Exception si hay error
     */
    @Override
    public List<Usuario> mostrarPretendientes(Long idUsuarioActual) throws Exception {
        // Validar ID
        if (idUsuarioActual == null || idUsuarioActual <= 0) {
            throw new Exception("ID de usuario inválido.");
        }
        
        // Verificar que existe el usuario actual
        Usuario usuarioActual = usuarioDAO.buscar(idUsuarioActual);
        if (usuarioActual == null) {
            throw new Exception("El usuario no existe.");
        }

        // Obtener todos los usuarios
        List<Usuario> todosLosUsuarios = usuarioDAO.listar();
        if (todosLosUsuarios == null || todosLosUsuarios.isEmpty()) {
            return null;
        }

        // Obtener IDs de usuarios bloqueados por el usuario actual
        List<Bloqueo> bloqueosPorUsuario = bloqueoDAO.buscarPorBloqueador(usuarioActual);
        List<Long> idsUsuariosBloqueados = bloqueosPorUsuario.stream()
            .map(b -> b.getUsuarioBloqueado().getIdUsuario())
            .collect(Collectors.toList());

        // Obtener IDs de usuarios que han bloqueado al usuario actual
        List<Bloqueo> bloqueosRecibidos = bloqueoDAO.buscarPorBloqueado(usuarioActual);
        List<Long> idsUsuariosQueBloquean = bloqueosRecibidos.stream()
            .map(b -> b.getUsuarioBloqueador().getIdUsuario())
            .collect(Collectors.toList());

        // Obtener IDs de usuarios con los que ha interaccionado
        List<Interaccion> interaccionesEnviadas = interaccionDAO.buscarPorEmisor(usuarioActual);
        List<Long> idsInteraccionados = interaccionesEnviadas.stream()
            .map(i -> i.getUsuarioReceptor().getIdUsuario())
            .collect(Collectors.toList());

        // Filtrar usuarios: excluir al mismo usuario y los que tienen bloqueos o interacciones
        List<Usuario> pretendientes = todosLosUsuarios.stream()
            .filter(u -> !u.getIdUsuario().equals(idUsuarioActual)) // No a sí mismo
            .filter(u -> !idsUsuariosBloqueados.contains(u.getIdUsuario())) // No bloqueados
            .filter(u -> !idsUsuariosQueBloquean.contains(u.getIdUsuario())) // No que lo bloquean
            .filter(u -> !idsInteraccionados.contains(u.getIdUsuario())) // Sin interacciones previas
            .limit(100)
            .collect(Collectors.toList());

        return pretendientes.isEmpty() ? null : pretendientes;
    }

    /**
     * Busca un usuario por ID
     * @param id ID del usuario
     * @return Usuario encontrado o null
     */
    @Override
    public Usuario buscarPorId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return usuarioDAO.buscar(id);
    }

    /**
     * Lista todos los usuarios
     * @return Lista de usuarios
     */
    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = usuarioDAO.listar();
        if (lista == null) return lista;
        return lista.size() > 100 ? null : lista;
    }

    /**
     * Busca un usuario por correo
     * @param correo Correo electrónico
     * @return Usuario encontrado o null
     */
    @Override
    public Usuario buscarPorCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
            return null;
        }
        return usuarioDAO.buscarPorCorreo(correo);
    }

    /**
     * Elimina un usuario
     * @param id ID del usuario
     * @throws Exception si hay error
     */
    @Override
    public void eliminarUsuario(Long id) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("ID inválido para eliminar usuario.");
        }

        if (usuarioDAO.buscar(id) == null) {
            throw new Exception("El usuario no existe.");
        }

        usuarioDAO.eliminar(id);
    }

    public JLabel crearImagen(){
        JLabel img = new JLabel();
        return img;
    }
    
}
