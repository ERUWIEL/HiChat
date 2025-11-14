/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Mensaje;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.impl.MensajeService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */
public class MensajeTest {
    
 @Test
    public void testRegistrarMensaje_Nulo() {
        MensajeService service = new MensajeService();
        assertThrows(Exception.class, () -> {
            service.registrarMensaje(null);
        });
    }

    @Test
    public void testRegistrarMensaje_SinChat() {
        MensajeService service = new MensajeService();

        Usuario emisor = new Usuario.Builder()
                .nombre("Abdiel")
                .apellidoPaterno("Bargas")
                .correoElectronico("abdiel@booking.com")
                .contrasena("12345")
                .build();

        Mensaje mensaje = new Mensaje.Builder()
                .usuarioEmisor(emisor)
                .contenido("Hola")
                .build();

        assertThrows(Exception.class, () -> {
            service.registrarMensaje(mensaje);
        });
    }

    @Test
    public void testRegistrarMensaje_SinEmisor() {
        MensajeService service = new MensajeService();

        Chat chat = new Chat.Builder()
                .idChat(1L)                
                .nombre("XD")   
                .build();  
        Mensaje mensaje = new Mensaje.Builder()
                .chat(chat)
                .contenido("Hola")
                .build();

        assertThrows(Exception.class, () -> {
            service.registrarMensaje(mensaje);
        });
    }

    @Test
    public void testRegistrarMensaje_Vacio() {
        MensajeService service = new MensajeService();

        Chat chat = new Chat.Builder()
                .idChat(1L)                
                .nombre("XD")   
                .build();  
        Usuario emisor = new Usuario.Builder()
                .nombre("Diana")
                .apellidoPaterno("Lopez")
                .correoElectronico("diana@mail.com")
                .contrasena("abc123")
                .build();

        Mensaje mensaje = new Mensaje.Builder()
                .chat(chat)
                .usuarioEmisor(emisor)
                .contenido("")
                .build();

        assertThrows(Exception.class, () -> {
            service.registrarMensaje(mensaje);
        });
    }

    @Test
    public void testActualizarMensaje_Nulo() {
        MensajeService service = new MensajeService();

        assertThrows(Exception.class, () -> {
            service.actualizarMensaje(null);
        });
    }

    @Test
    public void testActualizarMensaje_SinId() {
        MensajeService service = new MensajeService();
        Chat chat = new Chat.Builder()
                .idChat(1L)                
                .nombre("XD")   
                .build();  

        Usuario emisor = new Usuario.Builder()
                .nombre("Pedro")
                .apellidoPaterno("Perez")
                .correoElectronico("pedro@mail.com")
                .contrasena("1234")
                .build();

        Mensaje mensaje = new Mensaje.Builder()
                .chat(chat)
                .usuarioEmisor(emisor)
                .contenido("Hola")
                .build();

        assertThrows(Exception.class, () -> {
            service.actualizarMensaje(mensaje);
        });
    }

    @Test
    public void testEliminarMensaje_IdInvalido() {
        MensajeService service = new MensajeService();

        assertThrows(Exception.class, () -> {
            service.eliminarMensaje(0L);
        });
    }

    @Test
    public void testBuscarPorId_Invalido() {
        MensajeService service = new MensajeService();

        assertNull(service.buscarPorId(0L));
        assertNull(service.buscarPorId(null));
    }

    @Test
    public void testListarPorChat_Nulo() {
        MensajeService service = new MensajeService();

        assertNull(service.listarPorChat(null));
    }

    @Test
    public void testListarNoVistosPorUsuario_Nulo() {
        MensajeService service = new MensajeService();

        assertNull(service.listarNoVistosPorUsuario(null));
    }
}
