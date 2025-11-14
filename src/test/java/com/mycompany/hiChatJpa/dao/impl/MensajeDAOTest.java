package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * 
 * @author gatog
 */
@DisplayName("Tests de Integración - MensajeDAO")
class MensajeDAOTest extends BaseIntegrationTest {
    
    private Mensaje mensajeTest;
    private Chat chatTest;
    private Usuario usuario1;
    private Usuario usuario2;
    
    @BeforeEach
    void setUpTest() {
        // Crear usuarios
        usuario1 = new Usuario.Builder()
                .nombre("Juan")
                .apellidoPaterno("Pérez")
                .correoElectronico("juan@email.com")
                .contrasena("password123")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        usuario2 = new Usuario.Builder()
                .nombre("María")
                .apellidoPaterno("López")
                .correoElectronico("maria@email.com")
                .contrasena("password456")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        em.persist(usuario1);
        em.persist(usuario2);
        
        // Crear match
        Match match = new Match.Builder()
                .usuarioA(usuario1)
                .usuarioB(usuario2)
                .fechaMatch(LocalDateTime.now())
                .build();
        em.persist(match);
        
        // Crear chat
        Set<Usuario> participantes = new HashSet<>();
        participantes.add(usuario1);
        participantes.add(usuario2);
        
        chatTest = new Chat.Builder()
                .nombre("Chat test")
                .match(match)
                .participantes(participantes)
                .build();
        em.persist(chatTest);
        
        flush();
        
        // Crear mensaje
        mensajeTest = new Mensaje.Builder()
                .chat(chatTest)
                .usuarioEmisor(usuario1)
                .contenido("Hola, ¿cómo estás?")
                .fechaEnvio(LocalDateTime.now())
                .estaVisto(false)
                .estaBorrado(false)
                .build();
    }
    
    @Test
    @DisplayName("Insertar mensaje válido - debe persistir")
    void insertar_cuandoMensajeValido_debePersistir() {
        // Act
        em.persist(mensajeTest);
        flushAndClear();
        
        // Assert
        Mensaje encontrado = em.find(Mensaje.class, mensajeTest.getIdMensaje());
        
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getContenido()).isEqualTo("Hola, ¿cómo estás?");
        assertThat(encontrado.getChat().getIdChat()).isEqualTo(chatTest.getIdChat());
        assertThat(encontrado.getUsuarioEmisor().getIdUsuario())
                .isEqualTo(usuario1.getIdUsuario());
        assertThat(encontrado.getEstaVisto()).isFalse();
    }
    
    @Test
    @DisplayName("Actualizar mensaje - debe modificar")
    void actualizar_cuandoMensajeExiste_debeModificar() {
        // Arrange
        em.persist(mensajeTest);
        flushAndClear();
        
        // Act
        Mensaje aActualizar = em.find(Mensaje.class, mensajeTest.getIdMensaje());
        aActualizar.setEstaVisto(true);
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Mensaje actualizado = em.find(Mensaje.class, mensajeTest.getIdMensaje());
        assertThat(actualizado.getEstaVisto()).isTrue();
    }
    
    @Test
    @DisplayName("Eliminar mensaje - debe eliminarse")
    void eliminar_cuandoMensajeExiste_debeEliminar() {
        // Arrange
        em.persist(mensajeTest);
        flushAndClear();
        Long id = mensajeTest.getIdMensaje();
        
        // Act
        Mensaje aEliminar = em.find(Mensaje.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Mensaje eliminado = em.find(Mensaje.class, id);
        assertThat(eliminado).isNull();
    }
    
    @Test
    @DisplayName("Buscar mensajes por chat - debe retornar lista")
    void buscarPorChat_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(mensajeTest);
        
        Mensaje mensaje2 = new Mensaje.Builder()
                .chat(chatTest)
                .usuarioEmisor(usuario2)
                .contenido("Hola, bien ¿y tú?")
                .fechaEnvio(LocalDateTime.now())
                .estaVisto(false)
                .estaBorrado(false)
                .build();
        em.persist(mensaje2);
        
        flushAndClear();
        
        // Act
        List<Mensaje> mensajes = em.createQuery(
                "SELECT m FROM Mensaje m WHERE m.chat.idChat = :chatId", 
                Mensaje.class)
                .setParameter("chatId", chatTest.getIdChat())
                .getResultList();
        
        // Assert
        assertThat(mensajes).hasSize(2);
        assertThat(mensajes)
                .allMatch(m -> m.getChat().getIdChat().equals(chatTest.getIdChat()));
    }
    
    @Test
    @DisplayName("Buscar mensajes no vistos por usuario - debe retornar lista")
    void buscarNoVistosPorUsuario_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(mensajeTest);
        
        Mensaje mensajeVisto = new Mensaje.Builder()
                .chat(chatTest)
                .usuarioEmisor(usuario1)
                .contenido("Este mensaje está visto")
                .fechaEnvio(LocalDateTime.now())
                .estaVisto(true)
                .estaBorrado(false)
                .build();
        em.persist(mensajeVisto);
        
        flushAndClear();
        
        // Act - Buscar mensajes no vistos en chats donde participa usuario2
        List<Mensaje> mensajesNoVistos = em.createQuery(
                "SELECT m FROM Mensaje m " +
                "WHERE m.chat IN (SELECT c FROM Chat c JOIN c.participantes p WHERE p.idUsuario = :usuarioId) " +
                "AND m.usuarioEmisor.idUsuario <> :usuarioId " +
                "AND m.estaVisto = false", 
                Mensaje.class)
                .setParameter("usuarioId", usuario2.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(mensajesNoVistos).hasSize(1);
        assertThat(mensajesNoVistos.get(0).getEstaVisto()).isFalse();
    }
    
    @Test
    @DisplayName("Listar todos los mensajes - debe retornar lista")
    void listar_debeRetornarTodosLosMensajes() {
        // Arrange
        em.persist(mensajeTest);
        flushAndClear();
        
        // Act
        List<Mensaje> todos = em.createQuery("SELECT m FROM Mensaje m", Mensaje.class)
                .getResultList();
        
        // Assert
        assertThat(todos).hasSize(1);
    }
}
