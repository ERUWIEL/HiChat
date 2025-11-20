package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.dao.IBloqueoDAO;
import com.mycompany.hiChatJpa.dao.IInteraccionDAO;
import com.mycompany.hiChatJpa.dao.IUsuarioDAO;
import com.mycompany.hiChatJpa.dao.impl.BloqueoDAO;
import com.mycompany.hiChatJpa.dao.impl.InteraccionDAO;
import com.mycompany.hiChatJpa.dao.impl.UsuarioDAO;
import com.mycompany.hiChatJpa.dto.ActualizarUsuarioDTO;
import com.mycompany.hiChatJpa.dto.LoginDTO;
import com.mycompany.hiChatJpa.dto.RegistroDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Genero;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.IUsuarioService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implementación de la capa de servicio para Usuario
 * @author gatog
 */
public class UsuarioService implements IUsuarioService {

    private final IUsuarioDAO usuarioDAO;
    private final IBloqueoDAO bloqueoDAO;
    private final IInteraccionDAO interaccionDAO;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.bloqueoDAO = new BloqueoDAO();
        this.interaccionDAO = new InteraccionDAO();
    }

    // ==================== MÉTODOS PRIVADOS DE VALIDACIÓN ====================

    /**
     * Convierte un Usuario a UsuarioPerfilDTO
     */
    private UsuarioPerfilDTO usuarioADTO(Usuario usuario) {
        if (usuario == null) return null;
        
        Integer edad = null;
        if (usuario.getFechaNacimiento() != null) {
            edad = Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears();
        }
        
        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setCarrera(usuario.getCarrera());
        dto.setBiografia(usuario.getBiografia());
        dto.setUrlFotoPerfil(usuario.getUrlFotoPerfil());
        dto.setGenero(usuario.getGenero() != null ? usuario.getGenero().toString() : null);
        dto.setEdad(edad);
        return dto;
    }

    /**
     * Convierte RegistroDTO a Usuario
     */
    private Usuario registroDTOAUsuario(RegistroDTO dto) {
        if (dto == null) return null;
        
        Genero genero = null;
        if (dto.getGenero() != null && !dto.getGenero().isEmpty()) {
            try {
                genero = Genero.valueOf(dto.getGenero());
            } catch (IllegalArgumentException e) {
                genero = null;
            }
        }

        Usuario usuario = new Usuario.Builder()
            .nombre(dto.getNombre())
            .apellidoPaterno(dto.getApellidoPaterno())
            .apellidoMaterno(dto.getApellidoMaterno())
            .correoElectronico(dto.getCorreoElectronico())
            .contrasena(dto.getContrasena())
            .carrera(dto.getCarrera())
            .genero(genero)
            .fechaNacimiento(dto.getFechaNacimiento())
            .build();
        
        return usuario;
    }

    /**
     * Valida el formato de un email
     */
    private boolean validarEmail(String email) {
        return pattern.matcher(email).matches();
    }

    /**
     * Valida que el usuario tenga al menos 18 años
     */
    private boolean validarEdadMinima(LocalDate fechaNacimiento) {
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(fechaNacimiento, hoy);
        return periodo.getYears() >= 18;
    }

    // ==================== IMPLEMENTACIÓN DE MÉTODOS ====================

    @Override
    public void registrarUsuario(RegistroDTO registroDTO) throws Exception {
        if (registroDTO == null) {
            throw new Exception("El usuario no puede ser nulo.");
        }

        if (registroDTO.getNombre() == null || registroDTO.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre es obligatorio.");
        }
        if (registroDTO.getNombre().length() < 2 || registroDTO.getNombre().length() > 50) {
            throw new Exception("El nombre debe tener entre 2 y 50 caracteres.");
        }

        if (registroDTO.getApellidoPaterno() == null || registroDTO.getApellidoPaterno().trim().isEmpty()) {
            throw new Exception("El apellido paterno es obligatorio.");
        }
        if (registroDTO.getApellidoPaterno().length() < 2 || registroDTO.getApellidoPaterno().length() > 50) {
            throw new Exception("El apellido paterno debe tener entre 2 y 50 caracteres.");
        }

        if (registroDTO.getCorreoElectronico() == null || registroDTO.getCorreoElectronico().isEmpty()) {
            throw new Exception("El correo electrónico es obligatorio.");
        }
        if (!validarEmail(registroDTO.getCorreoElectronico())) {
            throw new Exception("El formato del correo electrónico es inválido.");
        }
        if (registroDTO.getCorreoElectronico().length() > 100) {
            throw new Exception("El correo no puede exceder 100 caracteres.");
        }

        Usuario existente = usuarioDAO.buscarPorCorreo(registroDTO.getCorreoElectronico());
        if (existente != null) {
            throw new Exception("Ya existe un usuario registrado con ese correo.");
        }

        if (registroDTO.getContrasena() == null || registroDTO.getContrasena().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        if (registroDTO.getContrasena().length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres.");
        }
        if (registroDTO.getContrasena().length() > 255) {
            throw new Exception("La contraseña no puede exceder 255 caracteres.");
        }

        if (registroDTO.getFechaNacimiento() != null) {
            if (!validarEdadMinima(registroDTO.getFechaNacimiento())) {
                throw new Exception("Debes tener al menos 18 años para registrarte.");
            }
        }

        Usuario usuario = registroDTOAUsuario(registroDTO);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuarioDAO.insertar(usuario);
    }

    @Override
    public void actualizarUsuario(ActualizarUsuarioDTO actualizarDTO) throws Exception {
        if (actualizarDTO == null || actualizarDTO.getIdUsuario() == null) {
            throw new Exception("Debe especificar un usuario válido para actualizar.");
        }

        Usuario usuarioExistente = usuarioDAO.buscar(actualizarDTO.getIdUsuario());
        if (usuarioExistente == null) {
            throw new Exception("El usuario no existe.");
        }

        if (actualizarDTO.getNombre() != null && !actualizarDTO.getNombre().isEmpty()) {
            if (actualizarDTO.getNombre().length() < 2 || actualizarDTO.getNombre().length() > 50) {
                throw new Exception("El nombre debe tener entre 2 y 50 caracteres.");
            }
            usuarioExistente.setNombre(actualizarDTO.getNombre());
        }

        if (actualizarDTO.getApellidoPaterno() != null && !actualizarDTO.getApellidoPaterno().isEmpty()) {
            if (actualizarDTO.getApellidoPaterno().length() < 2 || actualizarDTO.getApellidoPaterno().length() > 50) {
                throw new Exception("El apellido paterno debe tener entre 2 y 50 caracteres.");
            }
            usuarioExistente.setApellidoPaterno(actualizarDTO.getApellidoPaterno());
        }

        if (actualizarDTO.getApellidoMaterno() != null && !actualizarDTO.getApellidoMaterno().isEmpty()) {
            usuarioExistente.setApellidoMaterno(actualizarDTO.getApellidoMaterno());
        }

        if (actualizarDTO.getCarrera() != null) {
            if (actualizarDTO.getCarrera().length() > 100) {
                throw new Exception("La carrera no puede exceder 100 caracteres.");
            }
            if (!actualizarDTO.getCarrera().isEmpty()) {
                usuarioExistente.setCarrera(actualizarDTO.getCarrera());
            }
        }

        if (actualizarDTO.getBiografia() != null) {
            if (actualizarDTO.getBiografia().length() > 500) {
                throw new Exception("La biografía no puede exceder 500 caracteres.");
            }
            if (!actualizarDTO.getBiografia().isEmpty()) {
                usuarioExistente.setBiografia(actualizarDTO.getBiografia());
            }
        }

        if (actualizarDTO.getUrlFotoPerfil() != null && !actualizarDTO.getUrlFotoPerfil().isEmpty()) {
            usuarioExistente.setUrlFotoPerfil(actualizarDTO.getUrlFotoPerfil());
        }

        if (actualizarDTO.getGenero() != null && !actualizarDTO.getGenero().isEmpty()) {
            try {
                Genero genero = Genero.valueOf(actualizarDTO.getGenero());
                usuarioExistente.setGenero(genero);
            } catch (IllegalArgumentException e) {
                throw new Exception("Género inválido.");
            }
        }

        if (actualizarDTO.getFechaNacimiento() != null) {
            if (!validarEdadMinima(actualizarDTO.getFechaNacimiento())) {
                throw new Exception("Debes tener al menos 18 años.");
            }
            usuarioExistente.setFechaNacimiento(actualizarDTO.getFechaNacimiento());
        }

        usuarioDAO.actualizar(usuarioExistente);
    }

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

    @Override
    public UsuarioPerfilDTO buscarPorId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        Usuario usuario = usuarioDAO.buscar(id);
        return usuarioADTO(usuario);
    }

    @Override
    public List<UsuarioPerfilDTO> listarUsuarios() {
        List<Usuario> lista = usuarioDAO.listar();
        if (lista == null || lista.isEmpty()) {
            return null;
        }
        return lista.stream()
            .map(this::usuarioADTO)
            .collect(Collectors.toList());
    }

    @Override
    public UsuarioPerfilDTO buscarPorCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
            return null;
        }
        Usuario usuario = usuarioDAO.buscarPorCorreo(correo);
        return usuarioADTO(usuario);
    }

    @Override
    public List<UsuarioPerfilDTO> buscarPorNombreCompleto(String nombre, String apellidoPaterno) {
        if (nombre == null || nombre.isEmpty() || apellidoPaterno == null || apellidoPaterno.isEmpty()) {
            return null;
        }

        List<Usuario> lista = usuarioDAO.buscarPorNombreCompleto(nombre, apellidoPaterno);
        if (lista == null || lista.isEmpty()) {
            return null;
        }

        return lista.stream()
            .map(this::usuarioADTO)
            .collect(Collectors.toList());
    }

    @Override
    public UsuarioPerfilDTO iniciarSesion(LoginDTO loginDTO) throws Exception {
        if (loginDTO == null) {
            throw new Exception("El DTO de login no puede ser nulo.");
        }
        if (loginDTO.getCorreoElectronico() == null || loginDTO.getCorreoElectronico().isEmpty()) {
            throw new Exception("El correo electrónico es obligatorio.");
        }
        if (loginDTO.getContrasena() == null || loginDTO.getContrasena().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        Usuario usuarioRegistrado = usuarioDAO.buscarPorCorreo(loginDTO.getCorreoElectronico());
        if (usuarioRegistrado == null) {
            throw new Exception("El usuario no existe.");
        }

        if (!usuarioRegistrado.getContrasena().equals(loginDTO.getContrasena())) {
            throw new Exception("Contraseña incorrecta.");
        }

        return usuarioADTO(usuarioRegistrado);
    }

    @Override
    public List<UsuarioPerfilDTO> filtrarUsuariosPorNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre de búsqueda es obligatorio.");
        }

        if (nombre.length() < 2) {
            throw new Exception("Ingresa al menos 2 caracteres para buscar.");
        }

        List<Usuario> usuarios = usuarioDAO.listar();
        if (usuarios == null || usuarios.isEmpty()) {
            return null;
        }

        List<UsuarioPerfilDTO> resultados = usuarios.stream()
            .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                        u.getApellidoPaterno().toLowerCase().contains(nombre.toLowerCase()))
            .limit(100)
            .map(this::usuarioADTO)
            .collect(Collectors.toList());

        return resultados.isEmpty() ? null : resultados;
    }

    @Override
    public List<UsuarioPerfilDTO> mostrarPretendientes(Long idUsuarioActual) throws Exception {
        if (idUsuarioActual == null || idUsuarioActual <= 0) {
            throw new Exception("ID de usuario inválido.");
        }

        Usuario usuarioActual = usuarioDAO.buscar(idUsuarioActual);
        if (usuarioActual == null) {
            throw new Exception("El usuario no existe.");
        }

        List<Usuario> todosLosUsuarios = usuarioDAO.listar();
        if (todosLosUsuarios == null || todosLosUsuarios.isEmpty()) {
            return null;
        }

        List<Bloqueo> bloqueosPorUsuario = bloqueoDAO.buscarPorBloqueador(usuarioActual);
        List<Long> idsUsuariosBloqueados = bloqueosPorUsuario.stream()
            .map(b -> b.getUsuarioBloqueado().getIdUsuario())
            .collect(Collectors.toList());

        List<Bloqueo> bloqueosRecibidos = bloqueoDAO.buscarPorBloqueado(usuarioActual);
        List<Long> idsUsuariosQueBloquean = bloqueosRecibidos.stream()
            .map(b -> b.getUsuarioBloqueador().getIdUsuario())
            .collect(Collectors.toList());

        List<Interaccion> interaccionesEnviadas = interaccionDAO.buscarPorEmisor(usuarioActual);
        List<Long> idsInteraccionados = interaccionesEnviadas.stream()
            .map(i -> i.getUsuarioReceptor().getIdUsuario())
            .collect(Collectors.toList());

        List<UsuarioPerfilDTO> pretendientes = todosLosUsuarios.stream()
            .filter(u -> !u.getIdUsuario().equals(idUsuarioActual))
            .filter(u -> !idsUsuariosBloqueados.contains(u.getIdUsuario()))
            .filter(u -> !idsUsuariosQueBloquean.contains(u.getIdUsuario()))
            .filter(u -> !idsInteraccionados.contains(u.getIdUsuario()))
            .limit(100)
            .map(this::usuarioADTO)
            .collect(Collectors.toList());

        return pretendientes.isEmpty() ? null : pretendientes;
    }

    @Override
    public void reestablecerContrasenia(Long idUsuario, String nuevaContrasenia) throws Exception {
        if (idUsuario == null || idUsuario <= 0) {
            throw new Exception("ID de usuario inválido.");
        }

        if (nuevaContrasenia == null || nuevaContrasenia.isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía.");
        }
        if (nuevaContrasenia.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres.");
        }
        if (nuevaContrasenia.length() > 255) {
            throw new Exception("La contraseña no puede exceder 255 caracteres.");
        }

        Usuario usuario = usuarioDAO.buscar(idUsuario);
        if (usuario == null) {
            throw new Exception("El usuario no existe.");
        }

        if (usuario.getContrasena().equals(nuevaContrasenia)) {
            throw new Exception("La nueva contraseña no puede ser igual a la actual.");
        }

        usuario.setContrasena(nuevaContrasenia);
        usuarioDAO.actualizar(usuario);
    }
}