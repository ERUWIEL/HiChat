package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dto.ChatConMensajesDTO;
import com.mycompany.hiChatJpa.dto.MensajeDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.exceptions.EntityNotFoundException;
import com.mycompany.hiChatJpa.exceptions.ServiceException;
import com.mycompany.hiChatJpa.repository.impl.ChatRepository;
import com.mycompany.hiChatJpa.repository.impl.MensajeRepository;
import com.mycompany.hiChatJpa.repository.impl.UsuarioRepository;
import com.mycompany.hiChatJpa.service.IChatService;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la capa de servicio para Chat
 *
 * @author gatog
 */
public class ChatService implements IChatService {

    public ChatService() {
    }

    @Override
    public boolean cambiarAliasDelChat(Long chatId, String nuevoNombre) throws ServiceException {
        EntityManager em = null;
        try {
            if (chatId == null || nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                throw new ServiceException("Los parámetros no pueden ser nulos o vacíos");
            }

            em = JpaUtil.getEntityManager();
            ChatRepository chatRepo = new ChatRepository(em);

            Chat chat = chatRepo.buscar(chatId);
            if (chat == null) {
                throw new EntityNotFoundException("Chat no encontrado");
            }

            JpaUtil.beginTransaction();
            chat.setNombre(nuevoNombre);
            chatRepo.actualizar(chat);
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
            throw new ServiceException("cambiarAliasDelChat", "Error al cambiar alias del chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public boolean enviarMensaje(Long idChat, Long idUsuarioEmisor, String contenidoMensaje) throws ServiceException {
        EntityManager em = null;
        try {
            if (idChat == null || idUsuarioEmisor == null || contenidoMensaje == null || contenidoMensaje.trim().isEmpty()) {
                throw new ServiceException("Los parámetros no pueden ser nulos o vacíos");
            }

            em = JpaUtil.getEntityManager();
            ChatRepository chatRepo = new ChatRepository(em);
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            MensajeRepository mensajeRepo = new MensajeRepository(em);

            Chat chat = chatRepo.buscar(idChat);
            if (chat == null) {
                throw new EntityNotFoundException("Chat no encontrado");
            }

            Usuario emisor = usuarioRepo.buscar(idUsuarioEmisor);
            if (emisor == null) {
                throw new EntityNotFoundException("Usuario emisor no encontrado");
            }

            // Verificar que el usuario es participante del chat
            boolean esParticipante = chat.getParticipantes().stream()
                    .anyMatch(u -> u.getIdUsuario().equals(idUsuarioEmisor));

            if (!esParticipante) {
                throw new ServiceException("El usuario no es participante del chat");
            }

            JpaUtil.beginTransaction();

            Mensaje nuevoMensaje = new Mensaje.Builder()
                    .chat(chat)
                    .usuarioEmisor(emisor)
                    .contenido(contenidoMensaje)
                    .fechaEnvio(LocalDateTime.now())
                    .estaVisto(false)
                    .estaBorrado(false)
                    .build();

            mensajeRepo.insertar(nuevoMensaje);
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
            throw new ServiceException("enviarMensaje", "Error al enviar mensaje", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public void marcarMensajeComoVisto(Long idMensaje) throws ServiceException {
        EntityManager em = null;
        try {
            if (idMensaje == null) {
                throw new ServiceException("El ID del mensaje no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            MensajeRepository mensajeRepo = new MensajeRepository(em);

            Mensaje mensaje = mensajeRepo.buscar(idMensaje);
            if (mensaje == null) {
                throw new EntityNotFoundException("Mensaje no encontrado");
            }

            if (!mensaje.getEstaVisto()) {
                JpaUtil.beginTransaction();
                mensaje.setEstaVisto(true);
                mensajeRepo.actualizar(mensaje);
                JpaUtil.commitTransaction();
            }

        } catch (EntityNotFoundException | ServiceException e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw e;
        } catch (Exception e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw new ServiceException("marcarMensajeComoVisto", "Error al marcar mensaje como visto", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public void marcarMensajesDelChatComoVistos(Long idChat, Long idUsuarioReceptor) throws ServiceException {
        EntityManager em = null;
        try {
            if (idChat == null || idUsuarioReceptor == null) {
                throw new ServiceException("Los parámetros no pueden ser nulos");
            }

            em = JpaUtil.getEntityManager();
            ChatRepository chatRepo = new ChatRepository(em);
            MensajeRepository mensajeRepo = new MensajeRepository(em);

            Chat chat = chatRepo.buscar(idChat);
            if (chat == null) {
                throw new EntityNotFoundException("Chat no encontrado");
            }

            List<Mensaje> mensajes = mensajeRepo.buscarPorChat(chat, 1000, 0);

            // Filtrar mensajes no vistos que no fueron enviados por el receptor
            List<Mensaje> mensajesParaMarcar = mensajes.stream()
                    .filter(m -> !m.getEstaVisto()
                    && !m.getUsuarioEmisor().getIdUsuario().equals(idUsuarioReceptor))
                    .collect(Collectors.toList());

            if (!mensajesParaMarcar.isEmpty()) {
                JpaUtil.beginTransaction();
                for (Mensaje mensaje : mensajesParaMarcar) {
                    mensaje.setEstaVisto(true);
                    mensajeRepo.actualizar(mensaje);
                }
                JpaUtil.commitTransaction();
            }

        } catch (EntityNotFoundException | ServiceException e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw e;
        } catch (Exception e) {
            if (em != null) {
                JpaUtil.rollbackTransaction();
            }
            throw new ServiceException("marcarMensajesDelChatComoVistos",
                    "Error al marcar mensajes como vistos", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public boolean eliminarMensaje(Long idMensaje) throws ServiceException {
        EntityManager em = null;
        try {
            if (idMensaje == null) {
                throw new ServiceException("El ID del mensaje no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            MensajeRepository mensajeRepo = new MensajeRepository(em);

            Mensaje mensaje = mensajeRepo.buscar(idMensaje);
            if (mensaje == null) {
                throw new EntityNotFoundException("Mensaje no encontrado");
            }

            JpaUtil.beginTransaction();
            mensaje.setEstaBorrado(true);
            mensajeRepo.actualizar(mensaje);
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
            throw new ServiceException("eliminarMensaje", "Error al eliminar mensaje", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public List<ChatConMensajesDTO> cargarChatsDelUsuario(Long idUsuario) throws ServiceException {
        EntityManager em = null;
        try {
            if (idUsuario == null) {
                throw new ServiceException("El ID del usuario no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            ChatRepository chatRepo = new ChatRepository(em);
            MensajeRepository mensajeRepo = new MensajeRepository(em);

            Usuario usuario = usuarioRepo.buscar(idUsuario);
            if (usuario == null) {
                throw new EntityNotFoundException("Usuario no encontrado");
            }

            List<Chat> chats = chatRepo.buscarPorParticipante(usuario, 100, 0);
            List<ChatConMensajesDTO> resultado = new ArrayList<>();

            for (Chat chat : chats) {
                ChatConMensajesDTO dto = new ChatConMensajesDTO();
                dto.setIdChat(chat.getIdChat());
                dto.setNombre(chat.getNombre());
                dto.setIdMatch(chat.getMatch() != null ? chat.getMatch().getIdMatch() : null);

                // Obtener participantes
                List<UsuarioPerfilDTO> participantes = chat.getParticipantes().stream()
                        .map(this::convertirAUsuarioPerfilDTO)
                        .collect(Collectors.toList());
                dto.setParticipantes(participantes);

                // Obtener mensajes del chat
                List<Mensaje> mensajes = mensajeRepo.buscarPorChat(chat, 1, 0);
                dto.setTotalMensajes(mensajes.size());

                // Obtener último mensaje
                if (!mensajes.isEmpty()) {
                    Mensaje ultimoMensaje = mensajes.get(0);
                    dto.setUltimoMensaje(ultimoMensaje.getContenido());
                    dto.setFechaUltimoMensaje(ultimoMensaje.getFechaEnvio());
                }

                // Verificar si hay mensajes no leídos
                boolean hayNoLeidos = mensajes.stream()
                        .anyMatch(m -> !m.getEstaVisto()
                        && !m.getUsuarioEmisor().getIdUsuario().equals(idUsuario));
                dto.setHayNoLeidos(hayNoLeidos);

                resultado.add(dto);
            }

            // Ordenar por fecha del último mensaje (más reciente primero)
            resultado.sort((c1, c2) -> {
                if (c1.getFechaUltimoMensaje() == null) {
                    return 1;
                }
                if (c2.getFechaUltimoMensaje() == null) {
                    return -1;
                }
                return c2.getFechaUltimoMensaje().compareTo(c1.getFechaUltimoMensaje());
            });

            return resultado;

        } catch (EntityNotFoundException | ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("cargarChatsDelUsuario",
                    "Error al cargar chats del usuario", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public List<MensajeDTO> obtenerMensajesDelChat(Long idChat, int limit, int offset) throws ServiceException {
        EntityManager em = null;
        try {
            if (idChat == null) {
                throw new ServiceException("El ID del chat no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            ChatRepository chatRepo = new ChatRepository(em);
            MensajeRepository mensajeRepo = new MensajeRepository(em);

            Chat chat = chatRepo.buscar(idChat);
            if (chat == null) {
                throw new EntityNotFoundException("Chat no encontrado");
            }

            List<Mensaje> mensajes = mensajeRepo.buscarPorChat(chat, limit, offset);

            return mensajes.stream()
                    .filter(m -> !m.getEstaBorrado())
                    .map(this::convertirAMensajeDTO)
                    .sorted(Comparator.comparing(MensajeDTO::getFechaEnvio))
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException | ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("obtenerMensajesDelChat",
                    "Error al obtener mensajes del chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    @Override
    public ChatConMensajesDTO obtenerDetallesChat(Long idChat) throws ServiceException {
        EntityManager em = null;
        try {
            if (idChat == null) {
                throw new ServiceException("El ID del chat no puede ser nulo");
            }

            em = JpaUtil.getEntityManager();
            ChatRepository chatRepo = new ChatRepository(em);
            MensajeRepository mensajeRepo = new MensajeRepository(em);

            Chat chat = chatRepo.buscar(idChat);
            if (chat == null) {
                throw new EntityNotFoundException("Chat no encontrado");
            }

            ChatConMensajesDTO dto = new ChatConMensajesDTO();
            dto.setIdChat(chat.getIdChat());
            dto.setNombre(chat.getNombre());
            dto.setIdMatch(chat.getMatch() != null ? chat.getMatch().getIdMatch() : null);

            List<UsuarioPerfilDTO> participantes = chat.getParticipantes().stream()
                    .map(this::convertirAUsuarioPerfilDTO)
                    .collect(Collectors.toList());
            dto.setParticipantes(participantes);

            List<Mensaje> mensajes = mensajeRepo.buscarPorChat(chat, 1000, 0);
            dto.setTotalMensajes(mensajes.size());

            if (!mensajes.isEmpty()) {
                Mensaje ultimoMensaje = mensajes.get(0);
                dto.setUltimoMensaje(ultimoMensaje.getContenido());
                dto.setFechaUltimoMensaje(ultimoMensaje.getFechaEnvio());
            }

            boolean hayNoLeidos = mensajes.stream()
                    .anyMatch(m -> !m.getEstaVisto());
            dto.setHayNoLeidos(hayNoLeidos);

            return dto;

        } catch (EntityNotFoundException | ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("obtenerDetallesChat",
                    "Error al obtener detalles del chat", e);
        } finally {
            if (em != null) {
                JpaUtil.closeEntityManager();
            }
        }
    }

    // Métodos auxiliares de conversión
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

        return dto;
    }

    private MensajeDTO convertirAMensajeDTO(Mensaje mensaje) {
        if (mensaje == null) {
            return null;
        }

        MensajeDTO dto = new MensajeDTO();
        dto.setIdMensaje(mensaje.getIdMensaje());
        dto.setIdChat(mensaje.getChat().getIdChat());
        dto.setEmisor(convertirAUsuarioPerfilDTO(mensaje.getUsuarioEmisor()));
        dto.setContenido(mensaje.getContenido());
        dto.setFechaEnvio(mensaje.getFechaEnvio());
        dto.setEstaVisto(mensaje.getEstaVisto());
        dto.setEstaBorrado(mensaje.getEstaBorrado());

        return dto;
    }
}
