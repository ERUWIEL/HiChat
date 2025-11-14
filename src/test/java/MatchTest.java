/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.hiChatJpa.entitys.Match;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.impl.MatchService;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */
public class MatchTest {
    
  private final MatchService servicio = new MatchService();

    // -------------------------------------------------------------
    // registrarMatch()
    // -------------------------------------------------------------
    @Test
    public void testRegistrarMatch_null() throws Exception {
        servicio.registrarMatch(null);
    }

    @Test
    public void testRegistrarMatch_usuariosNulos() throws Exception {
    Usuario usuarioA = new Usuario.Builder()
        .nombre("Abdiel")
        .apellidoPaterno("Bargas")
        .correoElectronico("abdiel@booking.com")
        .contrasena("12ert3")
        .build();

Usuario usuarioB = new Usuario.Builder()
        .nombre("Diana")
        .apellidoPaterno("Lopez")
        .correoElectronico("diana@booking.com")
        .contrasena("pass123")
        .build();

Match match = new Match.Builder()
        .usuarioA(usuarioA)
        .usuarioB(usuarioB)
        .build();

        servicio.registrarMatch(match);
    }

    @Test
    public void testRegistrarMatch_mismoUsuario() throws Exception {
      Usuario usuarioA = new Usuario.Builder()
        .nombre("Abdiel")
        .apellidoPaterno("Bargas")
        .correoElectronico("abdiel@booking.com")
        .contrasena("12ert3")
        .build();

Usuario usuarioB = new Usuario.Builder()
        .nombre("Diana")
        .apellidoPaterno("Lopez")
        .correoElectronico("diana@booking.com")
        .contrasena("pass123")
        .build();

Match m = new Match.Builder()
        .usuarioA(usuarioA)
        .usuarioB(usuarioB)
        .build();


        servicio.registrarMatch(m);
    }

    @Test
    public void testRegistrarMatch_valido() throws Exception {
        Usuario a = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
        a.setIdUsuario(1L);

        Usuario b = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
        b.setIdUsuario(2L);

    Usuario usuarioA = new Usuario.Builder()
        .nombre("Abdiel")
        .apellidoPaterno("Bargas")
        .correoElectronico("abdiel@booking.com")
        .contrasena("12ert3")
        .build();

Usuario usuarioB = new Usuario.Builder()
        .nombre("Diana")
        .apellidoPaterno("Lopez")
        .correoElectronico("diana@booking.com")
        .contrasena("pass123")
        .build();

Match match = new Match.Builder()
        .usuarioA(usuarioA)
        .usuarioB(usuarioB)
        .build();


        servicio.registrarMatch(match); 
    }

    // -------------------------------------------------------------
    // actualizarMatch()
    // -------------------------------------------------------------
    @Test
    public void testActualizarMatch_null() throws Exception {
        servicio.actualizarMatch(null);
    }

    @Test
    public void testActualizarMatch_sinId() throws Exception {
        Usuario usuarioA = new Usuario.Builder()
        .nombre("Abdiel")
        .apellidoPaterno("Bargas")
        .correoElectronico("abdiel@booking.com")
        .contrasena("12ert3")
        .build();

Usuario usuarioB = new Usuario.Builder()
        .nombre("Diana")
        .apellidoPaterno("Lopez")
        .correoElectronico("diana@booking.com")
        .contrasena("pass123")
        .build();

Match m = new Match.Builder()
        .usuarioA(usuarioA)
        .usuarioB(usuarioB)
        .build();

        servicio.actualizarMatch(m);
    }

    @Test
    public void testActualizarMatch_noExiste() throws Exception {
    Usuario usuarioA = new Usuario.Builder()
        .nombre("Abdiel")
        .apellidoPaterno("Bargas")
        .correoElectronico("abdiel@booking.com")
        .contrasena("12ert3")
        .build();

Usuario usuarioB = new Usuario.Builder()
        .nombre("Diana")
        .apellidoPaterno("Lopez")
        .correoElectronico("diana@booking.com")
        .contrasena("pass123")
        .build();

Match match = new Match.Builder()
        .usuarioA(usuarioA)
        .usuarioB(usuarioB)
        .build();

        match.setIdMatch(999L);
        servicio.actualizarMatch(match);
    }

    // -------------------------------------------------------------
    // eliminarMatch()
    // -------------------------------------------------------------
    @Test
    public void testEliminarMatch_idInvalido() throws Exception {
        servicio.eliminarMatch(0L);
    }

    @Test
    public void testEliminarMatch_noExiste() throws Exception {
        servicio.eliminarMatch(123L);
    }

    // -------------------------------------------------------------
    // buscarPorId()
    // -------------------------------------------------------------
    @Test
    public void testBuscarPorId_invalido() {
        assertNull(servicio.buscarPorId(0L));
        assertNull(servicio.buscarPorId(null));
    }

    @Test
    public void testBuscarPorId_validoNoExiste() {
        assertNull(servicio.buscarPorId(999L));
    }

    // -------------------------------------------------------------
    // listarMatchs()
    // -------------------------------------------------------------
    @Test
    public void testListarMatchs_basico() {
        List<Match> lista = servicio.listarMatchs();
        assertNotNull(lista); // puede ser lista vacía pero no null
    }

    // -------------------------------------------------------------
    // listarPorUsuarioA()
    // -------------------------------------------------------------
    @Test
    public void testListarPorUsuarioA_null() {
        assertNull(servicio.listarPorUsuarioA(null));
    }

    @Test
    public void testListarPorUsuarioA_valido() {
        Usuario u = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
        u.setIdUsuario(1L);

        List<Match> lista = servicio.listarPorUsuarioA(u);
        assertNotNull(lista);
    }

    // -------------------------------------------------------------
    // listarPorUsuarioB()
    // -------------------------------------------------------------
    @Test
    public void testListarPorUsuarioB_null() {
        assertNull(servicio.listarPorUsuarioB(null));
    }

    @Test
    public void testListarPorUsuarioB_valido() {
        Usuario u = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
        u.setIdUsuario(1L);

        List<Match> lista = servicio.listarPorUsuarioB(u);
        assertNotNull(lista);
    }
}
