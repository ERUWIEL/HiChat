package com.mycompany.hiChatJpa.service.impl;

import com.mycompany.hiChatJpa.config.CloudinaryUtil;
import com.mycompany.hiChatJpa.repository.impl.FotoRepository;
import com.mycompany.hiChatJpa.repository.impl.UsuarioRepository;
import com.mycompany.hiChatJpa.entitys.Foto;
import com.mycompany.hiChatJpa.entitys.Usuario;

import java.io.File;
import java.util.List;
import com.mycompany.hiChatJpa.repository.IUsuarioRepository;
import com.mycompany.hiChatJpa.repository.IFotoRepository;

/**
 * Servicio para manejar fotos con integración de Cloudinary
 * 
 * @author gatog
 */
public class FotoService {

    private final IFotoRepository fotoDAO;
    private final IUsuarioRepository usuarioDAO;
    private final CloudinaryUtil cloudinary;

    public FotoService() {
        this.fotoDAO = new FotoRepository();
        this.usuarioDAO = new UsuarioRepository();
        this.cloudinary = CloudinaryUtil.getInstance();
    }

    /**
     * Agrega una nueva foto de galería para un usuario
     * Sube la imagen a Cloudinary y guarda la referencia en la BD
     * 
     * @param rutaArchivo Ruta del archivo de imagen
     * @param idUsuario ID del usuario
     * @param descripcion Descripción de la foto
     * @return Foto guardada con URL de Cloudinary
     * @throws Exception Si hay error
     */
    public Foto agregarFotoGaleria(String rutaArchivo, Long idUsuario, String descripcion) throws Exception {
        // Validaciones
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new Exception("La ruta del archivo es obligatoria");
        }

        if (idUsuario == null || idUsuario <= 0) {
            throw new Exception("ID de usuario inválido");
        }

        // Verificar que el usuario existe
        Usuario usuario = usuarioDAO.buscar(idUsuario);
        if (usuario == null) {
            throw new Exception("El usuario no existe");
        }

        // Validar archivo
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            throw new Exception("El archivo no existe");
        }

        try {
            // 1. Crear el registro de la foto en BD (para obtener el ID)
            Foto foto = new Foto.Builder()
                    .usuario(usuario)
                    .urlFoto("temporal") // Temporal hasta subir a Cloudinary
                    .descripcion(descripcion)
                    .build();

            fotoDAO.insertar(foto);

            // 2. Subir imagen a Cloudinary usando el ID de la foto
            String urlCloudinary = cloudinary.subirFotoGaleria(
                    rutaArchivo, 
                    idUsuario, 
                    foto.getIdFoto()
            );

            // 3. Actualizar la URL en la base de datos
            foto.setUrlFoto(urlCloudinary);
            fotoDAO.actualizar(foto);
            return foto;

        } catch (Exception e) {
            throw new Exception("No se pudo guardar la foto: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la foto de perfil de un usuario
     * 
     * @param rutaArchivo Ruta del nuevo archivo
     * @param idUsuario ID del usuario
     * @return URL de la foto actualizada
     * @throws Exception Si hay error
     */
    public String actualizarFotoPerfil(String rutaArchivo, Long idUsuario) throws Exception {
        // Validaciones
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new Exception("La ruta del archivo es obligatoria");
        }

        if (idUsuario == null || idUsuario <= 0) {
            throw new Exception("ID de usuario inválido");
        }

        // Verificar que el usuario existe
        Usuario usuario = usuarioDAO.buscar(idUsuario);
        if (usuario == null) {
            throw new Exception("El usuario no existe");
        }

        try {
            // Subir/actualizar imagen a Cloudinary
            String urlCloudinary = cloudinary.subirFotoPerfil(rutaArchivo, idUsuario);

            // Actualizar URL en el usuario
            usuario.setUrlFotoPerfil(urlCloudinary);
            usuarioDAO.actualizar(usuario);
            return urlCloudinary;

        } catch (Exception e) {
            throw new Exception("No se pudo actualizar la foto de perfil: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una foto de galería
     * Elimina tanto de Cloudinary como de la BD
     * 
     * @param idFoto ID de la foto a eliminar
     * @throws Exception Si hay error
     */
    public void eliminarFotoGaleria(Long idFoto) throws Exception {
        if (idFoto == null || idFoto <= 0) {
            throw new Exception("ID de foto inválido");
        }

        // Buscar la foto
        Foto foto = fotoDAO.buscar(idFoto);
        if (foto == null) {
            throw new Exception("La foto no existe");
        }

        try {
            // 1. Eliminar de Cloudinary
            Long idUsuario = foto.getUsuario().getIdUsuario();
            boolean eliminadaCloudinary = cloudinary.eliminarFotoGaleria(idUsuario, idFoto);

            // 2. Eliminar de la base de datos
            fotoDAO.eliminar(idFoto);

        } catch (Exception e) {
            throw new Exception("No se pudo eliminar la foto: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todas las fotos de un usuario
     * 
     * @param idUsuario ID del usuario
     * @return Lista de fotos
     * @throws Exception Si hay error
     */
    public List<Foto> obtenerFotosDeUsuario(Long idUsuario) throws Exception {
        if (idUsuario == null || idUsuario <= 0) {
            throw new Exception("ID de usuario inválido");
        }

        Usuario usuario = usuarioDAO.buscar(idUsuario);
        if (usuario == null) {
            throw new Exception("El usuario no existe");
        }

        return fotoDAO.buscarPorUsuario(usuario);
    }

    /**
     * Genera thumbnail de una foto
     * 
     * @param idFoto ID de la foto
     * @return URL del thumbnail
     * @throws Exception Si hay error
     */
    public String generarThumbnail(Long idFoto) throws Exception {
        Foto foto = fotoDAO.buscar(idFoto);
        if (foto == null) {
            throw new Exception("La foto no existe");
        }

        Long idUsuario = foto.getUsuario().getIdUsuario();
        String publicId = CloudinaryUtil.getFolderFotos() + "/usuario_" + idUsuario + "_foto_" + idFoto;
        
        return cloudinary.generarThumbnail(publicId);
    }

    /**
     * Genera URL transformada con dimensiones personalizadas
     * 
     * @param idFoto ID de la foto
     * @param ancho Ancho deseado
     * @param alto Alto deseado
     * @return URL transformada
     * @throws Exception Si hay error
     */
    public String generarUrlTransformada(Long idFoto, int ancho, int alto) throws Exception {
        Foto foto = fotoDAO.buscar(idFoto);
        if (foto == null) {
            throw new Exception("La foto no existe");
        }

        Long idUsuario = foto.getUsuario().getIdUsuario();
        String publicId = CloudinaryUtil.getFolderFotos() + "/usuario_" + idUsuario + "_foto_" + idFoto;
        
        return cloudinary.generarUrlTransformada(publicId, ancho, alto);
    }
}
