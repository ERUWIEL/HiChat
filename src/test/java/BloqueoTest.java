/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.hiChatJpa.entitys.Bloqueo;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class BloqueoTest {

    private Usuario bloqueador;
    private Usuario bloqueado;

    @BeforeEach
    public void setUp() {
        bloqueador = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
        bloqueado  = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
    }

    @Test
    public void testBuilderConstructor() {
        LocalDateTime ahora = LocalDateTime.now();

        Bloqueo bloqueo = new Bloqueo.Builder()
                .idBloqueo(10L)
                .usuarioBloqueador(bloqueador)
                .usuarioBloqueado(bloqueado)
                .fechaBloqueo(ahora)
                .build();

        assertNotNull(bloqueo);
        assertEquals(10L, bloqueo.getIdBloqueo());
        assertEquals(bloqueador, bloqueo.getUsuarioBloqueador());
        assertEquals(bloqueado, bloqueo.getUsuarioBloqueado());
        assertEquals(ahora, bloqueo.getFechaBloqueo());
    }

    @Test
    public void testBuilderFechaPorDefecto() {
        Bloqueo bloqueo = new Bloqueo.Builder()
                .usuarioBloqueador(bloqueador)
                .usuarioBloqueado(bloqueado)
                .build();

        assertNotNull(bloqueo.getFechaBloqueo());
    }

    @Test
    public void testSetters() {
        Bloqueo bloqueo = new Bloqueo.Builder()
                .usuarioBloqueador(bloqueador)
                .usuarioBloqueado(bloqueado)
                .build();

        Usuario nuevoBloqueador =new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
        Usuario nuevoBloqueado  = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();

        bloqueo.setIdBloqueo(55L);
        bloqueo.setUsuarioBloqueador(nuevoBloqueador);
        bloqueo.setUsuarioBloqueado(nuevoBloqueado);

        LocalDateTime nuevaFecha = LocalDateTime.now();
        bloqueo.setFechaBloqueo(nuevaFecha);

        assertEquals(55L, bloqueo.getIdBloqueo());
        assertEquals(nuevoBloqueador, bloqueo.getUsuarioBloqueador());
        assertEquals(nuevoBloqueado, bloqueo.getUsuarioBloqueado());
        assertEquals(nuevaFecha, bloqueo.getFechaBloqueo());
    }

    @Test
    public void testIdBloqueoSetGet() {
        Bloqueo bloqueo = new Bloqueo.Builder()
                .usuarioBloqueador(bloqueador)
                .usuarioBloqueado(bloqueado)
                .build();

        bloqueo.setIdBloqueo(500L);

        assertEquals(500L, bloqueo.getIdBloqueo());
    }
}
