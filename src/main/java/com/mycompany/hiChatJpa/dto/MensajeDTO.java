package com.mycompany.hiChatJpa.dto;

import java.time.LocalDateTime;

/**
 * DTO para mostrar mensajes en el chat
 *
 * @author Luis Valenzuela
 */
public class MensajeDTO {

    private Long idMensaje;
    private Long idChat;
    private UsuarioPerfilDTO emisor;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private Boolean estaVisto;
    private Boolean estaBorrado;

    // Constructor vacío
    public MensajeDTO() {
    }

    // Constructor completo
    public MensajeDTO(Long idMensaje, Long idChat, UsuarioPerfilDTO emisor,
            String contenido, LocalDateTime fechaEnvio,
            Boolean estaVisto, Boolean estaBorrado) {
        this.idMensaje = idMensaje;
        this.idChat = idChat;
        this.emisor = emisor;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
        this.estaVisto = estaVisto;
        this.estaBorrado = estaBorrado;
    }

    // Getters y Setters
    public Long getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Long idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public UsuarioPerfilDTO getEmisor() {
        return emisor;
    }

    public void setEmisor(UsuarioPerfilDTO emisor) {
        this.emisor = emisor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Boolean getEstaVisto() {
        return estaVisto;
    }

    public void setEstaVisto(Boolean estaVisto) {
        this.estaVisto = estaVisto;
    }

    public Boolean getEstaBorrado() {
        return estaBorrado;
    }

    public void setEstaBorrado(Boolean estaBorrado) {
        this.estaBorrado = estaBorrado;
    }

    @Override
    public String toString() {
        return "MensajeDTO{"
                + "idMensaje=" + idMensaje
                + ", emisor=" + (emisor != null ? emisor.getNombre() : "null")
                + ", contenido='" + contenido + '\''
                + ", fechaEnvio=" + fechaEnvio
                + '}';
    }
}
