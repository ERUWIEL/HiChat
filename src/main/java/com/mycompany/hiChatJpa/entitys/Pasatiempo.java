package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * clase que modela un pasatiempo
 *
 * @author angel
 */
@Entity
@Table(name = "pasatiempo")
public class Pasatiempo implements Serializable {
    // seccion de mapeo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasatiempo")
    private Long idPasatiempo;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToMany(mappedBy = "pasatiempos")
    private Set<Usuario> usuarios = new HashSet<>();

    /**
     * metodo constructor implementando builder
     *
     * @param builder
     */
    private Pasatiempo(Builder builder) {
        this.idPasatiempo = builder.idPasatiempo;
        this.nombre = builder.nombre;
        this.descripcion = builder.descripcion;
        this.usuarios = new HashSet<>(builder.usuarios);
    }

    /**
     * metodo constructor por ausencia
     */
    protected Pasatiempo() {
    }

    /**
     * clase interna que permite crear instancias
     */
    public static class Builder {
        private Long idPasatiempo;
        private String nombre;
        private String descripcion;
        private Set<Usuario> usuarios = new HashSet<>();

        public Builder idPasatiempo(Long idPasatiempo) {
            this.idPasatiempo = idPasatiempo;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder usuarios(Set<Usuario> usuarios) {
            if (usuarios != null) {
                this.usuarios = new HashSet<>(usuarios);
            }
            return this;
        }

        /**
         * constructor builder con validaciones
         *
         * @return
         */
        public Pasatiempo build() {
            return new Pasatiempo(this);
        }
    }

    //getters y setters
    public Long getIdPasatiempo() {
        return idPasatiempo;
    }

    public void setIdPasatiempo(Long idPasatiempo) {
        this.idPasatiempo = idPasatiempo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
