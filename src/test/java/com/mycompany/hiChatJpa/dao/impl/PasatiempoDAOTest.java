package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 
 * @author gatog
 */
@DisplayName("Tests de Integración - PasatiempoDAO")
class PasatiempoDAOTest extends BaseIntegrationTest {
    
    private Pasatiempo pasatiempoTest;
    
    @BeforeEach
    void setUpTest() {
        pasatiempoTest = new Pasatiempo.Builder()
                .nombre("Fútbol")
                .descripcion("Deporte de equipo")
                .build();
    }
    
    @Test
    @DisplayName("Insertar pasatiempo válido - debe persistir")
    void insertar_cuandoPasatiempoValido_debePersistir() {
        // Act
        em.persist(pasatiempoTest);
        flushAndClear();
        
        // Assert
        Pasatiempo encontrado = em.find(Pasatiempo.class, pasatiempoTest.getIdPasatiempo());
        
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNombre()).isEqualTo("Fútbol");
        assertThat(encontrado.getDescripcion()).isEqualTo("Deporte de equipo");
    }
    
    @Test
    @DisplayName("Insertar múltiples pasatiempos - deben persistir")
    void insertar_cuandoMultiplesPasatiempos_debenPersistir() {
        // Arrange
        Pasatiempo pasatiempo2 = new Pasatiempo.Builder()
                .nombre("Lectura")
                .descripcion("Leer libros")
                .build();
        
        // Act
        em.persist(pasatiempoTest);
        em.persist(pasatiempo2);
        flushAndClear();
        
        // Assert
        List<Pasatiempo> pasatiempos = em.createQuery(
                "SELECT p FROM Pasatiempo p", 
                Pasatiempo.class)
                .getResultList();
        
        assertThat(pasatiempos).hasSize(2);
        assertThat(pasatiempos)
                .extracting(Pasatiempo::getNombre)
                .contains("Fútbol", "Lectura");
    }
    
    @Test
    @DisplayName("Actualizar pasatiempo - debe modificar")
    void actualizar_cuandoPasatiempoExiste_debeModificar() {
        // Arrange
        em.persist(pasatiempoTest);
        flushAndClear();
        
        // Act
        Pasatiempo aActualizar = em.find(Pasatiempo.class, pasatiempoTest.getIdPasatiempo());
        aActualizar.setDescripcion("Deporte rey");
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Pasatiempo actualizado = em.find(Pasatiempo.class, pasatiempoTest.getIdPasatiempo());
        assertThat(actualizado.getDescripcion()).isEqualTo("Deporte rey");
    }
    
    @Test
    @DisplayName("Eliminar pasatiempo - debe eliminarse")
    void eliminar_cuandoPasatiempoExiste_debeEliminar() {
        // Arrange
        em.persist(pasatiempoTest);
        flushAndClear();
        Long id = pasatiempoTest.getIdPasatiempo();
        
        // Act
        Pasatiempo aEliminar = em.find(Pasatiempo.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Pasatiempo eliminado = em.find(Pasatiempo.class, id);
        assertThat(eliminado).isNull();
    }
    
    @Test
    @DisplayName("Buscar pasatiempo por ID existente - debe retornar pasatiempo")
    void buscar_cuandoIdExiste_debeRetornarPasatiempo() {
        // Arrange
        em.persist(pasatiempoTest);
        flushAndClear();
        Long id = pasatiempoTest.getIdPasatiempo();
        
        // Act
        Pasatiempo encontrado = em.find(Pasatiempo.class, id);
        
        // Assert
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getIdPasatiempo()).isEqualTo(id);
        assertThat(encontrado.getNombre()).isEqualTo("Fútbol");
    }
    
    @Test
    @DisplayName("Buscar pasatiempo por ID inexistente - debe retornar null")
    void buscar_cuandoIdNoExiste_debeRetornarNull() {
        // Act
        Pasatiempo encontrado = em.find(Pasatiempo.class, 999L);
        
        // Assert
        assertThat(encontrado).isNull();
    }
    
    @Test
    @DisplayName("Buscar pasatiempo por nombre - debe retornar pasatiempo")
    void buscarPorNombre_cuandoExiste_debeRetornarPasatiempo() {
        // Arrange
        em.persist(pasatiempoTest);
        flushAndClear();
        
        // Act
        List<Pasatiempo> resultado = em.createQuery(
                "SELECT p FROM Pasatiempo p WHERE p.nombre = :nombre", 
                Pasatiempo.class)
                .setParameter("nombre", "Fútbol")
                .getResultList();
        
        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Fútbol");
    }
    
    @Test
    @DisplayName("Buscar pasatiempo por nombre inexistente - debe retornar lista vacía")
    void buscarPorNombre_cuandoNoExiste_debeRetornarListaVacia() {
        // Act
        List<Pasatiempo> resultado = em.createQuery(
                "SELECT p FROM Pasatiempo p WHERE p.nombre = :nombre", 
                Pasatiempo.class)
                .setParameter("nombre", "Inexistente")
                .getResultList();
        
        // Assert
        assertThat(resultado).isEmpty();
    }
    
    @Test
    @DisplayName("Listar todos los pasatiempos - debe retornar lista")
    void listar_debeRetornarTodosLosPasatiempos() {
        // Arrange
        em.persist(pasatiempoTest);
        
        Pasatiempo pasatiempo2 = new Pasatiempo.Builder()
                .nombre("Música")
                .descripcion("Tocar instrumentos")
                .build();
        em.persist(pasatiempo2);
        
        flushAndClear();
        
        // Act
        List<Pasatiempo> todos = em.createQuery(
                "SELECT p FROM Pasatiempo p ORDER BY p.nombre ASC", 
                Pasatiempo.class)
                .getResultList();
        
        // Assert
        assertThat(todos).hasSize(2);
        assertThat(todos)
                .extracting(Pasatiempo::getNombre)
                .containsExactly("Fútbol", "Música");
    }
    
    @Test
    @DisplayName("Listar cuando tabla vacía - debe retornar lista vacía")
    void listar_cuandoTablaVacia_debeRetornarListaVacia() {
        // Act
        List<Pasatiempo> todos = em.createQuery(
                "SELECT p FROM Pasatiempo p", 
                Pasatiempo.class)
                .getResultList();
        
        // Assert
        assertThat(todos).isEmpty();
    }
}