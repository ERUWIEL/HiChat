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
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "pasatiempo")
public class Pasatiempo implements Serializable {

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

    public Pasatiempo() {
    }

    public Pasatiempo(Long idPasatiempo, String nombre, String descripcion) {
        this.idPasatiempo = idPasatiempo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.idPasatiempo);
        hash = 29 * hash + Objects.hashCode(this.nombre);
        hash = 29 * hash + Objects.hashCode(this.descripcion);
        hash = 29 * hash + Objects.hashCode(this.usuarios);
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
        final Pasatiempo other = (Pasatiempo) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.idPasatiempo, other.idPasatiempo)) {
            return false;
        }
        return Objects.equals(this.usuarios, other.usuarios);
    }

    @Override
    public String toString() {
        return "Pasatiempo{" + "idPasatiempo=" + idPasatiempo + ", nombre=" + nombre + ", descripcion=" + descripcion + ", usuarios=" + usuarios + '}';
    }
}
