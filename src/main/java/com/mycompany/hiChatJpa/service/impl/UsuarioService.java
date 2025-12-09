package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.config.CloudinaryUtil;
import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dto.*;
import com.mycompany.hiChatJpa.entitys.*;
import com.mycompany.hiChatJpa.exceptions.DuplicateEntityException;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.RepositoryException;
import com.mycompany.hiChatJpa.exceptions.ServiceException;
import com.mycompany.hiChatJpa.repository.impl.*;
import com.mycompany.hiChatJpa.service.IUsuarioService;
import jakarta.persistence.EntityManager;
import java.io.IOException;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService implements IUsuarioService {

    private static final int EDAD_MINIMA = 18;
    private static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@(.+)$";

    public UsuarioService() {
    }

    // HOT FIX
    
    /**
     * metodo que permite al usuario hacer el login
     *
     * @param loginDTO
     * @return
     * @throws ServiceException
     */
    @Override
    public UsuarioPerfilDTO iniciarSesion(LoginDTO loginDTO) throws ServiceException {
        EntityManager em = null;
        try {
            validarLoginDTO(loginDTO);
            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            Usuario usuario = usuarioRepo.buscarPorCorreo(loginDTO.getCorreoElectronico());

            if (usuario == null) {
                throw new EntityNotFoundException("Usuario no encontrado con ese correo");
            }
            if (!loginDTO.getContrasena().equals(usuario.getContrasena())) {
                throw new ServiceException("Credenciales inválidas");
            }

            return convertirAUsuarioPerfilDTO(usuario);
        } catch (EntityNotFoundException | ServiceException e) {
            throw e;
        } catch (RepositoryException e) {
            throw new ServiceException("iniciarSesion", "Error al iniciar sesión", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    /**
     * metodo que permite registrar a un nuevo usuario
     * @param registroDTO
     * @return
     * @throws ServiceException 
     */
    @Override
    public boolean registrarUsuario(RegistroDTO registroDTO) throws ServiceException {
        EntityManager em = null;
        try {
            validarRegistroDTO(registroDTO);
            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            Usuario usuarioExistente = usuarioRepo.buscarPorCorreo(registroDTO.getCorreoElectronico());
            if (usuarioExistente != null) {
                throw new DuplicateEntityException("este correo electronico ya se encuentra registrado");
            }

            // registro sin foto de perfil
            Usuario nuevoUsuario = new Usuario.Builder()
                    .nombre(registroDTO.getNombre())
                    .apellidoPaterno(registroDTO.getApellidoPaterno())
                    .apellidoMaterno(registroDTO.getApellidoMaterno())
                    .correoElectronico(registroDTO.getCorreoElectronico())
                    .contrasena(registroDTO.getContrasena())
                    .fechaNacimiento(registroDTO.getFechaNacimiento())
                    .biografia(registroDTO.getBiografia())
                    .carrera(registroDTO.getCarrera())
                    .build();

            JpaUtil.beginTransaction();
            usuarioRepo.insertar(nuevoUsuario);
            JpaUtil.commitTransaction();

            // seteo dela imagen en cloudinary
            if (registroDTO.getUrlFotoPerfil() != null && !registroDTO.getUrlFotoPerfil().isEmpty()) {
                try {
                    CloudinaryUtil cloudinary = CloudinaryUtil.getInstance();
                    String urlFoto = cloudinary.subirFotoPerfil(registroDTO.getUrlFotoPerfil(), nuevoUsuario.getIdUsuario());

                    // actualizacion del usuario con su imagen
                    JpaUtil.beginTransaction();
                    nuevoUsuario.setUrlFotoPerfil(urlFoto);
                    usuarioRepo.actualizar(nuevoUsuario);
                    JpaUtil.commitTransaction();
                } catch (IOException e) {
                    throw e;
                }
            }

            return true;
        } catch (Exception ex) {
            throw new ServiceException("registrarUsuario", "no fue posible registrar al usuario", ex);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public boolean reestablecerContrasenia(Long idUsuario, String nuevaContrasenia) throws ServiceException {
        EntityManager em = null;
        try {
            if (idUsuario == null || nuevaContrasenia == null || nuevaContrasenia.trim().isEmpty()) {
                throw new ServiceException("Los parámetros no pueden ser nulos o vacíos");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            Usuario usuario = usuarioRepo.buscar(idUsuario);
            if (usuario == null) {
                throw new EntityNotFoundException("el usuario especificado no existe");
            }

            JpaUtil.beginTransaction();
            usuario.setContrasena(nuevaContrasenia);
            usuarioRepo.actualizar(usuario);
            JpaUtil.commitTransaction();

            return true;
        } catch (EntityNotFoundException | ServiceException e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw e;
        } catch (Exception e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw new ServiceException("reestablecerContrasenia", "error al reestablecer la contrasenia", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    
    
    
    
    
    
    @Override
    public boolean registrarInteraccion(Long idEmisor, Long idReceptor, TipoInteraccion tipo) throws ServiceException {
        EntityManager em = null;
        try {
            // Validaciones
            if (idEmisor == null || idReceptor == null || tipo == null) {
                throw new ServiceException("Los parámetros no pueden ser nulos");
            }

            if (idEmisor.equals(idReceptor)) {
                throw new ServiceException("No puedes interactuar contigo mismo");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            InteraccionRepository interaccionRepo = new InteraccionRepository(em);
            BloqueoRepository bloqueoRepo = new BloqueoRepository(em);
            MatchRepository matchRepo = new MatchRepository(em);

            // Verificar que ambos usuarios existan
            Usuario emisor = usuarioRepo.buscar(idEmisor);
            Usuario receptor = usuarioRepo.buscar(idReceptor);

            if (emisor == null || receptor == null) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }

            // Verificar que no haya bloqueo entre ellos
            List<Bloqueo> bloqueos = bloqueoRepo.buscarPorBloqueador(emisor, 100, 0);
            boolean estaBloqueado = bloqueos.stream()
                    .anyMatch(b -> b.getUsuarioBloqueado().getIdUsuario().equals(idReceptor));

            if (estaBloqueado) {
                throw new ServiceException("No puedes interactuar con un usuario bloqueado");
            }

            JpaUtil.beginTransaction();

            // Crear la interacción
            Interaccion nuevaInteraccion = new Interaccion.Builder()
                    .usuarioEmisor(emisor)
                    .usuarioReceptor(receptor)
                    .tipo(tipo)
                    .build();

            interaccionRepo.insertar(nuevaInteraccion);

            // Si es ME_GUSTA, verificar si hay match
            if (tipo == TipoInteraccion.ME_GUSTA) {
                // Buscar si el receptor ya le dio like al emisor
                List<Interaccion> interaccionesReceptor = interaccionRepo.buscarPorEmisor(receptor, 1000, 0);

                boolean hayMatchMutuo = interaccionesReceptor.stream()
                        .anyMatch(i -> i.getUsuarioReceptor().getIdUsuario().equals(idEmisor)
                        && i.getTipo() == TipoInteraccion.ME_GUSTA);

                if (hayMatchMutuo) {
                    // Crear el match
                    Match nuevoMatch = new Match.Builder()
                            .usuarioA(emisor)
                            .usuarioB(receptor)
                            .build();

                    matchRepo.insertar(nuevoMatch);

                    // Crear el chat automáticamente
                    ChatRepository chatRepo = new ChatRepository(em);
                    Chat nuevoChat = new Chat.Builder()
                            .nombre(emisor.getNombre() + " & " + receptor.getNombre())
                            .match(nuevoMatch)
                            .build();

                    chatRepo.insertar(nuevoChat);
                }
            }

            JpaUtil.commitTransaction();
            return true;

        } catch (EntityNotFoundException | ServiceException e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw e;
        } catch (Exception e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw new ServiceException("registrarInteraccion", "Error al registrar interacción", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public boolean bloquearUsuario(Long idEmisor, Long idReceptor) throws ServiceException {
        EntityManager em = null;
        try {
            if (idEmisor == null || idReceptor == null) {
                throw new ServiceException("Los IDs no pueden ser nulos");
            }

            if (idEmisor.equals(idReceptor)) {
                throw new ServiceException("No puedes bloquearte a ti mismo");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            BloqueoRepository bloqueoRepo = new BloqueoRepository(em);

            Usuario emisor = usuarioRepo.buscar(idEmisor);
            Usuario receptor = usuarioRepo.buscar(idReceptor);

            if (emisor == null || receptor == null) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }

            // Verificar si ya existe el bloqueo
            List<Bloqueo> bloqueosExistentes = bloqueoRepo.buscarPorBloqueador(emisor, 1000, 0);
            boolean yaEstaBloqueado = bloqueosExistentes.stream()
                    .anyMatch(b -> b.getUsuarioBloqueado().getIdUsuario().equals(idReceptor));

            if (yaEstaBloqueado) {
                throw new ServiceException("El usuario ya está bloqueado");
            }

            JpaUtil.beginTransaction();

            Bloqueo nuevoBloqueo = new Bloqueo.Builder()
                    .usuarioBloqueador(emisor)
                    .usuarioBloqueado(receptor)
                    .build();

            bloqueoRepo.insertar(nuevoBloqueo);
            JpaUtil.commitTransaction();

            return true;

        } catch (EntityNotFoundException | ServiceException e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw e;
        } catch (Exception e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw new ServiceException("bloquearUsuario", "Error al bloquear usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public boolean actualizarUsuario(ActualizarUsuarioDTO actualizarDTO) throws ServiceException {
        EntityManager em = null;
        try {
            validarActualizarUsuarioDTO(actualizarDTO);

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            Usuario usuario = usuarioRepo.buscar(actualizarDTO.getIdUsuario());
            if (usuario == null) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }

            JpaUtil.beginTransaction();

            // Actualizar solo los campos que vienen en el DTO
            if (actualizarDTO.getNombre() != null) {
                usuario.setNombre(actualizarDTO.getNombre());
            }
            if (actualizarDTO.getApellidoPaterno() != null) {
                usuario.setApellidoPaterno(actualizarDTO.getApellidoPaterno());
            }
            if (actualizarDTO.getApellidoMaterno() != null) {
                usuario.setApellidoMaterno(actualizarDTO.getApellidoMaterno());
            }
            if (actualizarDTO.getCarrera() != null) {
                usuario.setCarrera(actualizarDTO.getCarrera());
            }
            if (actualizarDTO.getBiografia() != null) {
                usuario.setBiografia(actualizarDTO.getBiografia());
            }
            if (actualizarDTO.getFechaNacimiento() != null) {
                usuario.setFechaNacimiento(actualizarDTO.getFechaNacimiento());
            }

            usuarioRepo.actualizar(usuario);
            JpaUtil.commitTransaction();

            return true;

        } catch (EntityNotFoundException | ServiceException e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw e;
        } catch (Exception e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw new ServiceException("actualizarUsuario", "Error al actualizar usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public boolean eliminarUsuario(Long id) throws ServiceException {
        EntityManager em = null;
        try {
            if (id == null) {
                throw new ServiceException("El ID no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            Usuario usuario = usuarioRepo.buscar(id);
            if (usuario == null) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }

            JpaUtil.beginTransaction();
            usuarioRepo.eliminar(id);
            JpaUtil.commitTransaction();

            return true;

        } catch (EntityNotFoundException | ServiceException e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw e;
        } catch (Exception e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw new ServiceException("eliminarUsuario", "Error al eliminar usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public UsuarioPerfilDTO buscarPorId(Long id) throws ServiceException {
        EntityManager em = null;
        try {
            if (id == null) {
                throw new ServiceException("El ID no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            Usuario usuario = usuarioRepo.buscar(id);
            if (usuario == null) {
                return null;
            }

            return convertirAUsuarioPerfilDTO(usuario);

        } catch (Exception e) {
            throw new ServiceException("buscarPorId", "Error al buscar usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public UsuarioPerfilDTO buscarPorCorreo(String correo) throws ServiceException {
        EntityManager em = null;
        try {
            if (correo == null || correo.trim().isEmpty()) {
                throw new ServiceException("El correo no puede ser nulo o vacío");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            Usuario usuario = usuarioRepo.buscarPorCorreo(correo);
            if (usuario == null) {
                return null;
            }

            return convertirAUsuarioPerfilDTO(usuario);

        } catch (Exception e) {
            throw new ServiceException("buscarPorCorreo", "Error al buscar usuario por correo", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public List<UsuarioPerfilDTO> listarUsuarios() throws ServiceException {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            List<Usuario> usuarios = usuarioRepo.listar(1000, 0);

            return usuarios.stream()
                    .map(this::convertirAUsuarioPerfilDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ServiceException("listarUsuarios", "Error al listar usuarios", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public List<UsuarioPerfilDTO> buscarPorNombreCompleto(String nombre, String apellidoPaterno) throws ServiceException {
        EntityManager em = null;
        try {
            if (nombre == null || apellidoPaterno == null) {
                throw new ServiceException("Los parámetros no pueden ser nulos");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            List<Usuario> usuarios = usuarioRepo.buscarPorNombreCompleto(nombre, apellidoPaterno, 100, 0);

            return usuarios.stream()
                    .map(this::convertirAUsuarioPerfilDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ServiceException("buscarPorNombreCompleto", "Error al buscar por nombre", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public List<UsuarioPerfilDTO> filtrarUsuariosPorNombre(String nombre) throws ServiceException {
        EntityManager em = null;
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ServiceException("El nombre no puede ser nulo o vacío");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            // Buscar por nombre (esto podría mejorarse con una query LIKE en el repository)
            List<Usuario> todosUsuarios = usuarioRepo.listar(1000, 0);

            List<Usuario> usuariosFiltrados = todosUsuarios.stream()
                    .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());

            return usuariosFiltrados.stream()
                    .map(this::convertirAUsuarioPerfilDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ServiceException("filtrarUsuariosPorNombre", "Error al filtrar usuarios", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public List<UsuarioPerfilDTO> mostrarPretendientes(Long idUsuarioActual) throws ServiceException {
        EntityManager em = null;
        try {
            if (idUsuarioActual == null) {
                throw new ServiceException("El ID del usuario no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            InteraccionRepository interaccionRepo = new InteraccionRepository(em);

            Usuario usuarioActual = usuarioRepo.buscar(idUsuarioActual);
            if (usuarioActual == null) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }

            // Obtener las interacciones recibidas de tipo ME_GUSTA
            List<Interaccion> interaccionesRecibidas = interaccionRepo.buscarPorReceptor(usuarioActual, 1000, 0);

            List<UsuarioPerfilDTO> pretendientes = interaccionesRecibidas.stream()
                    .filter(i -> i.getTipo() == TipoInteraccion.ME_GUSTA)
                    .map(i -> convertirAUsuarioPerfilDTO(i.getUsuarioEmisor()))
                    .collect(Collectors.toList());

            return pretendientes;

        } catch (Exception e) {
            throw new ServiceException("mostrarPretendientes", "Error al obtener pretendientes", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public List<MatchDTO> mostrarMatches(Long idUsuario) throws ServiceException {
        EntityManager em = null;
        try {
            if (idUsuario == null) {
                throw new ServiceException("El ID del usuario no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            MatchRepository matchRepo = new MatchRepository(em);

            Usuario usuario = usuarioRepo.buscar(idUsuario);
            if (usuario == null) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }

            // Buscar matches donde el usuario es usuarioA o usuarioB
            List<Match> matchesA = matchRepo.buscarPorUsuarioA(usuario, 1000, 0);
            List<Match> matchesB = matchRepo.buscarPorUsuarioB(usuario, 1000, 0);

            List<MatchDTO> resultado = new ArrayList<>();

            // Convertir matchesA
            for (Match match : matchesA) {
                MatchDTO dto = new MatchDTO();
                dto.setIdMatch(match.getIdMatch());
                dto.setUsuarioA(convertirAUsuarioPerfilDTO(match.getUsuarioA()));
                dto.setUsuarioB(convertirAUsuarioPerfilDTO(match.getUsuarioB()));
                dto.setFechaMatch(match.getFechaMatch());
                dto.setIdChat(match.getChat() != null ? match.getChat().getIdChat() : null);
                resultado.add(dto);
            }

            // Convertir matchesB
            for (Match match : matchesB) {
                MatchDTO dto = new MatchDTO();
                dto.setIdMatch(match.getIdMatch());
                dto.setUsuarioA(convertirAUsuarioPerfilDTO(match.getUsuarioA()));
                dto.setUsuarioB(convertirAUsuarioPerfilDTO(match.getUsuarioB()));
                dto.setFechaMatch(match.getFechaMatch());
                dto.setIdChat(match.getChat() != null ? match.getChat().getIdChat() : null);
                resultado.add(dto);
            }

            return resultado;

        } catch (Exception e) {
            throw new ServiceException("mostrarMatches", "Error al obtener matches", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }


    private void validarLoginDTO(LoginDTO dto) throws ServiceException {
        if (dto == null) {
            throw new ServiceException("LoginDTO no puede ser nulo");
        }
        if (dto.getCorreoElectronico() == null || dto.getCorreoElectronico().trim().isEmpty()) {
            throw new ServiceException("El correo electrónico es obligatorio");
        }
        if (dto.getContrasena() == null || dto.getContrasena().trim().isEmpty()) {
            throw new ServiceException("La contraseña es obligatoria");
        }
    }

    private void validarRegistroDTO(RegistroDTO dto) throws ServiceException {
        if (dto == null) {
            throw new ServiceException("RegistroDTO no puede ser nulo");
        }

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new ServiceException("El nombre es obligatorio");
        }
        if (dto.getApellidoPaterno() == null || dto.getApellidoPaterno().trim().isEmpty()) {
            throw new ServiceException("El apellido paterno es obligatorio");
        }
        if (dto.getCorreoElectronico() == null || dto.getCorreoElectronico().trim().isEmpty()) {
            throw new ServiceException("El correo electrónico es obligatorio");
        }
        if (!dto.getCorreoElectronico().matches(REGEX_EMAIL)) {
            throw new ServiceException("El formato del correo electrónico es inválido");
        }
        if (dto.getContrasena() == null || dto.getContrasena().trim().isEmpty()) {
            throw new ServiceException("La contrasenia no puede ser vacia");
        }
        if (dto.getFechaNacimiento() == null) {
            throw new ServiceException("La fecha de nacimiento es obligatoria");
        }

        // Validar edad mínima
        int edad = calcularEdad(dto.getFechaNacimiento());
        if (edad < EDAD_MINIMA) {
            throw new ServiceException("Debes tener al menos " + EDAD_MINIMA + " años para registrarte");
        }
    }

    private void validarActualizarUsuarioDTO(ActualizarUsuarioDTO dto) throws ServiceException {
        if (dto == null || dto.getIdUsuario() == null) {
            throw new ServiceException("ActualizarUsuarioDTO o ID de usuario no pueden ser nulos");
        }
    }

    private UsuarioPerfilDTO convertirAUsuarioPerfilDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        dto.setCorreoElectronico(usuario.getCorreoElectronico());
        dto.setCarrera(usuario.getCarrera());
        dto.setBiografia(usuario.getBiografia());
        dto.setUrlFotoPerfil(usuario.getUrlFotoPerfil());
        dto.setGenero(usuario.getGenero() != null ? usuario.getGenero().name() : null);
        dto.setFechaNacimiento(usuario.getFechaNacimiento());

        if (usuario.getFechaNacimiento() != null) {
            dto.setEdad(calcularEdad(usuario.getFechaNacimiento()));
        }

        return dto;
    }

    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
