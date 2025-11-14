package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * clase que modela un mensaje
 *
 * @author angel
 */
@Entity
@Table(name = "mensaje")
@NamedQueries({
    @NamedQuery(
        name = "Mensaje.findAll",
        query = "SELECT m FROM Mensaje m ORDER BY m.fechaEnvio DESC"
    ),
    @NamedQuery(
        name = "Mensaje.findByChat",
        query = "SELECT m FROM Mensaje m WHERE m.chat = :chat ORDER BY m.fechaEnvio DESC"
    ),
    @NamedQuery(
        name = "Mensaje.findNoVistosPorUsuario",
        query = "SELECT m FROM Mensaje m WHERE m.chat IN (SELECT c FROM Chat c JOIN c.participantes p WHERE p = :usuario) AND m.usuarioEmisor <> :usuario AND m.estaVisto = false ORDER BY m.fechaEnvio DESC"
    )
})
public class Mensaje implements Serializable {

    // seccion de mapeo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_emisor", nullable = false)
    private Usuario usuarioEmisor;

    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "esta_visto", nullable = false)
    private Boolean estaVisto = false;

    @Column(name = "esta_borrado", nullable = false)
    private Boolean estaBorrado = false;

    /**
     * constructor builder
     *
     * @param builder
     */
    private Mensaje(Builder builder) {
        this.idMensaje = builder.idMensaje;
        this.chat = builder.chat;
        this.usuarioEmisor = builder.usuarioEmisor;
        this.contenido = builder.contenido;
        this.fechaEnvio = builder.fechaEnvio;
        this.estaVisto = builder.estaVisto;
        this.estaBorrado = builder.estaBorrado;
    }

    /**
     * constructor por ausencia
     */
    protected Mensaje() {
    }

    /**
     * clase interna que permite implementar builder
     */
    public static class Builder {

        private Long idMensaje;
        private Chat chat;
        private Usuario usuarioEmisor;
        private String contenido;
        private LocalDateTime fechaEnvio;
        private Boolean estaVisto = false;
        private Boolean estaBorrado = false;

        public Builder idMensaje(Long idMensaje){
            this.idMensaje = idMensaje;
            return this;
        }
        
        public Builder chat(Chat chat) {
            this.chat = chat;
            return this;
        }

        public Builder usuarioEmisor(Usuario usuarioEmisor) {
            this.usuarioEmisor = usuarioEmisor;
            return this;
        }

        public Builder contenido(String contenido) {
            this.contenido = contenido;
            return this;
        }

        public Builder fechaEnvio(LocalDateTime fechaEnvio) {
            this.fechaEnvio = fechaEnvio;
            return this;
        }

        public Builder estaVisto(Boolean estaVisto) {
            this.estaVisto = estaVisto;
            return this;
        }

        public Builder estaBorrado(Boolean estaBorrado) {
            this.estaBorrado = estaBorrado;
            return this;
        }

        /**
         * constructor builder con validaciones
         *
         * @return
         */
        public Mensaje build() {
            if (fechaEnvio == null) {
                fechaEnvio = LocalDateTime.now();
            }
            return new Mensaje(this);
        }
    }

    // getters y setters
    public Long getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Long idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Usuario getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(Usuario usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Boolean getEstaVisto() {
        return estaVisto;
    }

    public void setEstaVisto(Boolean estaVisto) {
        this.estaVisto = estaVisto;
    }

    public Boolean getEstaBorrado() {
        return estaBorrado;
    }

    public void setEstaBorrado(Boolean estaBorrado) {
        this.estaBorrado = estaBorrado;
    }
}
