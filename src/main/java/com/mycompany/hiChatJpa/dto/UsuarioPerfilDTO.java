package com.mycompany.hiChatJpa.dto;

import java.time.LocalDate;

/**
 * DTO para mostrar perfil de usuario
 * Contiene información del usuario para ser usado en vistas
 * 
 * @author Luis Valenzuela
 */
public class UsuarioPerfilDTO {
    private Long idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    private String carrera;
    private String biografia;
    private String urlFotoPerfil;
    private String genero;
    private LocalDate fechaNacimiento;
    private Integer edad;

    // Constructor vacío
    public UsuarioPerfilDTO() {
    }

    // Constructor con campos principales
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

    // Constructor completo
    public UsuarioPerfilDTO(Long idUsuario, String nombre, String apellidoPaterno,
                           String apellidoMaterno, String correoElectronico, String carrera,
                           String biografia, String urlFotoPerfil, String genero,
                           LocalDate fechaNacimiento, Integer edad) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correoElectronico = correoElectronico;
        this.carrera = carrera;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
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

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", carrera='" + carrera + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}