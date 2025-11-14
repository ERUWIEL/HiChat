package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * clase que modela las interacciones entre usuarios
 *
 * @author angel
 */
@Entity
@Table(name = "interaccion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_emisor", "usuario_receptor"})
)
@NamedQueries({
    @NamedQuery(
            name = "Interaccion.findAll",
            query = "SELECT i FROM Interaccion i ORDER BY i.fechaInteraccion DESC"
    ),
    @NamedQuery(
            name = "Interaccion.findByEmisor",
            query = "SELECT i FROM Interaccion i WHERE i.usuarioEmisor = :emisor ORDER BY i.fechaInteraccion DESC"
    ),
    @NamedQuery(
            name = "Interaccion.findByReceptor",
            query = "SELECT i FROM Interaccion i WHERE i.usuarioReceptor = :receptor ORDER BY i.fechaInteraccion DESC"
    ),
    @NamedQuery(
            name = "Interaccion.findByTipo",
            query = "SELECT i FROM Interaccion i WHERE i.tipo = :tipo ORDER BY i.fechaInteraccion DESC"
    )
})
public class Interaccion implements Serializable {

    // seccion de mapeo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interaccion")
    private Long idInteraccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_emisor", nullable = false)
    private Usuario usuarioEmisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_receptor", nullable = false)
    private Usuario usuarioReceptor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoInteraccion tipo;

    @Column(name = "fecha_interaccion", nullable = false)
    private LocalDateTime fechaInteraccion;

    /**
     * constructor por ausencia
     */
    protected Interaccion() {
    }

    /**
     * constructor builder
     *
     * @param builder
     */
    private Interaccion(Builder builder) {
        this.idInteraccion = builder.idInteraccion;
        this.usuarioEmisor = builder.usuarioEmisor;
        this.usuarioReceptor = builder.usuarioReceptor;
        this.tipo = builder.tipo;
        this.fechaInteraccion = builder.fechaInteraccion;
    }

    /**
     * clase interna que nos permite implementar el patron builder
     */
    public static class Builder {

        private Long idInteraccion;
        private Usuario usuarioEmisor;
        private Usuario usuarioReceptor;
        private TipoInteraccion tipo;
        private LocalDateTime fechaInteraccion = LocalDateTime.now();

        public Builder idInteraccion(Long idInteraccion) {
            this.idInteraccion = idInteraccion;
            return this;
        }

        public Builder usuarioEmisor(Usuario usuarioEmisor) {
            this.usuarioEmisor = usuarioEmisor;
            return this;
        }

        public Builder usuarioReceptor(Usuario usuarioReceptor) {
            this.usuarioReceptor = usuarioReceptor;
            return this;
        }

        public Builder tipo(TipoInteraccion tipo) {
            this.tipo = tipo;
            return this;
        }

        public Builder fechaInteraccion(LocalDateTime fechaInteraccion) {
            this.fechaInteraccion = fechaInteraccion;
            return this;
        }

        /**
         * consctructor builder con validaciones
         *
         * @return
         */
        public Interaccion build() {
            return new Interaccion(this);
        }
    }

    //getters y setters
    public Long getIdInteraccion() {
        return idInteraccion;
    }

    public void setIdInteraccion(Long idInteraccion) {
        this.idInteraccion = idInteraccion;
    }

    public Usuario getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(Usuario usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }

    public Usuario getUsuarioReceptor() {
        return usuarioReceptor;
    }

    public void setUsuarioReceptor(Usuario usuarioReceptor) {
        this.usuarioReceptor = usuarioReceptor;
    }

    public TipoInteraccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoInteraccion tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaInteraccion() {
        return fechaInteraccion;
    }

    public void setFechaInteraccion(LocalDateTime fechaInteraccion) {
        this.fechaInteraccion = fechaInteraccion;
    }
}
