/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.hiChatJpa.entitys.Genero;
import com.mycompany.hiChatJpa.entitys.Interaccion;
import com.mycompany.hiChatJpa.entitys.TipoInteraccion;
import com.mycompany.hiChatJpa.entitys.Usuario;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author saidr
 */

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InteraccionTest {

    // --- Implementación mínima para pruebas ---
    class InteraccionDAOFalso {
        List<Interaccion> lista = new ArrayList<>();

        void insertar(Interaccion i) { lista.add(i); }

        Interaccion buscar(Long id) {
            return lista.stream()
                    .filter(x -> id.equals(x.getIdInteraccion()))
                    .findFirst().orElse(null);
        }

        void actualizar(Interaccion i) { /* vacio */ }

        void eliminar(Long id) { lista.removeIf(x -> x.getIdInteraccion().equals(id)); }

        List<Interaccion> listar() { return lista; }

        List<Interaccion> buscarPorEmisor(Usuario u) {
            return lista.stream()
                    .filter(x -> x.getUsuarioEmisor().equals(u))
                    .toList();
        }

        List<Interaccion> buscarPorReceptor(Usuario u) {
            return lista.stream()
                    .filter(x -> x.getUsuarioReceptor().equals(u))
                    .toList();
        }

        List<Interaccion> buscarPorTipo(TipoInteraccion t) {
            return lista.stream()
                    .filter(x -> x.getTipo().equals(t))
                    .toList();
        }
    }

    // -------- SERVICE FALSO PARA TESTEAR --------
    class InteraccionServiceFalso {

        InteraccionDAOFalso dao = new InteraccionDAOFalso();

        // Métodos reales copiados (un poco simplificados para test)
        public void registrarInteraccion(Interaccion interaccion) throws Exception {
            if (interaccion == null) throw new Exception("La interacción no puede ser nula.");
            if (interaccion.getUsuarioEmisor() == null || interaccion.getUsuarioReceptor() == null)
                throw new Exception("Debe especificar los usuarios emisor y receptor.");
            if (interaccion.getUsuarioEmisor().equals(interaccion.getUsuarioReceptor()))
                throw new Exception("Un usuario no puede interactuar consigo mismo.");
            if (interaccion.getTipo() == null)
                throw new Exception("Debe especificar el tipo de interacción.");

            List<Interaccion> previas = dao.buscarPorEmisor(interaccion.getUsuarioEmisor());
            boolean duplicada = previas.stream().anyMatch(item ->
                    item.getUsuarioReceptor().equals(interaccion.getUsuarioReceptor()) &&
                            item.getTipo().equals(interaccion.getTipo())
            );
            if (duplicada) throw new Exception("Ya existe una interacción de este tipo entre los mismos usuarios.");

            dao.insertar(interaccion);
        }

        public Boolean darLike(Long idUsuario) throws Exception {
            if (idUsuario == null || idUsuario <= 0)
                throw new Exception("ID de usuario inválido para dar like.");
            return true;
        }

        public Boolean darDislike(Long idUsuario) throws Exception {
            if (idUsuario == null || idUsuario <= 0)
                throw new Exception("ID de usuario inválido para dar dislike.");
            return true;
        }

        public Boolean darSuperLike(Long idUsuario) throws Exception {
            if (idUsuario == null || idUsuario <= 0)
                throw new Exception("ID de usuario inválido para dar super like.");
            return true;
        }

        public Boolean bloquearUsuario(Long idUsuario) throws Exception {
            if (idUsuario == null || idUsuario <= 0)
                throw new Exception("ID de usuario inválido para bloquear.");
            return true;
        }

        public Boolean desbloquearUsuario(Long idUsuario) throws Exception {
            if (idUsuario == null || idUsuario <= 0)
                throw new Exception("ID de usuario inválido para desbloquear.");
            return true;
        }
    }

    // ============================================================
    //                         TESTS
    // ============================================================

    @Test
    public void testRegistrarInteraccionExitosa() throws Exception {
        InteraccionServiceFalso srv = new InteraccionServiceFalso();

    Usuario usuario1 = new Usuario.Builder()
        .nombre("Said")
        .apellidoPaterno("Ramírez")
        .apellidoMaterno("Lopez")
        .correoElectronico("said.ramirez@example.com")
        .contrasena("pass123")
        .carrera("Ingeniería en Sistemas")
        .biografia("Me encanta programar y aprender nuevas tecnologías.")
        .urlFotoPerfil("https://miservidor.com/fotos/said.jpg")
        .genero(Genero.MASCULINO)
        .fechaNacimiento(LocalDate.of(2001, 4, 20))
        .build();
    
    Usuario usuario2 = new Usuario.Builder()
        .nombre("María")
        .apellidoPaterno("Hernández")
        .apellidoMaterno("Gómez")
        .correoElectronico("maria.hg@example.com")
        .contrasena("maria2024")
        .carrera("Arquitectura")
        .biografia("Amante del arte, la fotografía y el diseño urbano.")
        .urlFotoPerfil("https://miservidor.com/fotos/maria.jpg")
        .genero(Genero.FEMENINO)
        .fechaNacimiento(LocalDate.of(1999, 11, 3))
        .build();


        



  

Interaccion inter = new Interaccion.Builder()
        .usuarioEmisor(usuario1)
        .usuarioReceptor(usuario2)
        .tipo(TipoInteraccion.ME_GUSTA)
        .build();


        assertDoesNotThrow(() -> srv.registrarInteraccion(inter));
    }

   @Test
public void testRegistrarInteraccionDuplicada() throws Exception {
    InteraccionServiceFalso srv = new InteraccionServiceFalso();

    // Usuarios con IDs
    Usuario u1 = new Usuario.Builder()
            .nombre("Abdiel")
            .apellidoPaterno("Bargas")
            .correoElectronico("abdiel@booking.com")
            .contrasena("12ert3")
            .build();
    u1.setIdUsuario(1L);

    Usuario u2 = new Usuario.Builder()
            .nombre("Carlos")
            .apellidoPaterno("Duarte")
            .correoElectronico("carlos@gmail.com")
            .contrasena("passs456")
            .build();
    u2.setIdUsuario(2L);

    // Primera interacción (válida)
    Interaccion inter1 = new Interaccion.Builder()
            .usuarioEmisor(u1)
            .usuarioReceptor(u2)
            .tipo(TipoInteraccion.ME_GUSTA)
            .build();

    srv.registrarInteraccion(inter1);

    // Segunda interacción duplicada → debe lanzar excepción
    Interaccion inter2 = new Interaccion.Builder()
            .usuarioEmisor(u1)
            .usuarioReceptor(u2)
            .tipo(TipoInteraccion.ME_GUSTA)
            .build();

    assertThrows(Exception.class, () -> srv.registrarInteraccion(inter2));
}


    @Test
    public void testDarLikeValido() throws Exception {
        InteraccionServiceFalso srv = new InteraccionServiceFalso();
        assertTrue(srv.darLike(5L));
    }

    @Test
    public void testDarLikeInvalido() {
        InteraccionServiceFalso srv = new InteraccionServiceFalso();
        assertThrows(Exception.class, () -> srv.darLike(0L));
    }

    @Test
    public void testDarDislikeValido() throws Exception {
        InteraccionServiceFalso srv = new InteraccionServiceFalso();
        assertTrue(srv.darDislike(3L));
    }

    @Test
    public void testDarSuperLikeValido() throws Exception {
        InteraccionServiceFalso srv = new InteraccionServiceFalso();
        assertTrue(srv.darSuperLike(10L));
    }

    @Test
    public void testBloquearValido() throws Exception {
        InteraccionServiceFalso srv = new InteraccionServiceFalso();
        assertTrue(srv.bloquearUsuario(2L));
    }

    @Test
    public void testDesbloquearInvalido() {
        InteraccionServiceFalso srv = new InteraccionServiceFalso();
        assertThrows(Exception.class, () -> srv.desbloquearUsuario(-1L));
    }
}
