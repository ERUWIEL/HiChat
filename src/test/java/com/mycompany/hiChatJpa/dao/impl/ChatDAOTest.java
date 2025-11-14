
package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Match;
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
@DisplayName("Tests de Integración - ChatDAO")
class ChatDAOTest extends BaseIntegrationTest {
    
    private Chat chatTest;
    private Usuario usuario1;
    private Usuario usuario2;
    private Match matchTest;
    
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
        matchTest = new Match.Builder()
                .usuarioA(usuario1)
                .usuarioB(usuario2)
                .fechaMatch(LocalDateTime.now())
                .build();
        em.persist(matchTest);
        
        flush();
        
        // Crear chat
        Set<Usuario> participantes = new HashSet<>();
        participantes.add(usuario1);
        participantes.add(usuario2);
        
        chatTest = new Chat.Builder()
                .nombre("Chat de prueba")
                .match(matchTest)
                .participantes(participantes)
                .build();
    }
    
    @Test
    @DisplayName("Insertar chat válido - debe persistir")
    void insertar_cuandoChatValido_debePersistir() {
        // Act
        em.persist(chatTest);
        flushAndClear();
        
        // Assert
        Chat encontrado = em.find(Chat.class, chatTest.getIdChat());
        
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNombre()).isEqualTo("Chat de prueba");
        assertThat(encontrado.getMatch().getIdMatch()).isEqualTo(matchTest.getIdMatch());
    }
    
    @Test
    @DisplayName("Actualizar chat - debe modificar")
    void actualizar_cuandoChatExiste_debeModificar() {
        // Arrange
        em.persist(chatTest);
        flushAndClear();
        
        // Act
        Chat aActualizar = em.find(Chat.class, chatTest.getIdChat());
        aActualizar.setNombre("Chat actualizado");
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Chat actualizado = em.find(Chat.class, chatTest.getIdChat());
        assertThat(actualizado.getNombre()).isEqualTo("Chat actualizado");
    }
    
    @Test
    @DisplayName("Eliminar chat - debe eliminarse")
    void eliminar_cuandoChatExiste_debeEliminar() {
        // Arrange
        em.persist(chatTest);
        flushAndClear();
        Long id = chatTest.getIdChat();
        
        // Act
        Chat aEliminar = em.find(Chat.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Chat eliminado = em.find(Chat.class, id);
        assertThat(eliminado).isNull();
    }
    
    @Test
    @DisplayName("Buscar chat por nombre - debe retornar lista")
    void buscarPorNombre_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(chatTest);
        flushAndClear();
        
        // Act
        List<Chat> chats = em.createQuery(
                "SELECT c FROM Chat c WHERE c.nombre LIKE :nombre", 
                Chat.class)
                .setParameter("nombre", "%prueba%")
                .getResultList();
        
        // Assert
        assertThat(chats).hasSize(1);
        assertThat(chats.get(0).getNombre()).contains("prueba");
    }
    
    @Test
    @DisplayName("Buscar chats por participante - debe retornar lista")
    void buscarPorParticipante_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(chatTest);
        flushAndClear();
        
        // Act
        List<Chat> chats = em.createQuery(
                "SELECT c FROM Chat c JOIN c.participantes p WHERE p.idUsuario = :usuarioId", 
                Chat.class)
                .setParameter("usuarioId", usuario1.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(chats).hasSize(1);
    }
    
    @Test
    @DisplayName("Listar todos los chats - debe retornar lista")
    void listar_debeRetornarTodosLosChats() {
        // Arrange
        em.persist(chatTest);
        flushAndClear();
        
        // Act
        List<Chat> todos = em.createQuery("SELECT c FROM Chat c", Chat.class)
                .getResultList();
        
        // Assert
        assertThat(todos).hasSize(1);
    }
}