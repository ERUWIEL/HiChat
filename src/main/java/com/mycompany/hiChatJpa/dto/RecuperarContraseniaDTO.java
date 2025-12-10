package com.mycompany.hiChatJpa.dto;

/**
 *
 * @author gatog
 */
public class RecuperarContraseniaDTO {
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String nuevaConstrasenia;

    public RecuperarContraseniaDTO(String nombre, String apellido, String correoElectronico, String nuevaConstrasenia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.nuevaConstrasenia = nuevaConstrasenia;
    }

    public RecuperarContraseniaDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNuevaConstrasenia() {
        return nuevaConstrasenia;
    }

    public void setNuevaConstrasenia(String nuevaConstrasenia) {
        this.nuevaConstrasenia = nuevaConstrasenia;
    }
}
