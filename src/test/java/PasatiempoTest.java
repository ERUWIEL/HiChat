/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import com.mycompany.hiChatJpa.service.IPasatiempoService;

import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import com.mycompany.hiChatJpa.service.impl.PasatiempoService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */
public class PasatiempoTest {
    
@Test
public void testRegistrarPasatiempo_Nulo() {
    PasatiempoService service = new PasatiempoService();

    assertThrows(Exception.class, () -> {
        service.registrarPasatiempo(null);
    });
}


    @Test
    public void testRegistrarPasatiempo_SinNombre() {
        PasatiempoService service = new PasatiempoService();

        Pasatiempo p = new Pasatiempo.Builder()
                .nombre("")
                .build();

        assertThrows(Exception.class, () -> {
            service.registrarPasatiempo(p);
        });
    }

    @Test
    public void testActualizarPasatiempo_Nulo() {
        PasatiempoService service = new PasatiempoService();

        assertThrows(Exception.class, () -> {
            service.actualizarPasatiempo(null);
        });
    }

    @Test
    public void testActualizarPasatiempo_SinId() {
        PasatiempoService service = new PasatiempoService();

        Pasatiempo p = new Pasatiempo.Builder()
                .nombre("Cantar")
                .build();

        assertThrows(Exception.class, () -> {
            service.actualizarPasatiempo(p);
        });
    }

    @Test
    public void testEliminarPasatiempo_IdInvalido() {
        PasatiempoService service = new PasatiempoService();

        assertThrows(Exception.class, () -> {
            service.eliminarPasatiempo(0L);
        });
    }

    @Test
    public void testBuscarPorId_Invalido() {
        PasatiempoService service = new PasatiempoService();

        assertNull(service.buscarPorId(null));
        assertNull(service.buscarPorId(0L));
    }

    @Test
    public void testBuscarPorNombre_Nulo() {
        PasatiempoService service = new PasatiempoService();

        assertNull(service.buscarPorNombre(null));
    }

    @Test
    public void testBuscarPorNombre_Vacio() {
        PasatiempoService service = new PasatiempoService();

        assertNull(service.buscarPorNombre(""));
    }

    @Test
    public void testListarPasatiempos_Limite() {
        PasatiempoService service = new PasatiempoService();

        // si la lista es mayor a 100, debe regresar null
        assertNull(service.listarPasatiempos());
    }
}
