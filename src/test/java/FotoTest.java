/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.impl.FotoService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */
public class FotoTest {
    
 // -------------------------------------------------------------
    // TESTS DE registrarFoto()
    // -------------------------------------------------------------
    
    @Test
    public void registrarFoto_fotoNula_lanzaExcepcion() throws Exception {
        FotoService servicio = new FotoService();

        Exception ex = assertThrows(
            Exception.class,
            () -> servicio.registrarFoto(null)
        );

        assertEquals("La foto no puede ser nula.", ex.getMessage());
    }

    @Test
    public void registrarFoto() throws Exception {
        FotoService servicio = new FotoService();
        
        Usuario usuario = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();

        Foto f =  new Foto.Builder()
        .idFoto(null) // al guardar en BD se genera
        .urlFoto("https://cdn.perfilusuario.com/fotos/luis_profile_01.jpg")
        .descripcion("Foto de perfil tomada en un evento de tecnología en CDMX.")
        .usuario(usuario)
        .build();
        f.setUrlFoto("img1.jpg");
        f.setUsuario(null);

        Exception ex = assertThrows(
            Exception.class,
            () -> servicio.registrarFoto(f)
        );

        assertEquals("Debe especificar el usuario al que pertenece la foto.", ex.getMessage());
    }

    @Test
    public void registrarFoto_urlVacia_lanzaExcepcion() throws Exception {
        FotoService servicio = new FotoService();
        
           Usuario usuario = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();

        Foto f =  new Foto.Builder()
        .idFoto(null) // al guardar en BD se genera
        .urlFoto("https://cdn.perfilusuario.com/fotos/candy.jpg")
        .descripcion("Foto de perfil tomada en un evento de tecnología en CDMX.")
        .usuario(usuario)
        .build();
        f.setUsuario(new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build());
        f.setUrlFoto("  ");

        Exception ex = assertThrows(
            Exception.class,
            () -> servicio.registrarFoto(f)
        );

        assertEquals("La URL de la foto es obligatoria.", ex.getMessage());
    }

    // -------------------------------------------------------------
    // TESTS DE actualizarFoto()
    // -------------------------------------------------------------

    @Test
    public void actualizarFoto_fotoNula_lanzaExcepcion() throws Exception {
        FotoService servicio = new FotoService();

        Exception ex = assertThrows(
            Exception.class,
            () -> servicio.actualizarFoto(null)
        );

        assertEquals("Debe especificar una foto válida para actualizar.", ex.getMessage());
    }

    @Test
    public void actualizarFoto_idNulo_lanzaExcepcion() throws Exception {
        FotoService servicio = new FotoService();
        
           Usuario usuario = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();

        Foto f =  new Foto.Builder()
        .idFoto(null) // al guardar en BD se genera
        .urlFoto("https://cdn.perfilusuario.com/fotos/said_profile_01.jpg")
        .descripcion("Foto de perfil tomada en un evento de tecnología en CDMX.")
        .usuario(usuario)
        .build();
        f.setIdFoto(null);

        Exception ex = assertThrows(
            Exception.class,
            () -> servicio.actualizarFoto(f)
        );

        assertEquals("Debe especificar una foto válida para actualizar.", ex.getMessage());
    }

    // -------------------------------------------------------------
    // TESTS DE eliminarFoto()
    // -------------------------------------------------------------

    @Test
    public void eliminarFoto_idNulo_lanzaExcepcion() throws Exception {
        FotoService servicio = new FotoService();

        Exception ex = assertThrows(
            Exception.class,
            () -> servicio.eliminarFoto(null)
        );

        assertEquals("ID inválido para eliminar foto.", ex.getMessage());
    }

    @Test
    public void eliminarFoto_idNegativo_lanzaExcepcion() throws Exception {
        FotoService servicio = new FotoService();

        Exception ex = assertThrows(
            Exception.class,
            () -> servicio.eliminarFoto(-5L)
        );

        assertEquals("ID inválido para eliminar foto.", ex.getMessage());
    }

    // -------------------------------------------------------------
    // TESTS DE buscarPorId()
    // -------------------------------------------------------------

    @Test
    public void buscarPorId_idInvalido_regresaNull() {
        FotoService servicio = new FotoService();

        assertNull(servicio.buscarPorId(null));
        assertNull(servicio.buscarPorId(0L));
        assertNull(servicio.buscarPorId(-2L));
    }

    // -------------------------------------------------------------
    // TESTS DE listarPorDescripcion()
    // -------------------------------------------------------------

    @Test
    public void listarPorDescripcion_descripcionVacia_regresaNull() {
        FotoService servicio = new FotoService();

        assertNull(servicio.listarPorDescripcion(""));
        assertNull(servicio.listarPorDescripcion("   "));
        assertNull(servicio.listarPorDescripcion(null));
    }

    // -------------------------------------------------------------
    // TESTS DE listarPorUsuario()
    // -------------------------------------------------------------

    @Test
    public void listarPorUsuario_usuarioNulo_regresaNull() {
        FotoService servicio = new FotoService();

        assertNull(servicio.listarPorUsuario(null));
    }
}
