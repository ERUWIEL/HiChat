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
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "foto")
public class Foto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_foto")
    private Long idFoto;

    @Column(name = "url_foto", nullable = false, length = 255)
    private String urlFoto;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public Foto() {
    }

    public Foto(Long idFoto, String urlFoto, String descripcion, Usuario usuario) {
        this.idFoto = idFoto;
        this.urlFoto = urlFoto;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public Long getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Long idFoto) {
        this.idFoto = idFoto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.idFoto);
        hash = 97 * hash + Objects.hashCode(this.urlFoto);
        hash = 97 * hash + Objects.hashCode(this.descripcion);
        hash = 97 * hash + Objects.hashCode(this.usuario);
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
        final Foto other = (Foto) obj;
        if (!Objects.equals(this.urlFoto, other.urlFoto)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.idFoto, other.idFoto)) {
            return false;
        }
        return Objects.equals(this.usuario, other.usuario);
    }

    @Override
    public String toString() {
        return "Foto{" + "idFoto=" + idFoto + ", urlFoto=" + urlFoto + ", descripcion=" + descripcion + ", usuario=" + usuario + '}';
    }
}
