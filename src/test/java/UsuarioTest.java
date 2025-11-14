/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.hiChatJpa.entitys.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UsuarioTest {

      private UsuarioService usuarioService;
    private Usuario usuario;

  

    @Test
    public void testRegistrarUsuarioNulo() {
        Exception e = assertThrows(Exception.class, () -> usuarioService.registrarUsuario(null));
        assertEquals("El usuario no puede ser nulo.", e.getMessage());
    }

    @Test
    public void testRegistrarUsuarioCorreoNulo() {
        Usuario u = new Usuario.Builder().nombre("Ana").build();
        Exception e = assertThrows(Exception.class, () -> usuarioService.registrarUsuario(u));
        assertEquals("El usuario debe tener un correo válido.", e.getMessage());
    }

    @Test
    public void testActualizarUsuarioNulo() {
        Exception e = assertThrows(Exception.class, () -> usuarioService.actualizarUsuario(null));
        assertEquals("Debe especificar un usuario válido para actualizar.", e.getMessage());
    }

    @Test
    public void testEliminarUsuarioIdNulo() {
        Exception e = assertThrows(Exception.class, () -> usuarioService.eliminarUsuario(null));
        assertEquals("ID inválido para eliminar usuario.", e.getMessage());
    }

    @Test
    public void testBuscarPorId() {
        Usuario u = usuarioService.buscarPorId(1L);
        assertNotNull(u);
        assertEquals("Juan", u.getNombre());
    }

    @Test
    public void testListarUsuarios() {
        List<Usuario> lista = usuarioService.listarUsuarios();
        assertNotNull(lista);
        assertEquals(1, lista.size());
    }

    @Test
    public void testBuscarPorCorreo() {
        Usuario u = usuarioService.buscarPorCorreo("juan@mail.com");
        assertNotNull(u);
        assertEquals("Juan", u.getNombre());
    }

    @Test
    public void testBuscarPorNombreCompleto() {
        List<Usuario> lista = usuarioService.buscarPorNombreCompleto("Juan", "Pérez");
        assertNotNull(lista);
        assertEquals(1, lista.size());
    }
}
