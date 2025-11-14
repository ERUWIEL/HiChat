package com.mycompany.hiChatJpa.dao.impl;

import com.mycompany.hiChatJpa.BaseIntegrationTest;
import com.mycompany.hiChatJpa.entitys.Genero;
import com.mycompany.hiChatJpa.entitys.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 
 * @author gatog
 */
@DisplayName("Tests de Integración - UsuarioDAO")
class UsuarioDAOTest extends BaseIntegrationTest {
    
    private Usuario usuarioTest;
    
    @BeforeEach
    void setUpTest() {
        usuarioTest = new Usuario.Builder()
                .nombre("Juan")
                .apellidoPaterno("Pérez")
                .apellidoMaterno("García")
                .correoElectronico("juan.perez@email.com")
                .contrasena("password123")
                .genero(Genero.MASCULINO)
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .fechaRegistro(LocalDateTime.now())
                .carrera("Ingeniería en Software")
                .biografia("Usuario de prueba")
                .build();
    }
    
    @Test
    @DisplayName("Insertar usuario válido - debe persistir correctamente")
    void insertar_cuandoUsuarioValido_debePersistir() {
        // Act - Usa directamente el EntityManager
        em.persist(usuarioTest);
        flushAndClear(); // Simula una nueva transacción
        
        // Assert
        Usuario encontrado = em.find(Usuario.class, usuarioTest.getIdUsuario());
        
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNombre()).isEqualTo("Juan");
        assertThat(encontrado.getCorreoElectronico()).isEqualTo("juan.perez@email.com");
        assertThat(encontrado.getGenero()).isEqualTo(Genero.MASCULINO);
    }
    
    @Test
    @DisplayName("Insertar múltiples usuarios - deben persistir correctamente")
    void insertar_cuandoMultiplesUsuarios_debenPersistir() {
        // Arrange
        Usuario usuario2 = new Usuario.Builder()
                .nombre("María")
                .apellidoPaterno("López")
                .correoElectronico("maria.lopez@email.com")
                .contrasena("password456")
                .fechaRegistro(LocalDateTime.now())
                .build();
        
        // Act
        em.persist(usuarioTest);
        em.persist(usuario2);
        flushAndClear();
        
        // Assert
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                .getResultList();
        
        assertThat(usuarios).hasSize(2);
        assertThat(usuarios)
                .extracting(Usuario::getNombre)
                .contains("Juan", "María");
    }
    
    @Test
    @DisplayName("Actualizar usuario existente - debe modificar los datos")
    void actualizar_cuandoUsuarioExiste_debeModificar() {
        // Arrange - Insertar usuario primero
        em.persist(usuarioTest);
        flushAndClear();
        
        // Act - Modificar datos
        Usuario aActualizar = em.find(Usuario.class, usuarioTest.getIdUsuario());
        aActualizar.setNombre("Juan Carlos");
        aActualizar.setBiografia("Biografía actualizada");
        em.merge(aActualizar);
        flushAndClear();
        
        // Assert
        Usuario actualizado = em.find(Usuario.class, usuarioTest.getIdUsuario());
        
        assertThat(actualizado.getNombre()).isEqualTo("Juan Carlos");
        assertThat(actualizado.getBiografia()).isEqualTo("Biografía actualizada");
        assertThat(actualizado.getCorreoElectronico()).isEqualTo("juan.perez@email.com");
    }
    
    @Test
    @DisplayName("Eliminar usuario existente - debe eliminarse")
    void eliminar_cuandoUsuarioExiste_debeEliminar() {
        // Arrange
        em.persist(usuarioTest);
        flushAndClear();
        Long id = usuarioTest.getIdUsuario();
        
        // Act
        Usuario aEliminar = em.find(Usuario.class, id);
        em.remove(aEliminar);
        flushAndClear();
        
        // Assert
        Usuario eliminado = em.find(Usuario.class, id);
        assertThat(eliminado).isNull();
    }
    
    @Test
    @DisplayName("Eliminar usuario inexistente - no debe lanzar error")
    void eliminar_cuandoUsuarioNoExiste_noDebeLanzarError() {
        // Act & Assert
        Usuario inexistente = em.find(Usuario.class, 999L);
        
        assertThat(inexistente).isNull();
        // Si no existe, simplemente no hay nada que eliminar
    }
    
    @Test
    @DisplayName("Buscar usuario por ID existente - debe retornar usuario")
    void buscar_cuandoIdExiste_debeRetornarUsuario() {
        // Arrange
        em.persist(usuarioTest);
        flushAndClear();
        Long id = usuarioTest.getIdUsuario();
        
        // Act
        Usuario encontrado = em.find(Usuario.class, id);
        
        // Assert
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getIdUsuario()).isEqualTo(id);
        assertThat(encontrado.getNombre()).isEqualTo("Juan");
    }
    
    @Test
    @DisplayName("Buscar usuario por ID inexistente - debe retornar null")
    void buscar_cuandoIdNoExiste_debeRetornarNull() {
        // Act
        Usuario encontrado = em.find(Usuario.class, 999L);
        
        // Assert
        assertThat(encontrado).isNull();
    }
    
    @Test
    @DisplayName("Buscar usuario por correo existente - debe retornar usuario")
    void buscarPorCorreo_cuandoCorreoExiste_debeRetornarUsuario() {
        // Arrange
        em.persist(usuarioTest);
        flushAndClear();
        
        // Act
        Usuario encontrado = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correoElectronico = :correo", 
                Usuario.class)
                .setParameter("correo", "juan.perez@email.com")
                .getSingleResult();
        
        // Assert
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getCorreoElectronico()).isEqualTo("juan.perez@email.com");
        assertThat(encontrado.getNombre()).isEqualTo("Juan");
    }
    
    @Test
    @DisplayName("Buscar usuario por correo inexistente - debe retornar null")
    void buscarPorCorreo_cuandoCorreoNoExiste_debeRetornarNull() {
        // Act
        List<Usuario> resultado = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.correoElectronico = :correo", 
                Usuario.class)
                .setParameter("correo", "noexiste@email.com")
                .getResultList();
        
        // Assert
        assertThat(resultado).isEmpty();
    }
    
    @Test
    @DisplayName("Buscar por nombre completo - debe retornar lista")
    void buscarPorNombreCompleto_cuandoExisten_debeRetornarLista() {
        // Arrange
        em.persist(usuarioTest);
        
        Usuario usuario2 = new Usuario.Builder()
                .nombre("Juan")
                .apellidoPaterno("Pérez")
                .apellidoMaterno("Rodríguez")
                .correoElectronico("juan.perez2@email.com")
                .contrasena("password123")
                .fechaRegistro(LocalDateTime.now())
                .build();
        em.persist(usuario2);
        
        flushAndClear();
        
        // Act
        List<Usuario> encontrados = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.apellidoPaterno = :apellidoPaterno", 
                Usuario.class)
                .setParameter("nombre", "Juan")
                .setParameter("apellidoPaterno", "Pérez")
                .getResultList();
        
        // Assert
        assertThat(encontrados).hasSize(2);
        assertThat(encontrados)
                .allMatch(u -> u.getNombre().equals("Juan") && u.getApellidoPaterno().equals("Pérez"));
    }
    
    @Test
    @DisplayName("Listar todos los usuarios - debe retornar lista")
    void listar_debeRetornarTodosLosUsuarios() {
        // Arrange
        em.persist(usuarioTest);
        
        Usuario usuario2 = new Usuario.Builder()
                .nombre("María")
                .apellidoPaterno("López")
                .correoElectronico("maria@email.com")
                .contrasena("password123")
                .fechaRegistro(LocalDateTime.now())
                .build();
        em.persist(usuario2);
        
        flushAndClear();
        
        // Act
        List<Usuario> todos = em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                .getResultList();
        
        // Assert
        assertThat(todos).hasSize(2);
        assertThat(todos)
                .extracting(Usuario::getNombre)
                .contains("Juan", "María");
    }
    
    @Test
    @DisplayName("Listar usuarios cuando la tabla está vacía - debe retornar lista vacía")
    void listar_cuandoTablaVacia_debeRetornarListaVacia() {
        // Act
        List<Usuario> todos = em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                .getResultList();
        
        // Assert
        assertThat(todos).isEmpty();
    }
    
    @Test
    @DisplayName("Usuario con pasatiempos - debe persistir sin error")
    void insertar_cuandoUsuarioConPasatiempos_debePersistirRelacion() {
        // Act
        em.persist(usuarioTest);
        flushAndClear();
        
        // Assert
        Usuario encontrado = em.find(Usuario.class, usuarioTest.getIdUsuario());
        assertThat(encontrado.getPasatiempos()).isNotNull();
        assertThat(encontrado.getPasatiempos()).isEmpty();
    }
}
