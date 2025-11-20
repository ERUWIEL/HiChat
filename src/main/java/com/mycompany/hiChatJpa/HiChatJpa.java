package com.mycompany.hiChatJpa;

import com.mycompany.hiChatJpa.config.JpaUtil;
import com.mycompany.hiChatJpa.dto.RegistroDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import com.mycompany.hiChatJpa.service.impl.InteraccionService;
import com.mycompany.hiChatJpa.service.impl.UsuarioService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal para cargar datos de prueba
 * @author Laboratorios
 */
public class HiChatJpa {

    private static UsuarioService usuarioService = new UsuarioService();
    private static InteraccionService interaccionService = new InteraccionService();

    public static void main(String[] args) {
        try {
            System.out.println("========== INICIANDO CARGA DE DATOS ==========\n");

            // 1. Verificar si ya hay usuarios
            List<UsuarioPerfilDTO> usuariosExistentes = usuarioService.listarUsuarios();
            List<UsuarioPerfilDTO> estudiantes;

            if (usuariosExistentes != null && !usuariosExistentes.isEmpty()) {
                System.out.println(" Ya existen " + usuariosExistentes.size() + " usuarios en la BD");
                System.out.println("¿Deseas usar los existentes? (Presiona Enter para continuar)\n");
                estudiantes = usuariosExistentes;
                
                // Si quieres limpiar todo, descomenta estas líneas:
                // System.out.println("Limpiando datos existentes...");
                // limpiarDatos();
                // estudiantes = agregarEstudiantes();
            } else {
                // 2. Agregar 50 estudiantes
                estudiantes = agregarEstudiantes();
                System.out.println("\n 50 estudiantes agregados correctamente\n");
            }

            // 3. Agregar "me gusta" de forma inteligente
            int likesAgregados = agregarMeGustas(estudiantes);
            System.out.println("\n " + likesAgregados + " 'Me Gusta' agregados correctamente\n");

            System.out.println("========== CARGA DE DATOS COMPLETADA ==========");

        } catch (Exception e) {
            System.err.println(" Error durante la carga de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // IMPORTANTE: Cerrar recursos
            JpaUtil.shutdown();
        }
    }

    /**
     * Método que agrega 50 estudiantes
     */
    private static List<UsuarioPerfilDTO> agregarEstudiantes() throws Exception {
        List<UsuarioPerfilDTO> estudiantes = new ArrayList<>();

        String[] nombres = {
            "Juan", "María", "Carlos", "Ana", "Luis",
            "Pedro", "Sofia", "Diego", "Laura", "Miguel",
            "Elena", "Jorge", "Patricia", "Roberto", "Francisca",
            "Antonio", "Gabriela", "Manuel", "Andrea", "Rafael",
            "Camila", "Fernando", "Valentina", "Enrique", "Isabella",
            "Alejandro", "Martina", "Felipe", "Catalina", "Rodrigo",
            "Daniela", "Andrés", "Florencia", "Gustavo", "Marcela",
            "Raúl", "Constanza", "Oscar", "Lorena", "Héctor",
            "Natalia", "Víctor", "Verónica", "Ramón", "Bárbara",
            "Salvador", "Rocío", "Arturo", "Susana", "Ernesto"
        };

        String[] apellidos = {
            "García", "Martínez", "López", "González", "Rodríguez",
            "Hernández", "Pérez", "Sánchez", "Reyes", "Flores"
        };

        String[] carreras = {
            "Ingeniería en Sistemas", "Administración de Empresas",
            "Derecho", "Medicina", "Enfermería"
        };

        System.out.println("--- Agregando 50 Estudiantes ---\n");

        for (int i = 0; i < 50; i++) {
            String nombre = nombres[i];
            String apellidoPaterno = apellidos[i % apellidos.length];
            String apellidoMaterno = apellidos[(i + 1) % apellidos.length];
            String carrera = carreras[i % carreras.length];
            String genero = (i % 2 == 0) ? "MASCULINO" : "FEMENINO";

            RegistroDTO registroDTO = new RegistroDTO(
                    nombre,
                    apellidoPaterno,
                    apellidoMaterno,
                    "estudiante" + i + "@uni.edu",
                    "Pass" + i + "123!",
                    LocalDate.of(2000, 1, 1).plusDays(i * 5),
                    carrera,
                    genero
            );

            try {
                usuarioService.registrarUsuario(registroDTO);
                
                // Buscar el usuario recién insertado para obtener su ID
                UsuarioPerfilDTO usuarioInsertado = usuarioService.buscarPorCorreo(registroDTO.getCorreoElectronico());
                estudiantes.add(usuarioInsertado);

                System.out.println((i + 1) + ". " + nombre + " " + apellidoPaterno
                        + " - " + carrera + " (ID: " + usuarioInsertado.getIdUsuario() + ")");
            } catch (Exception e) {
                System.err.println("    Error al registrar " + nombre + ": " + e.getMessage());
            }
        }

        return estudiantes;
    }

    /**
     * Método mejorado que agrega exactamente 100 "me gusta" sin duplicados
     */
    private static int agregarMeGustas(List<UsuarioPerfilDTO> estudiantes) throws Exception {
        System.out.println("--- Agregando 'Me Gusta' ---\n");

        int contador = 0;
        int matchesCreados = 0;
        int intentos = 0;
        int maxIntentos = 300; // Límite de seguridad para evitar bucles infinitos

        // Estrategia: distribuir los likes entre todos los usuarios
        // Cada estudiante dará likes hasta completar 100
        for (int i = 0; i < estudiantes.size() && contador < 99; i++) {
            UsuarioPerfilDTO emisor = estudiantes.get(i);
            Long idEmisor = emisor.getIdUsuario();
            int offset = 1;

            // Intentar dar likes a diferentes usuarios
            while (contador < 99 && offset < estudiantes.size()) {
                intentos++;
                if (intentos >= maxIntentos) break;

                int receptorIdx = (i + offset) % estudiantes.size();

                // Evitar que sea él mismo
                if (receptorIdx != i) {
                    UsuarioPerfilDTO receptor = estudiantes.get(receptorIdx);
                    Long idReceptor = receptor.getIdUsuario();

                    try {
                        boolean matchCreado = interaccionService.darLike(idEmisor, idReceptor);

                        contador++;

                        String matchInfo = matchCreado ? " [ MATCH CREADO!]" : "";
                        System.out.println(contador + ". " + emisor.getNombre() + " (ID:" 
                                + idEmisor + ") → " + receptor.getNombre() + " (ID:" 
                                + idReceptor + ")" + matchInfo);

                        if (matchCreado) {
                            matchesCreados++;
                        }

                    } catch (Exception e) {
                        // Si ya existe el like, simplemente pasamos al siguiente
                        if (e.getMessage().contains("Ya habías dado like")) {
                            System.out.println("    Like ya existente, omitiendo...");
                        } else {
                            System.out.println("    Error: " + e.getMessage());
                        }
                    }
                }
                offset++;
            }
        }

        // LIKE ESPECIAL #100: Usuario 50 → Usuario 1
        System.out.println("\n--- Like Especial Final ---");
        if (estudiantes.size() >= 50) {
            UsuarioPerfilDTO usuario50 = estudiantes.get(49); // Índice 49 = usuario 50
            UsuarioPerfilDTO usuario1 = estudiantes.get(0);   // Índice 0 = usuario 1
            
            try {
                boolean matchCreado = interaccionService.darLike(
                    usuario50.getIdUsuario(), 
                    usuario1.getIdUsuario()
                );

                contador++;
                
                String matchInfo = matchCreado ? " [ MATCH CREADO!]" : "";
                System.out.println("100. " + usuario50.getNombre() + " (ID:" 
                        + usuario50.getIdUsuario() + ")  " + usuario1.getNombre() 
                        + " (ID:" + usuario1.getIdUsuario() + ")" + matchInfo 
                        + " [LIKE FINAL ESPECIAL]");

                if (matchCreado) {
                    matchesCreados++;
                }

            } catch (Exception e) {
                System.out.println("   Error en like final: " + e.getMessage());
            }
        } else {
            System.out.println(" No hay suficientes usuarios para el like final");
        }

        System.out.println("\n Resumen:");
        System.out.println("   - Likes agregados: " + contador);
        System.out.println("   - Matches creados: " + matchesCreados);
        System.out.println("   - Intentos totales: " + intentos);

        if (intentos >= maxIntentos) {
            System.out.println("\n Advertencia: Se alcanzó el límite de intentos");
        }

        return contador;
    }
}