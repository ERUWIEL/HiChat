package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 
 * @author gatog
 */
@DisplayName("Tests de Integración - MatchDAO")
class MatchDAOTest extends BaseIntegrationTest {
    
    private Match matchTest;
    private Usuario usuarioA;
    private Usuario usuarioB;
    
    @BeforeEach
    void setUpTest() {
        // Crear usuarios
        usuarioA = new Usuario.Builder()
                .nombre("Juan")
                .apellidoPaterno("Pérez")
                .correoElectronico("juan@email.com")
                .contrasena("password123")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        usuarioB = new Usuario.Builder()
                .nombre("María")
                .apellidoPaterno("López")
                .correoElectronico("maria@email.com")
                .contrasena("password456")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        em.persist(usuarioA);
        em.persist(usuarioB);
        flush();
        
        // Crear match
        matchTest = new Match.Builder()
                .usuarioA(usuarioA)
                .usuarioB(usuarioB)
                .fechaMatch(LocalDateTime.now())
                .build();
    }
    
    @Test
    @DisplayName("Insertar match válido - debe persistir")
    void insertar_cuandoMatchValido_debePersistir() {
        // Act
        em.persist(matchTest);
        flushAndClear();
        
        // Assert
        Match encontrado = em.find(Match.class, matchTest.getIdMatch());
        
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUsuarioA().getIdUsuario()).isEqualTo(usuarioA.getIdUsuario());
        assertThat(encontrado.getUsuarioB().getIdUsuario()).isEqualTo(usuarioB.getIdUsuario());
    }
    
    @Test
    @DisplayName("Actualizar match - debe modificar")
    void actualizar_cuandoMatchExiste_debeModificar() {
        // Arrange
        em.persist(matchTest);
        flushAndClear();
        
        // Act
        Match aActualizar = em.find(Match.class, matchTest.getIdMatch());
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        aActualizar.setFechaMatch(nuevaFecha);
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Match actualizado = em.find(Match.class, matchTest.getIdMatch());
        assertThat(actualizado.getFechaMatch()).isEqualTo(nuevaFecha);
    }
    
    @Test
    @DisplayName("Eliminar match - debe eliminarse")
    void eliminar_cuandoMatchExiste_debeEliminar() {
        // Arrange
        em.persist(matchTest);
        flushAndClear();
        Long id = matchTest.getIdMatch();
        
        // Act
        Match aEliminar = em.find(Match.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Match eliminado = em.find(Match.class, id);
        assertThat(eliminado).isNull();
    }
    
    @Test
    @DisplayName("Buscar matches por usuario A - debe retornar lista")
    void buscarPorUsuarioA_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(matchTest);
        
        Usuario usuarioC = new Usuario.Builder()
                .nombre("Pedro")
                .apellidoPaterno("García")
                .correoElectronico("pedro@email.com")
                .contrasena("password789")
                .fechaRegistro(LocalDateTime.now())
                .build();
        em.persist(usuarioC);
        
        Match otroMatch = new Match.Builder()
                .usuarioA(usuarioA)
                .usuarioB(usuarioC)
                .fechaMatch(LocalDateTime.now())
                .build();
        em.persist(otroMatch);
        
        flushAndClear();
        
        // Act
        List<Match> matches = em.createQuery(
                "SELECT m FROM Match m WHERE m.usuarioA.idUsuario = :usuarioId", 
                Match.class)
                .setParameter("usuarioId", usuarioA.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(matches).hasSize(2);
        assertThat(matches)
                .allMatch(m -> m.getUsuarioA().getIdUsuario().equals(usuarioA.getIdUsuario()));
    }
    
    @Test
    @DisplayName("Buscar matches por usuario B - debe retornar lista")
    void buscarPorUsuarioB_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(matchTest);
        flushAndClear();
        
        // Act
        List<Match> matches = em.createQuery(
                "SELECT m FROM Match m WHERE m.usuarioB.idUsuario = :usuarioId", 
                Match.class)
                .setParameter("usuarioId", usuarioB.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(matches).hasSize(1);
        assertThat(matches.get(0).getUsuarioB().getIdUsuario())
                .isEqualTo(usuarioB.getIdUsuario());
    }
    
    @Test
    @DisplayName("Listar todos los matches - debe retornar lista")
    void listar_debeRetornarTodosLosMatches() {
        // Arrange
        em.persist(matchTest);
        flushAndClear();
        
        // Act
        List<Match> todos = em.createQuery("SELECT m FROM Match m", Match.class)
                .getResultList();
        
        // Assert
        assertThat(todos).hasSize(1);
    }
}