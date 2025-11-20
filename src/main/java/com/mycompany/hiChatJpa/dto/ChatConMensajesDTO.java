/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hiChatJpa.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Luis Valenzuela
 * DTO para cargar chats del usuario con información completa
 * Incluye participantes y último mensaje
 */
public class ChatConMensajesDTO {

    private Long idChat;
    private String nombre;
    private Long idMatch;
    private List<UsuarioPerfilDTO> participantes;
    private Integer totalMensajes;
    private String ultimoMensaje;
    private LocalDateTime fechaUltimoMensaje;
    private Boolean hayNoLeidos;

    // Constructor vacío
    public ChatConMensajesDTO() {
    }

    // Constructor completo
    public ChatConMensajesDTO(Long idChat, String nombre, Long idMatch,
                              List<UsuarioPerfilDTO> participantes, Integer totalMensajes,
                              String ultimoMensaje, LocalDateTime fechaUltimoMensaje,
                              Boolean hayNoLeidos) {
        this.idChat = idChat;
        this.nombre = nombre;
        this.idMatch = idMatch;
        this.participantes = participantes;
        this.totalMensajes = totalMensajes;
        this.ultimoMensaje = ultimoMensaje;
        this.fechaUltimoMensaje = fechaUltimoMensaje;
        this.hayNoLeidos = hayNoLeidos;
    }

    // Getters y Setters
    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Long idMatch) {
        this.idMatch = idMatch;
    }

    public List<UsuarioPerfilDTO> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<UsuarioPerfilDTO> participantes) {
        this.participantes = participantes;
    }

    public Integer getTotalMensajes() {
        return totalMensajes;
    }

    public void setTotalMensajes(Integer totalMensajes) {
        this.totalMensajes = totalMensajes;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public LocalDateTime getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    public void setFechaUltimoMensaje(LocalDateTime fechaUltimoMensaje) {
        this.fechaUltimoMensaje = fechaUltimoMensaje;
    }

    public Boolean getHayNoLeidos() {
        return hayNoLeidos;
    }

    public void setHayNoLeidos(Boolean hayNoLeidos) {
        this.hayNoLeidos = hayNoLeidos;
    }

    @Override
    public String toString() {
        return "ChatConMensajesDTO{" +
                "idChat=" + idChat +
                ", nombre='" + nombre + '\'' +
                ", totalMensajes=" + totalMensajes +
                ", hayNoLeidos=" + hayNoLeidos +
                '}';
    }
}
