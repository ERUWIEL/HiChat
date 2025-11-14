
package com.mycompany.hiChatJpa.dto;

import java.time.LocalDate;

/**
 *
 * @author Luis Valenzuela
 * DTO para el registro de nuevos usuarios
 * Recibe: datos básicos para crear una cuenta
 * No incluye contraseña en la respuesta por seguridad
 */

public class RegistroDTO {

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private String carrera;
    private String genero;

    // Constructor vacío
    public RegistroDTO() {
    }

    // Constructor completo
    public RegistroDTO(String nombre, String apellidoPaterno, String apellidoMaterno,
                       String correoElectronico, String contrasena, LocalDate fechaNacimiento,
                       String carrera, String genero) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.fechaNacimiento = fechaNacimiento;
        this.carrera = carrera;
        this.genero = genero;
    }

    // Getters y Setters
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "RegistroDTO{" +
                "nombre='" + nombre + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                '}';
    } 
}
