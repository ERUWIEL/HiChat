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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "interaccion",
    uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_emisor", "usuario_receptor"})
)
public class Interaccion implements Serializable {

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

    @PrePersist
    protected void onCreate() {
        fechaInteraccion = LocalDateTime.now();
    }

    public Interaccion() {
    }

    public Interaccion(Long idInteraccion, Usuario usuarioEmisor, Usuario usuarioReceptor, TipoInteraccion tipo, LocalDateTime fechaInteraccion) {
        this.idInteraccion = idInteraccion;
        this.usuarioEmisor = usuarioEmisor;
        this.usuarioReceptor = usuarioReceptor;
        this.tipo = tipo;
        this.fechaInteraccion = fechaInteraccion;
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.idInteraccion);
        hash = 37 * hash + Objects.hashCode(this.usuarioEmisor);
        hash = 37 * hash + Objects.hashCode(this.usuarioReceptor);
        hash = 37 * hash + Objects.hashCode(this.tipo);
        hash = 37 * hash + Objects.hashCode(this.fechaInteraccion);
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
        final Interaccion other = (Interaccion) obj;
        if (!Objects.equals(this.idInteraccion, other.idInteraccion)) {
            return false;
        }
        if (!Objects.equals(this.usuarioEmisor, other.usuarioEmisor)) {
            return false;
        }
        if (!Objects.equals(this.usuarioReceptor, other.usuarioReceptor)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return Objects.equals(this.fechaInteraccion, other.fechaInteraccion);
    }

    @Override
    public String toString() {
        return "Interaccion{" + "idInteraccion=" + idInteraccion + ", usuarioEmisor=" + usuarioEmisor + ", usuarioReceptor=" + usuarioReceptor + ", tipo=" + tipo + ", fechaInteraccion=" + fechaInteraccion + '}';
    }
}
