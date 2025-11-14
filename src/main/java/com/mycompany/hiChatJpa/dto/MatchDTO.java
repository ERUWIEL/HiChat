/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hiChatJpa.dto;

import java.time.LocalDateTime;

/**
 *
 * @author Luis Valenzuela
 * MatchDTO.java (Mejorado)
 * Ubicación: src/main/java/com/mycompany/hiChatJpa/dto/MatchDTO.java
 *Para: mostrarMatches(), crearMatch()
 */
public class MatchDTO {

    private Long idMatch;
    private UsuarioPerfilDTO usuarioA;
    private UsuarioPerfilDTO usuarioB;
    private LocalDateTime fechaMatch;
    private Long idChat;

    // Constructor vacío
    public MatchDTO() {
    }

    // Constructor completo
    public MatchDTO(Long idMatch, UsuarioPerfilDTO usuarioA, UsuarioPerfilDTO usuarioB,
                    LocalDateTime fechaMatch, Long idChat) {
        this.idMatch = idMatch;
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
        this.fechaMatch = fechaMatch;
        this.idChat = idChat;
    }

    // Getters y Setters
    public Long getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Long idMatch) {
        this.idMatch = idMatch;
    }

    public UsuarioPerfilDTO getUsuarioA() {
        return usuarioA;
    }

    public void setUsuarioA(UsuarioPerfilDTO usuarioA) {
        this.usuarioA = usuarioA;
    }

    public UsuarioPerfilDTO getUsuarioB() {
        return usuarioB;
    }

    public void setUsuarioB(UsuarioPerfilDTO usuarioB) {
        this.usuarioB = usuarioB;
    }

    public LocalDateTime getFechaMatch() {
        return fechaMatch;
    }

    public void setFechaMatch(LocalDateTime fechaMatch) {
        this.fechaMatch = fechaMatch;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    @Override
    public String toString() {
        return "MatchDTO{" +
                "idMatch=" + idMatch +
                ", usuarioA=" + (usuarioA != null ? usuarioA.getNombre() : "null") +
                ", usuarioB=" + (usuarioB != null ? usuarioB.getNombre() : "null") +
                ", fechaMatch=" + fechaMatch +
                '}';
    }
}
