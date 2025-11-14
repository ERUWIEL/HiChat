/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hiChatJpa.dto;


/**
 *
 * @author Luis Valenzuela
 * DTO para el login de usuarios
 * Recibe: correo y contraseña
 * Retorna: datos del usuario autenticado
 */
public class LoginDTO {

    private String correoElectronico;
    private String contrasena;

    // Constructor vacío
    public LoginDTO() {
    }

    // Constructor con parámetros
    public LoginDTO(String correoElectronico, String contrasena) {
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    // Getters y Setters
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

    @Override
    public String toString() {
        return "LoginDTO{" +
                "correoElectronico='" + correoElectronico + '\'' +
                '}';
    }
}