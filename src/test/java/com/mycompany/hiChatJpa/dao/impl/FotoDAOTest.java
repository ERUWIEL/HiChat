package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Foto;
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
@DisplayName("Tests de Integración - FotoDAO")
class FotoDAOTest extends BaseIntegrationTest {
    
    private Foto fotoTest;
    private Usuario usuarioTest;
    
    @BeforeEach
    void setUpTest() {
        // Crear usuario
        usuarioTest = new Usuario.Builder()
                .nombre("Juan")
                .apellidoPaterno("Pérez")
                .correoElectronico("juan@email.com")
                .contrasena("password123")
                .fechaRegistro(LocalDateTime.now())
                .build();
        em.persist(usuarioTest);
        flush();
        
        // Crear foto
        fotoTest = new Foto.Builder()
                .urlFoto("https://example.com/foto1.jpg")
                .descripcion("Foto de perfil")
                .usuario(usuarioTest)
                .build();
    }
    
    @Test
    @DisplayName("Insertar foto válida - debe persistir")
    void insertar_cuandoFotoValida_debePersistir() {
        // Act
        em.persist(fotoTest);
        flushAndClear();
        
        // Assert
        Foto encontrada = em.find(Foto.class, fotoTest.getIdFoto());
        
        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getUrlFoto()).isEqualTo("https://example.com/foto1.jpg");
        assertThat(encontrada.getDescripcion()).isEqualTo("Foto de perfil");
        assertThat(encontrada.getUsuario().getIdUsuario()).isEqualTo(usuarioTest.getIdUsuario());
    }
    
    @Test
    @DisplayName("Actualizar foto - debe modificar")
    void actualizar_cuandoFotoExiste_debeModificar() {
        // Arrange
        em.persist(fotoTest);
        flushAndClear();
        
        // Act
        Foto aActualizar = em.find(Foto.class, fotoTest.getIdFoto());
        aActualizar.setDescripcion("Nueva descripción");
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Foto actualizada = em.find(Foto.class, fotoTest.getIdFoto());
        assertThat(actualizada.getDescripcion()).isEqualTo("Nueva descripción");
    }
    
    @Test
    @DisplayName("Eliminar foto - debe eliminarse")
    void eliminar_cuandoFotoExiste_debeEliminar() {
        // Arrange
        em.persist(fotoTest);
        flushAndClear();
        Long id = fotoTest.getIdFoto();
        
        // Act
        Foto aEliminar = em.find(Foto.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Foto eliminada = em.find(Foto.class, id);
        assertThat(eliminada).isNull();
    }
    
    @Test
    @DisplayName("Buscar fotos por usuario - debe retornar lista")
    void buscarPorUsuario_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(fotoTest);
        
        Foto foto2 = new Foto.Builder()
                .urlFoto("https://example.com/foto2.jpg")
                .descripcion("Segunda foto")
                .usuario(usuarioTest)
                .build();
        em.persist(foto2);
        
        flushAndClear();
        
        // Act
        List<Foto> fotos = em.createQuery(
                "SELECT f FROM Foto f WHERE f.usuario.idUsuario = :usuarioId", 
                Foto.class)
                .setParameter("usuarioId", usuarioTest.getIdUsuario())
                .getResultList();
        
        // Assert
        assertThat(fotos).hasSize(2);
        assertThat(fotos)
                .allMatch(f -> f.getUsuario().getIdUsuario().equals(usuarioTest.getIdUsuario()));
    }
    
    @Test
    @DisplayName("Buscar fotos por descripción - debe retornar lista")
    void buscarPorDescripcion_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(fotoTest);
        flushAndClear();
        
        // Act
        List<Foto> fotos = em.createQuery(
                "SELECT f FROM Foto f WHERE f.descripcion LIKE :descripcion", 
                Foto.class)
                .setParameter("descripcion", "%perfil%")
                .getResultList();
        
        // Assert
        assertThat(fotos).hasSize(1);
        assertThat(fotos.get(0).getDescripcion()).contains("perfil");
    }
    
    @Test
    @DisplayName("Listar todas las fotos - debe retornar lista")
    void listar_debeRetornarTodasLasFotos() {
        // Arrange
        em.persist(fotoTest);
        flushAndClear();
        
        // Act
        List<Foto> todas = em.createQuery("SELECT f FROM Foto f", Foto.class)
                .getResultList();
        
        // Assert
        assertThat(todas).hasSize(1);
    }
}
