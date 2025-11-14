package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Bloqueo;
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
@DisplayName("Tests de Integración - BloqueoDAO")
class BloqueoDAOTest extends BaseIntegrationTest {
    
    private Usuario usuarioBloqueador;
    private Usuario usuarioBloqueado;
    private Bloqueo bloqueoTest;
    
    @BeforeEach
    void setUpTest() {
        // Crear usuarios primero
        usuarioBloqueador = new Usuario.Builder()
                .nombre("Juan")
                .apellidoPaterno("Pérez")
                .correoElectronico("juan@email.com")
                .contrasena("password123")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        usuarioBloqueado = new Usuario.Builder()
                .nombre("María")
                .apellidoPaterno("López")
                .correoElectronico("maria@email.com")
                .contrasena("password456")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        em.persist(usuarioBloqueador);
        em.persist(usuarioBloqueado);
        flush();
        
        // Crear bloqueo
        bloqueoTest = new Bloqueo.Builder()
                .usuarioBloqueador(usuarioBloqueador)
                .usuarioBloqueado(usuarioBloqueado)
                .fechaBloqueo(LocalDateTime.now())
                .build();
    }
    
    @Test
    @DisplayName("Insertar bloqueo válido - debe persistir")
    void insertar_cuandoBloqueoValido_debePersistir() {
        // Act
        em.persist(bloqueoTest);
        flushAndClear();
        
        // Assert
        Bloqueo encontrado = em.find(Bloqueo.class, bloqueoTest.getIdBloqueo());
        
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUsuarioBloqueador().getIdUsuario())
                .isEqualTo(usuarioBloqueador.getIdUsuario());
        assertThat(encontrado.getUsuarioBloqueado().getIdUsuario())
                .isEqualTo(usuarioBloqueado.getIdUsuario());
    }
    
    @Test
    @DisplayName("Actualizar bloqueo - debe modificar")
    void actualizar_cuandoBloqueoExiste_debeModificar() {
        // Arrange
        em.persist(bloqueoTest);
        flushAndClear();
        
        // Act
        Bloqueo aActualizar = em.find(Bloqueo.class, bloqueoTest.getIdBloqueo());
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(1);
        aActualizar.setFechaBloqueo(nuevaFecha);
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Bloqueo actualizado = em.find(Bloqueo.class, bloqueoTest.getIdBloqueo());
        assertThat(actualizado.getFechaBloqueo()).isEqualTo(nuevaFecha);
    }
    
    @Test
    @DisplayName("Eliminar bloqueo - debe eliminarse")
    void eliminar_cuandoBloqueoExiste_debeEliminar() {
        // Arrange
        em.persist(bloqueoTest);
        flushAndClear();
        Long id = bloqueoTest.getIdBloqueo();
        
        // Act
        Bloqueo aEliminar = em.find(Bloqueo.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Bloqueo eliminado = em.find(Bloqueo.class, id);
        assertThat(eliminado).isNull();
    }
    
    @Test
    @DisplayName("Buscar bloqueos por bloqueador - debe retornar lista")
    void buscarPorBloqueador_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(bloqueoTest);
        
        Usuario otroBloqueado = new Usuario.Builder()
                .nombre("Pedro")
                .apellidoPaterno("García")
                .correoElectronico("pedro@email.com")
                .contrasena("password789")
                .fechaRegistro(LocalDateTime.now())
                .build();
        em.persist(otroBloqueado);
        
        Bloqueo otroBloqueo = new Bloqueo.Builder()
                .usuarioBloqueador(usuarioBloqueador)
                .usuarioBloqueado(otroBloqueado)
                .fechaBloqueo(LocalDateTime.now())
                .build();
        em.persist(otroBloqueo);
        
        flushAndClear();
        
        // Act
        List<Bloqueo> bloqueos = em.createQuery(
                "SELECT b FROM Bloqueo b WHERE b.usuarioBloqueador.idUsuario = :usuarioId", 
                Bloqueo.class)
                .setParameter("usuarioId", usuarioBloqueador.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(bloqueos).hasSize(2);
        assertThat(bloqueos)
                .allMatch(b -> b.getUsuarioBloqueador().getIdUsuario()
                        .equals(usuarioBloqueador.getIdUsuario()));
    }
    
    @Test
    @DisplayName("Buscar bloqueos por bloqueado - debe retornar lista")
    void buscarPorBloqueado_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(bloqueoTest);
        flushAndClear();
        
        // Act
        List<Bloqueo> bloqueos = em.createQuery(
                "SELECT b FROM Bloqueo b WHERE b.usuarioBloqueado.idUsuario = :usuarioId", 
                Bloqueo.class)
                .setParameter("usuarioId", usuarioBloqueado.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(bloqueos).hasSize(1);
        assertThat(bloqueos.get(0).getUsuarioBloqueado().getIdUsuario())
                .isEqualTo(usuarioBloqueado.getIdUsuario());
    }
    
    @Test
    @DisplayName("Listar todos los bloqueos - debe retornar lista")
    void listar_debeRetornarTodosLosBloqueos() {
        // Arrange
        em.persist(bloqueoTest);
        flushAndClear();
        
        // Act
        List<Bloqueo> todos = em.createQuery("SELECT b FROM Bloqueo b", Bloqueo.class)
                .getResultList();
        
        // Assert
        assertThat(todos).hasSize(1);
    }
}