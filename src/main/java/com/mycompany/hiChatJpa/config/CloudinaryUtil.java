package com.mycompany.hiChatJpa.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryUtil {

    private static CloudinaryUtil instance;
    private final Cloudinary cloudinary;

    private static final String FOLDER_PERFIL = "hichat/perfiles";
    private static final String FOLDER_FOTOS = "hichat/fotos";
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final String URL_FOTO_DEFAULT = "https://res.cloudinary.com/dyhw48sho/image/upload/v1765325366/default-picture.png";
    private static final String PUBLIC_ID_FOTO_DEFAULT = "hichat/perfiles/default-user";

    private CloudinaryUtil() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");

            if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
                throw new IllegalStateException("CLOUDINARY_URL no está configurada");
            }

            this.cloudinary = new Cloudinary(cloudinaryUrl);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo conectar con Cloudinary", e);
        }
    }

    public static synchronized CloudinaryUtil getInstance() {
        if (instance == null) {
            instance = new CloudinaryUtil();
        }
        return instance;
    }

    public String obtenerUrlFotoDefault() {
        if (existeImagen(PUBLIC_ID_FOTO_DEFAULT)) {
            return URL_FOTO_DEFAULT;
        }
        
        try {
            return subirImagenDesdeRecurso(
                    "/icons/default-picture.png",
                    "default-user",
                    FOLDER_PERFIL
            );
        } catch (IOException e) {
            return "/icons/default-picture.png";
        }
    }

    public String subirImagenDesdeStream(InputStream inputStream, String publicId, String carpeta) throws IOException {
        try {
            Map params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", carpeta,
                    "use_filename", false,
                    "unique_filename", true,
                    "overwrite", false,
                    "resource_type", "image"
            );

            Map resultado = cloudinary.uploader().upload(inputStream, params);
            String url = (String) resultado.get("secure_url");
            return url;
        } catch (IOException e) {
            throw new IOException("No se pudo subir la imagen: " + e.getMessage(), e);
        }
    }

    public String subirImagenDesdeRecurso(String rutaRecurso, String publicId, String carpeta) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(rutaRecurso);
        if (inputStream == null) {
            throw new IOException("No se encontró el recurso: " + rutaRecurso);
        }
        try {
            return subirImagenDesdeStream(inputStream, publicId, carpeta);
        } finally {
            inputStream.close();
        }
    }

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

    public String subirImagen(File archivo, String publicId, String carpeta) throws IOException {
        return subirImagen(archivo.getAbsolutePath(), publicId, carpeta);
    }

    public String subirFotoPerfil(String rutaArchivo, Long idUsuario) throws IOException {
        String publicId = "usuario_" + idUsuario;
        return subirImagen(rutaArchivo, publicId, FOLDER_PERFIL);
    }

    public String subirFotoGaleria(String rutaArchivo, Long idUsuario, Long idFoto) throws IOException {
        String publicId = "usuario_" + idUsuario + "_foto_" + idFoto;
        return subirImagen(rutaArchivo, publicId, FOLDER_FOTOS);
    }

    public String actualizarImagen(String rutaArchivo, String publicId, String carpeta) throws IOException {
        validarArchivo(new File(rutaArchivo));

        try {
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

    public boolean eliminarFotoPerfil(Long idUsuario) throws IOException {
        String publicId = FOLDER_PERFIL + "/usuario_" + idUsuario;
        return eliminarImagen(publicId);
    }

    public boolean eliminarFotoGaleria(Long idUsuario, Long idFoto) throws IOException {
        String publicId = FOLDER_FOTOS + "/usuario_" + idUsuario + "_foto_" + idFoto;
        return eliminarImagen(publicId);
    }

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

    public String generarThumbnail(String publicId) {
        return generarUrlTransformada(publicId, 150, 150);
    }

    public boolean existeImagen(String publicId) {
        try {
            cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public static String getFolderPerfil() {
        return FOLDER_PERFIL;
    }

    public static String getFolderFotos() {
        return FOLDER_FOTOS;
    }

    public static String getUrlFotoDefault() {
        return URL_FOTO_DEFAULT;
    }
}
