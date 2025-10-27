
package com.mycompany.hiChatJpa.service;

import com.mycompany.hiChatJpa.entitys.Pasatiempo;
import java.util.List;

/**
 *
 * @author gatog
 */
public interface IPasatiempoService {

    void registrarPasatiempo(Pasatiempo pasatiempo) throws Exception;

    void actualizarPasatiempo(Pasatiempo pasatiempo) throws Exception;

    void eliminarPasatiempo(Long id) throws Exception;

    Pasatiempo buscarPorId(Long id);

    List<Pasatiempo> listarPasatiempos();

    Pasatiempo buscarPorNombre(String nombre);
}
