/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hiChatJpa.dto;


import java.time.LocalDateTime;

/**
 *
 * @author Luis Valenzuela
 * DTO para información de bloqueos entre usuarios
 * Usado para validar y mostrar estado de bloqueos
 */
public class BloqueoDTO {

    private Long idBloqueo;
    private Long idUsuarioBloqueador;
    private String nombreBloqueador;
    private Long idUsuarioBloqueado;
    private String nombreBloqueado;
    private LocalDateTime fechaBloqueo;
    private Boolean bloqueado; // Indica si hay bloqueo

    // Constructor vacío
    public BloqueoDTO() {
    }

    // Constructor para validación de bloqueo
    public BloqueoDTO(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    // Constructor completo
    public BloqueoDTO(Long idBloqueo, Long idUsuarioBloqueador, String nombreBloqueador,
                      Long idUsuarioBloqueado, String nombreBloqueado,
                      LocalDateTime fechaBloqueo, Boolean bloqueado) {
        this.idBloqueo = idBloqueo;
        this.idUsuarioBloqueador = idUsuarioBloqueador;
        this.nombreBloqueador = nombreBloqueador;
        this.idUsuarioBloqueado = idUsuarioBloqueado;
        this.nombreBloqueado = nombreBloqueado;
        this.fechaBloqueo = fechaBloqueo;
        this.bloqueado = bloqueado;
    }

    // Getters y Setters
    public Long getIdBloqueo() {
        return idBloqueo;
    }

    public void setIdBloqueo(Long idBloqueo) {
        this.idBloqueo = idBloqueo;
    }

    public Long getIdUsuarioBloqueador() {
        return idUsuarioBloqueador;
    }

    public void setIdUsuarioBloqueador(Long idUsuarioBloqueador) {
        this.idUsuarioBloqueador = idUsuarioBloqueador;
    }

    public String getNombreBloqueador() {
        return nombreBloqueador;
    }

    public void setNombreBloqueador(String nombreBloqueador) {
        this.nombreBloqueador = nombreBloqueador;
    }

    public Long getIdUsuarioBloqueado() {
        return idUsuarioBloqueado;
    }

    public void setIdUsuarioBloqueado(Long idUsuarioBloqueado) {
        this.idUsuarioBloqueado = idUsuarioBloqueado;
    }

    public String getNombreBloqueado() {
        return nombreBloqueado;
    }

    public void setNombreBloqueado(String nombreBloqueado) {
        this.nombreBloqueado = nombreBloqueado;
    }

    public LocalDateTime getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(LocalDateTime fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    @Override
    public String toString() {
        return "BloqueoDTO{" +
                "idBloqueo=" + idBloqueo +
                ", nombreBloqueador='" + nombreBloqueador + '\'' +
                ", nombreBloqueado='" + nombreBloqueado + '\'' +
                ", bloqueado=" + bloqueado +
                '}';
    }
}
