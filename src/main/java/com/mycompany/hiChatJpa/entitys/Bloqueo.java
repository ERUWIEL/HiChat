package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * clase que modela el bloqueo de usuarios
 *
 * @author angel
 */
@Entity
@Table(name = "bloqueo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_bloqueador", "usuario_bloqueado"})
)
public class Bloqueo implements Serializable {

    //seccion de mapeo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bloqueo")
    private Long idBloqueo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_bloqueador", nullable = false)
    private Usuario usuarioBloqueador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_bloqueado", nullable = false)
    private Usuario usuarioBloqueado;

    @Column(name = "fecha_bloqueo", nullable = false)
    private LocalDateTime fechaBloqueo;

    /**
     * constructor por ausencia de la clase
     */
    protected Bloqueo() {
    }

    /**
     * consctructor builder
     */
    private Bloqueo(Builder builder) {
        this.idBloqueo = builder.idBloqueo;
        this.usuarioBloqueador = builder.usuarioBloqueador;
        this.usuarioBloqueado = builder.usuarioBloqueado;
        this.fechaBloqueo = builder.fechaBloqueo;
    }

    /**
     * clase interna que nos permite implementar el patron builder
     */
    public static class Builder {

        private Long idBloqueo;
        private Usuario usuarioBloqueador;
        private Usuario usuarioBloqueado;
        private LocalDateTime fechaBloqueo = LocalDateTime.now();

        public Builder idBloqueo(Long idBloqueo) {
            this.idBloqueo = idBloqueo;
            return this;
        }

        public Builder usuarioBloqueador(Usuario usuarioBloqueador) {
            this.usuarioBloqueador = usuarioBloqueador;
            return this;
        }

        public Builder usuarioBloqueado(Usuario usuarioBloqueado) {
            this.usuarioBloqueado = usuarioBloqueado;
            return this;
        }

        public Builder fechaBloqueo(LocalDateTime fechaBloqueo) {
            this.fechaBloqueo = fechaBloqueo;
            return this;
        }

        public Bloqueo build() {
            return new Bloqueo(this);
        }
    }

    //getters y setters
    public Long getIdBloqueo() {
        return idBloqueo;
    }

    public void setIdBloqueo(Long idBloqueo) {
        this.idBloqueo = idBloqueo;
    }

    public Usuario getUsuarioBloqueador() {
        return usuarioBloqueador;
    }

    public void setUsuarioBloqueador(Usuario usuarioBloqueador) {
        this.usuarioBloqueador = usuarioBloqueador;
    }

    public Usuario getUsuarioBloqueado() {
        return usuarioBloqueado;
    }

    public void setUsuarioBloqueado(Usuario usuarioBloqueado) {
        this.usuarioBloqueado = usuarioBloqueado;
    }

    public LocalDateTime getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(LocalDateTime fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }
}
