package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "mensaje")
public class Mensaje implements Serializable {

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

    @PrePersist
    protected void onCreate() {
        fechaEnvio = LocalDateTime.now();
    }

    public Mensaje() {
    }

    public Mensaje(Long idMensaje, Chat chat, Usuario propietario, String contenido, LocalDateTime fechaEnvio) {
        this.idMensaje = idMensaje;
        this.chat = chat;
        this.usuarioEmisor = propietario;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.idMensaje);
        hash = 67 * hash + Objects.hashCode(this.chat);
        hash = 67 * hash + Objects.hashCode(this.usuarioEmisor);
        hash = 67 * hash + Objects.hashCode(this.contenido);
        hash = 67 * hash + Objects.hashCode(this.fechaEnvio);
        hash = 67 * hash + Objects.hashCode(this.estaVisto);
        hash = 67 * hash + Objects.hashCode(this.estaBorrado);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mensaje other = (Mensaje) obj;
        if (!Objects.equals(this.contenido, other.contenido)) {
            return false;
        }
        if (!Objects.equals(this.idMensaje, other.idMensaje)) {
            return false;
        }
        if (!Objects.equals(this.chat, other.chat)) {
            return false;
        }
        if (!Objects.equals(this.usuarioEmisor, other.usuarioEmisor)) {
            return false;
        }
        if (!Objects.equals(this.fechaEnvio, other.fechaEnvio)) {
            return false;
        }
        if (!Objects.equals(this.estaVisto, other.estaVisto)) {
            return false;
        }
        return Objects.equals(this.estaBorrado, other.estaBorrado);
    }

    @Override
    public String toString() {
        return "Mensaje{" + "idMensaje=" + idMensaje + ", chat=" + chat + ", propietario=" + usuarioEmisor + ", contenido=" + contenido + ", fechaEnvio=" + fechaEnvio + ", estaVisto=" + estaVisto + ", estaBorrado=" + estaBorrado + '}';
    }
}
