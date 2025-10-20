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
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "bloqueo",
    uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_bloqueador", "usuario_bloqueado"})
)
public class Bloqueo implements Serializable {

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

    @PrePersist
    protected void onCreate() {
        fechaBloqueo = LocalDateTime.now();
    }

    public Bloqueo() {
    }

    public Bloqueo(Long idBloqueo, Usuario usuarioBloqueador, Usuario usuarioBloqueado, LocalDateTime fechaBloqueo) {
        this.idBloqueo = idBloqueo;
        this.usuarioBloqueador = usuarioBloqueador;
        this.usuarioBloqueado = usuarioBloqueado;
        this.fechaBloqueo = fechaBloqueo;
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idBloqueo);
        hash = 97 * hash + Objects.hashCode(this.usuarioBloqueador);
        hash = 97 * hash + Objects.hashCode(this.usuarioBloqueado);
        hash = 97 * hash + Objects.hashCode(this.fechaBloqueo);
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
        final Bloqueo other = (Bloqueo) obj;
        if (!Objects.equals(this.idBloqueo, other.idBloqueo)) {
            return false;
        }
        if (!Objects.equals(this.usuarioBloqueador, other.usuarioBloqueador)) {
            return false;
        }
        if (!Objects.equals(this.usuarioBloqueado, other.usuarioBloqueado)) {
            return false;
        }
        return Objects.equals(this.fechaBloqueo, other.fechaBloqueo);
    }

    @Override
    public String toString() {
        return "Bloqueo{" + "idBloqueo=" + idBloqueo + ", usuarioBloqueador=" + usuarioBloqueador + ", usuarioBloqueado=" + usuarioBloqueado + ", fechaBloqueo=" + fechaBloqueo + '}';
    }
}
