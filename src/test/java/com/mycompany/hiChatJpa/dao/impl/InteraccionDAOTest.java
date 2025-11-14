package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
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
@DisplayName("Tests de Integración - InteraccionDAO")
class InteraccionDAOTest extends BaseIntegrationTest {
    
    private Interaccion interaccionTest;
    private Usuario usuarioEmisor;
    private Usuario usuarioReceptor;
    
    @BeforeEach
    void setUpTest() {
        // Crear usuarios
        usuarioEmisor = new Usuario.Builder()
                .nombre("Juan")
                .apellidoPaterno("Pérez")
                .correoElectronico("juan@email.com")
                .contrasena("password123")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        usuarioReceptor = new Usuario.Builder()
                .nombre("María")
                .apellidoPaterno("López")
                .correoElectronico("maria@email.com")
                .contrasena("password456")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        em.persist(usuarioEmisor);
        em.persist(usuarioReceptor);
        flush();
        
        // Crear interacción
        interaccionTest = new Interaccion.Builder()
                .usuarioEmisor(usuarioEmisor)
                .usuarioReceptor(usuarioReceptor)
                .tipo(TipoInteraccion.ME_GUSTA)
                .fechaInteraccion(LocalDateTime.now())
                .build();
    }
    
    @Test
    @DisplayName("Insertar interacción válida - debe persistir")
    void insertar_cuandoInteraccionValida_debePersistir() {
        // Act
        em.persist(interaccionTest);
        flushAndClear();
        
        // Assert
        Interaccion encontrada = em.find(Interaccion.class, interaccionTest.getIdInteraccion());
        
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getTipo()).isEqualTo(TipoInteraccion.ME_GUSTA);
        assertThat(encontrada.getUsuarioEmisor().getIdUsuario())
                .isEqualTo(usuarioEmisor.getIdUsuario());
        assertThat(encontrada.getUsuarioReceptor().getIdUsuario())
                .isEqualTo(usuarioReceptor.getIdUsuario());
    }
    
    @Test
    @DisplayName("Actualizar interacción - debe modificar")
    void actualizar_cuandoInteraccionExiste_debeModificar() {
        // Arrange
        em.persist(interaccionTest);
        flushAndClear();
        
        // Act
        Interaccion aActualizar = em.find(Interaccion.class, interaccionTest.getIdInteraccion());
        aActualizar.setTipo(TipoInteraccion.ME_GUSTA);
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Interaccion actualizada = em.find(Interaccion.class, interaccionTest.getIdInteraccion());
        assertThat(actualizada.getTipo()).isEqualTo(TipoInteraccion.ME_GUSTA);
    }
    
    @Test
    @DisplayName("Eliminar interacción - debe eliminarse")
    void eliminar_cuandoInteraccionExiste_debeEliminar() {
        // Arrange
        em.persist(interaccionTest);
        flushAndClear();
        Long id = interaccionTest.getIdInteraccion();
        
        // Act
        Interaccion aEliminar = em.find(Interaccion.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Interaccion eliminada = em.find(Interaccion.class, id);
        assertThat(eliminada).isNull();
    }
    
    @Test
    @DisplayName("Buscar interacciones por emisor - debe retornar lista")
    void buscarPorEmisor_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(interaccionTest);
        
        Usuario otroReceptor = new Usuario.Builder()
                .nombre("Pedro")
                .apellidoPaterno("García")
                .correoElectronico("pedro@email.com")
                .contrasena("password789")
                .fechaRegistro(LocalDateTime.now())
                .build();
        em.persist(otroReceptor);
        
        Interaccion otraInteraccion = new Interaccion.Builder()
                .usuarioEmisor(usuarioEmisor)
                .usuarioReceptor(otroReceptor)
                .tipo(TipoInteraccion.NO_ME_INTERESA)
                .fechaInteraccion(LocalDateTime.now())
                .build();
        em.persist(otraInteraccion);
        
        flushAndClear();
        
        // Act
        List<Interaccion> interacciones = em.createQuery(
                "SELECT i FROM Interaccion i WHERE i.usuarioEmisor.idUsuario = :usuarioId", 
                Interaccion.class)
                .setParameter("usuarioId", usuarioEmisor.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(interacciones).hasSize(2);
        assertThat(interacciones)
                .allMatch(i -> i.getUsuarioEmisor().getIdUsuario()
                        .equals(usuarioEmisor.getIdUsuario()));
    }
    
    @Test
    @DisplayName("Buscar interacciones por receptor - debe retornar lista")
    void buscarPorReceptor_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(interaccionTest);
        flushAndClear();
        
        // Act
        List<Interaccion> interacciones = em.createQuery(
                "SELECT i FROM Interaccion i WHERE i.usuarioReceptor.idUsuario = :usuarioId", 
                Interaccion.class)
                .setParameter("usuarioId", usuarioReceptor.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(interacciones).hasSize(1);
        assertThat(interacciones.get(0).getUsuarioReceptor().getIdUsuario())
                .isEqualTo(usuarioReceptor.getIdUsuario());
    }
    
    @Test
    @DisplayName("Buscar interacciones por tipo - debe retornar lista")
    void buscarPorTipo_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(interaccionTest);
        flushAndClear();
        
        // Act
        List<Interaccion> interacciones = em.createQuery(
                "SELECT i FROM Interaccion i WHERE i.tipo = :tipo", 
                Interaccion.class)
                .setParameter("tipo", TipoInteraccion.ME_GUSTA)
                .getResultList();
        
        // Assert
        assertThat(interacciones).hasSize(1);
        assertThat(interacciones.get(0).getTipo()).isEqualTo(TipoInteraccion.ME_GUSTA);
    }
    
    @Test
    @DisplayName("Listar todas las interacciones - debe retornar lista")
    void listar_debeRetornarTodasLasInteracciones() {
        // Arrange
        em.persist(interaccionTest);
        flushAndClear();
        
        // Act
        List<Interaccion> todas = em.createQuery("SELECT i FROM Interaccion i", Interaccion.class)
                .getResultList();
        
        // Assert
        assertThat(todas).hasSize(1);
    }
}
