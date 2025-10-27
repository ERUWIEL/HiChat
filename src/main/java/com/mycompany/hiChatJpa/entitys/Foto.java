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

/**
 * clase que modela una foto
 * @author angel
 */
@Entity
@Table(name = "foto")
@NamedQueries({
    @NamedQuery(
        name = "Foto.findAll",
        query = "SELECT f FROM Foto f ORDER BY f.idFoto"
    ),
    @NamedQuery(
        name = "Foto.findByUsuario",
        query = "SELECT f FROM Foto f WHERE f.usuario = :usuario ORDER BY f.idFoto"
    ),
    @NamedQuery(
        name = "Foto.findByDescripcion",
        query = "SELECT f FROM Foto f WHERE f.descripcion LIKE :descripcion"
    )
})
public class Foto implements Serializable {
    // seccion de mapeo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Long idFoto;

    @Column(name = "url_foto", nullable = false, length = 255)
    private String urlFoto;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * metodo constructor builder
     *
     * @param builder
     */
    private Foto(Builder builder) {
        this.idFoto = builder.idFoto;
        this.urlFoto = builder.urlFoto;
        this.descripcion = builder.descripcion;
        this.usuario = builder.usuario;
    }

    /**
     * metodo constructor por ausencia
     */
    protected Foto() {
    }

    /**
     * clase interna para implementar builder
     */
    public static class Builder {
        private Long idFoto;
        private String urlFoto;
        private String descripcion;
        private Usuario usuario;

        public Builder idFoto(Long idFoto) {
            this.idFoto = idFoto;
            return this;
        }

        public Builder urlFoto(String urlFoto) {
            this.urlFoto = urlFoto;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder usuario(Usuario usuario) {
            this.usuario = usuario;
            return this;
        }

        /**
         * construtor validador
         *
         * @return
         */
        public Foto build() {
            return new Foto(this);
        }
    }

    //getters y setters
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
}
