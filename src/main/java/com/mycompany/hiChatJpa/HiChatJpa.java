package com.mycompany.hiChatJpa;

import com.mycompany.hiChatJpa.config.CloudinaryUtil;
import com.mycompany.hiChatJpa.service.impl.UsuarioService;
import java.io.IOException;

/**
 * Clase principal para cargar datos de prueba
 *
 * @author erwbyel
 */
public class HiChatJpa {
    
    private static UsuarioService usuarioService = new UsuarioService();

    public static void main(String[] args) {
        CloudinaryUtil cl = CloudinaryUtil.getInstance();
        long ln = (long) 1;
        try {
            cl.subirFotoPerfil("\"C:\\Users\\gatog\\Pictures\\Screenshots\\Screenshot 2025-10-31 094652.png\"", ln);
        } catch (IOException ex) {
            System.getLogger(HiChatJpa.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        System.exit(0);

    }
    
    
}
