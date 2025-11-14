/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hiChatJpa.dto;


/**
 *
 * @author Luis Valenzuela
 * DTO para mostrar perfil público de usuario
 * Contiene solo información visible para otros usuarios
 * No incluye datos sensibles
 */

public class UsuarioPerfilDTO {

    private Long idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String carrera;
    private String biografia;
    private String urlFotoPerfil;
    private String genero;
    private Integer edad;

    // Constructor vacío
    public UsuarioPerfilDTO() {
    }

    // Constructor completo
    public UsuarioPerfilDTO(Long idUsuario, String nombre, String apellidoPaterno,
                           String carrera, String biografia, String urlFotoPerfil,
                           String genero, Integer edad) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.carrera = carrera;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.genero = genero;
        this.edad = edad;
    }

    // Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return "UsuarioPerfilDTO{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", carrera='" + carrera + '\'' +
                '}';
    }
}
