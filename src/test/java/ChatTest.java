/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.hiChatJpa.dao.IChatDAO;
import com.mycompany.hiChatJpa.entitys.Chat;
import com.mycompany.hiChatJpa.entitys.Usuario;
import com.mycompany.hiChatJpa.service.impl.ChatService;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ChatTest {

   private ChatService servicio;
    private IChatDAO daoFake;

    @BeforeEach
    public void setUp() throws Exception {

        // DAO falso en memoria
        daoFake = new IChatDAO() {

            private final List<Chat> datos = new ArrayList<>();

            @Override
            public void insertar(Chat chat) {
                datos.add(chat);
            }

            @Override
            public void actualizar(Chat chat) {
                for (int i = 0; i < datos.size(); i++) {
                    if (datos.get(i).getIdChat().equals(chat.getIdChat())) {
                        datos.set(i, chat);
                    }
                }
            }

            @Override
            public void eliminar(Long id) {
                datos.removeIf(c -> c.getIdChat().equals(id));
            }

            @Override
            public Chat buscar(Long id) {
                return datos.stream()
                        .filter(c -> c.getIdChat().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public List<Chat> listar() {
                return new ArrayList<>(datos);
            }

            @Override
            public List<Chat> buscarPorNombre(String nombre) {
                List<Chat> r = new ArrayList<>();
                for (Chat c : datos) {
                    if (c.getNombre().equalsIgnoreCase(nombre)) {
                        r.add(c);
                    }
                }
                return r;
            }

            @Override
            public List<Chat> buscarPorParticipante(Usuario usuario) {
                List<Chat> r = new ArrayList<>();
                for (Chat c : datos) {
                    if (c.getParticipantes().contains(usuario)) {
                        r.add(c);
                    }
                }
                return r;
            }
        };

        // Crear ChatService REAL
        servicio = new ChatService();

        // Inyectar daoFake usando REFLEXIÓN
        Field campo = ChatService.class.getDeclaredField("chatDAO");
        campo.setAccessible(true);
        campo.set(servicio, daoFake);
    }

    // ============================================
    // TESTS
    // ============================================

    @Test
    public void testRegistrarChatCorrecto() throws Exception {
        Chat c =  new Chat.Builder()
                .idChat(1L)                
                .nombre("XD")   
                .build();  
        c.setIdChat(1L);
        c.setNombre("Chat Amistad");

        Usuario u = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
        u.setIdUsuario(10L);

        c.setParticipantes(Set.of(u));

        servicio.registrarChat(c);

        assertEquals(1, daoFake.listar().size());
    }

    @Test
    public void testRegistrarChatDuplicado() throws Exception {
        Chat c1 =  new Chat.Builder()
                .idChat(1L)              
                .nombre("Chat de prueba")
                .build();  

        Chat c2 =  new Chat.Builder()
                .idChat(1L)           
                .nombre("Chat de prueba")   
                .build();  

        servicio.registrarChat(c1);

        assertThrows(Exception.class, () -> servicio.registrarChat(c2));
    }

    @Test
    public void testActualizarChatNoExistente() {
        Chat c =  new Chat.Builder()
                .idChat(1L)                
                .nombre("Chat de prueba")   
                .build();  

        assertThrows(Exception.class, () -> servicio.actualizarChat(c));
    }

    @Test
    public void testEliminarChatCorrecto() throws Exception {
        Chat c =  new Chat.Builder()
                .idChat(1L)              
                .nombre("Chat de prueba")   
                .build();  

        daoFake.insertar(c);

        servicio.eliminarChat(8L);

        assertEquals(0, daoFake.listar().size());
    }

    @Test
    public void testBuscarPorIdInvalido() {
        assertNull(servicio.buscarPorId(null));
        assertNull(servicio.buscarPorId(0L));
        assertNull(servicio.buscarPorId(-2L));
    }

    @Test
    public void testListarPorNombre() throws Exception {
        Chat c =  new Chat.Builder()
                .idChat(1L)                
                .nombre("Chat ")   
                .build();  

        daoFake.insertar(c);

        List<Chat> resultado = servicio.listarPorNombre("Fiesta");

        assertEquals(1, resultado.size());
    }
}

