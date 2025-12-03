package com.mycompany.hiChatJpa.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Clase de utilidad para manejar operaciones con Cloudinary
 * Implementa el patrón Singleton para una única instancia de conexión
 * 
 * @author gatog
 */
public class CloudinaryUtil {
    
    private static CloudinaryUtil instance;
    private final Cloudinary cloudinary;

    // Constantes de configuración
    private static final String FOLDER_PERFIL = "hichat/perfiles";
    private static final String FOLDER_FOTOS = "hichat/fotos";
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    /**
     * Constructor privado para el patrón Singleton
     */
    private CloudinaryUtil() {
        try {
            // Cargar variables de entorno desde archivo .env
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");
            
            if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
                throw new IllegalStateException("CLOUDINARY_URL no está configurada en el archivo .env");
            }

            this.cloudinary = new Cloudinary(cloudinaryUrl);
            
        } catch (Exception e) {
            throw new RuntimeException("No se pudo conectar con Cloudinary", e);
        }
    }

    /**
     * Obtiene la instancia única de CloudinaryUtil (Singleton)
     * 
     * @return Instancia de CloudinaryUtil
     */
    public static synchronized CloudinaryUtil getInstance() {
        if (instance == null) {
            instance = new CloudinaryUtil();
        }
        return instance;
    }

    /**
     * Sube una imagen a Cloudinary desde una ruta de archivo
     * 
     * @param rutaArchivo Ruta absoluta del archivo
     * @param publicId Identificador único para la imagen (sin extensión)
     * @param carpeta Carpeta donde guardar (usar constantes FOLDER_*)
     * @return URL pública de la imagen subida
     * @throws IOException Si hay error al subir
     */
    public String subirImagen(String rutaArchivo, String publicId, String carpeta) throws IOException {
        validarArchivo(new File(rutaArchivo));

        try {
            Map params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", carpeta,
                    "use_filename", false,
                    "unique_filename", true,
                    "overwrite", false,
                    "resource_type", "image"
            );

            Map resultado = cloudinary.uploader().upload(new File(rutaArchivo), params);
            String url = (String) resultado.get("secure_url");
            
            return url;

        } catch (IOException e) {
            throw new IOException("No se pudo subir la imagen: " + e.getMessage(), e);
        }
    }

    /**
     * Sube una imagen desde un objeto File
     * 
     * @param archivo Archivo de imagen
     * @param publicId Identificador único
     * @param carpeta Carpeta destino
     * @return URL pública de la imagen
     * @throws IOException Si hay error
     */
    public String subirImagen(File archivo, String publicId, String carpeta) throws IOException {
        return subirImagen(archivo.getAbsolutePath(), publicId, carpeta);
    }

    /**
     * Sube una foto de perfil de usuario
     * 
     * @param rutaArchivo Ruta del archivo
     * @param idUsuario ID del usuario (se usa como publicId)
     * @return URL pública de la imagen
     * @throws IOException Si hay error
     */
    public String subirFotoPerfil(String rutaArchivo, Long idUsuario) throws IOException {
        String publicId = "usuario_" + idUsuario;
        return subirImagen(rutaArchivo, publicId, FOLDER_PERFIL);
    }

    /**
     * Sube una foto de galería de usuario
     * 
     * @param rutaArchivo Ruta del archivo
     * @param idUsuario ID del usuario
     * @param idFoto ID de la foto
     * @return URL pública de la imagen
     * @throws IOException Si hay error
     */
    public String subirFotoGaleria(String rutaArchivo, Long idUsuario, Long idFoto) throws IOException {
        String publicId = "usuario_" + idUsuario + "_foto_" + idFoto;
        return subirImagen(rutaArchivo, publicId, FOLDER_FOTOS);
    }

    /**
     * Actualiza una imagen existente (reemplaza)
     * 
     * @param rutaArchivo Nueva ruta del archivo
     * @param publicId ID público de la imagen a reemplazar (sin carpeta)
     * @param carpeta Carpeta donde está la imagen
     * @return URL pública de la nueva imagen
     * @throws IOException Si hay error
     */
    public String actualizarImagen(String rutaArchivo, String publicId, String carpeta) throws IOException {
        validarArchivo(new File(rutaArchivo));

        try {
            // Configurar para sobrescribir
            Map params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", carpeta,
                    "overwrite", true,
                    "resource_type", "image"
            );

            Map resultado = cloudinary.uploader().upload(new File(rutaArchivo), params);
            String url = (String) resultado.get("secure_url");
            
            return url;

        } catch (IOException e) {
            throw new IOException("No se pudo actualizar la imagen: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una imagen de Cloudinary
     * 
     * @param publicId ID público completo (incluyendo carpeta, ej: "hichat/perfiles/usuario_1")
     * @return true si se eliminó correctamente
     * @throws IOException Si hay error
     */
    public boolean eliminarImagen(String publicId) throws IOException {
        try {
            Map params = ObjectUtils.asMap(
                    "resource_type", "image"
            );

            Map resultado = cloudinary.uploader().destroy(publicId, params);
            String result = (String) resultado.get("result");
            
            boolean exitoso = "ok".equals(result);           
            return exitoso;

        } catch (IOException e) {
            throw new IOException("No se pudo eliminar la imagen: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una foto de perfil
     * 
     * @param idUsuario ID del usuario
     * @return true si se eliminó
     * @throws IOException Si hay error
     */
    public boolean eliminarFotoPerfil(Long idUsuario) throws IOException {
        String publicId = FOLDER_PERFIL + "/usuario_" + idUsuario;
        return eliminarImagen(publicId);
    }

    /**
     * Elimina una foto de galería
     * 
     * @param idUsuario ID del usuario
     * @param idFoto ID de la foto
     * @return true si se eliminó
     * @throws IOException Si hay error
     */
    public boolean eliminarFotoGaleria(Long idUsuario, Long idFoto) throws IOException {
        String publicId = FOLDER_FOTOS + "/usuario_" + idUsuario + "_foto_" + idFoto;
        return eliminarImagen(publicId);
    }

    /**
     * Obtiene los detalles de una imagen
     * 
     * @param publicId ID público de la imagen
     * @return Map con los detalles de la imagen
     * @throws Exception Si hay error
     */
    public Map obtenerDetalles(String publicId) throws Exception {
        try {
            Map params = ObjectUtils.asMap(
                    "resource_type", "image",
                    "quality_analysis", true,
                    "colors", true
            );

            Map detalles = cloudinary.api().resource(publicId, params);
            
            return detalles;

        } catch (Exception e) {
            throw new Exception("No se pudieron obtener los detalles de la imagen", e);
        }
    }

    /**
     * Genera una URL transformada de la imagen
     * 
     * @param publicId ID público de la imagen
     * @param ancho Ancho deseado (0 para mantener original)
     * @param alto Alto deseado (0 para mantener original)
     * @return URL con transformación aplicada
     */
    public String generarUrlTransformada(String publicId, int ancho, int alto) {
        try {
            com.cloudinary.Transformation transformacion = new com.cloudinary.Transformation();
            
            if (ancho > 0 && alto > 0) {
                transformacion
                    .width(ancho)
                    .height(alto)
                    .crop("fill")
                    .gravity("face")
                    .quality("auto")
                    .fetchFormat("auto");
            } else if (ancho > 0) {
                transformacion.width(ancho).crop("scale");
            } else if (alto > 0) {
                transformacion.height(alto).crop("scale");
            }

            String url = cloudinary.url()
                    .transformation(transformacion)
                    .generate(publicId);
            
            return url;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Genera URL de thumbnail (miniatura) de 150x150
     * 
     * @param publicId ID público de la imagen
     * @return URL del thumbnail
     */
    public String generarThumbnail(String publicId) {
        return generarUrlTransformada(publicId, 150, 150);
    }

    /**
     * Verifica si una imagen existe en Cloudinary
     * 
     * @param publicId ID público de la imagen
     * @return true si existe
     */
    public boolean existeImagen(String publicId) {
        try {
            cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida que el archivo sea válido
     * 
     * @param archivo Archivo a validar
     * @throws IOException Si el archivo no es válido
     */
    private void validarArchivo(File archivo) throws IOException {
        if (archivo == null || !archivo.exists()) {
            throw new IOException("El archivo no existe");
        }

        if (!archivo.isFile()) {
            throw new IOException("La ruta no corresponde a un archivo");
        }

        if (archivo.length() > MAX_FILE_SIZE) {
            throw new IOException("El archivo excede el tamaño máximo permitido (10MB)");
        }

        String nombre = archivo.getName().toLowerCase();
        if (!nombre.matches(".*\\.(jpg|jpeg|png|gif|webp)$")) {
            throw new IOException("Formato de imagen no soportado. Use: JPG, PNG, GIF o WEBP");
        }
    }

    /**
     * Obtiene la instancia de Cloudinary directa (uso avanzado)
     * 
     * @return Instancia de Cloudinary
     */
    public Cloudinary getCloudinary() {
        return cloudinary;
    }
    
    public static String getFolderPerfil() {
        return FOLDER_PERFIL;
    }

    public static String getFolderFotos() {
        return FOLDER_FOTOS;
    }
}