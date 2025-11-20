/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hiChatJpa.holder;
import com.mycompany.hiChatJpa.dto.RegistroDTO;

/**
 *
 * @author Luis Valenzuela
 * Clase estática para pasar el RegistroDTO entre las vistas
 * Mantiene una instancia del DTO que se comparte entre todos los pasos
 */
public class RegistroDTOHolder {
    private static RegistroDTO registroDTO = new RegistroDTO();
    
    /**
     * Obtiene la instancia actual del DTO
     */
    public static RegistroDTO getRegistroDTO() {
        return registroDTO;
    }
    
    /**
     * Establece una nueva instancia del DTO
     */
    public static void setRegistroDTO(RegistroDTO dto) {
        registroDTO = dto;
    }
    
    /**
     * Limpia los datos del DTO (útil después de completar el registro)
     */
    public static void clearRegistroDTO() {
        registroDTO = new RegistroDTO();
    }
}