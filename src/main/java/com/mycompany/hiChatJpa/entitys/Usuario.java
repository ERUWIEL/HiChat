package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;

    @Column(name = "nombre_completo", length = 150)
    private String nombreCompleto;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "carrera", length = 100)
    private String carrera;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "url_foto_perfil", length = 255)
    private String urlFotoPerfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 20)
    private Genero genero;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @ManyToMany
    @JoinTable(
            name = "usuario_pasatiempo",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_pasatiempo")
    )
    private Set<Pasatiempo> pasatiempos = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Foto> fotos = new HashSet<>();

    @OneToMany(mappedBy = "usuarioEmisor")
    private Set<Interaccion> interaccionesEnviadas = new HashSet<>();

    @OneToMany(mappedBy = "usuarioReceptor")
    private Set<Interaccion> interaccionesRecibidas = new HashSet<>();

    @OneToMany(mappedBy = "usuarioBloqueador")
    private Set<Bloqueo> bloqueosRealizados = new HashSet<>();

    @OneToMany(mappedBy = "usuarioBloqueado")
    private Set<Bloqueo> bloqueosRecibidos = new HashSet<>();

    @OneToMany(mappedBy = "usuarioEmisor")
    private Set<Mensaje> mensajesEnviados = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "usuario_chat",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_chat")
    )
    private Set<Chat> chats = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        if (nombreCompleto == null) {
            nombreCompleto = nombre + " " + apellidoPaterno
                    + (apellidoMaterno != null ? " " + apellidoMaterno : "");
        }
    }

    public Usuario() {
    }

    public Usuario(Long idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, String nombreCompleto, String correoElectronico, String contrasena, String carrera, String biografia, String urlFotoPerfil, Genero genero, LocalDate fechaNacimiento, LocalDateTime fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.carrera = carrera;
        this.biografia = biografia;
        this.urlFotoPerfil = urlFotoPerfil;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Pasatiempo> getPasatiempos() {
        return pasatiempos;
    }

    public void setPasatiempos(Set<Pasatiempo> pasatiempos) {
        this.pasatiempos = pasatiempos;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    public Set<Interaccion> getInteraccionesEnviadas() {
        return interaccionesEnviadas;
    }

    public void setInteraccionesEnviadas(Set<Interaccion> interaccionesEnviadas) {
        this.interaccionesEnviadas = interaccionesEnviadas;
    }

    public Set<Interaccion> getInteraccionesRecibidas() {
        return interaccionesRecibidas;
    }

    public void setInteraccionesRecibidas(Set<Interaccion> interaccionesRecibidas) {
        this.interaccionesRecibidas = interaccionesRecibidas;
    }

    public Set<Bloqueo> getBloqueosRealizados() {
        return bloqueosRealizados;
    }

    public void setBloqueosRealizados(Set<Bloqueo> bloqueosRealizados) {
        this.bloqueosRealizados = bloqueosRealizados;
    }

    public Set<Bloqueo> getBloqueosRecibidos() {
        return bloqueosRecibidos;
    }

    public void setBloqueosRecibidos(Set<Bloqueo> bloqueosRecibidos) {
        this.bloqueosRecibidos = bloqueosRecibidos;
    }

    public Set<Mensaje> getMensajesEnviados() {
        return mensajesEnviados;
    }

    public void setMensajesEnviados(Set<Mensaje> mensajesEnviados) {
        this.mensajesEnviados = mensajesEnviados;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.idUsuario);
        hash = 71 * hash + Objects.hashCode(this.nombre);
        hash = 71 * hash + Objects.hashCode(this.apellidoPaterno);
        hash = 71 * hash + Objects.hashCode(this.apellidoMaterno);
        hash = 71 * hash + Objects.hashCode(this.nombreCompleto);
        hash = 71 * hash + Objects.hashCode(this.correoElectronico);
        hash = 71 * hash + Objects.hashCode(this.contrasena);
        hash = 71 * hash + Objects.hashCode(this.carrera);
        hash = 71 * hash + Objects.hashCode(this.biografia);
        hash = 71 * hash + Objects.hashCode(this.urlFotoPerfil);
        hash = 71 * hash + Objects.hashCode(this.genero);
        hash = 71 * hash + Objects.hashCode(this.fechaNacimiento);
        hash = 71 * hash + Objects.hashCode(this.fechaRegistro);
        hash = 71 * hash + Objects.hashCode(this.pasatiempos);
        hash = 71 * hash + Objects.hashCode(this.fotos);
        hash = 71 * hash + Objects.hashCode(this.interaccionesEnviadas);
        hash = 71 * hash + Objects.hashCode(this.interaccionesRecibidas);
        hash = 71 * hash + Objects.hashCode(this.bloqueosRealizados);
        hash = 71 * hash + Objects.hashCode(this.bloqueosRecibidos);
        hash = 71 * hash + Objects.hashCode(this.mensajesEnviados);
        hash = 71 * hash + Objects.hashCode(this.chats);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apellidoPaterno, other.apellidoPaterno)) {
            return false;
        }
        if (!Objects.equals(this.apellidoMaterno, other.apellidoMaterno)) {
            return false;
        }
        if (!Objects.equals(this.nombreCompleto, other.nombreCompleto)) {
            return false;
        }
        if (!Objects.equals(this.correoElectronico, other.correoElectronico)) {
            return false;
        }
        if (!Objects.equals(this.contrasena, other.contrasena)) {
            return false;
        }
        if (!Objects.equals(this.carrera, other.carrera)) {
            return false;
        }
        if (!Objects.equals(this.biografia, other.biografia)) {
            return false;
        }
        if (!Objects.equals(this.urlFotoPerfil, other.urlFotoPerfil)) {
            return false;
        }
        if (!Objects.equals(this.idUsuario, other.idUsuario)) {
            return false;
        }
        if (this.genero != other.genero) {
            return false;
        }
        if (!Objects.equals(this.fechaNacimiento, other.fechaNacimiento)) {
            return false;
        }
        if (!Objects.equals(this.fechaRegistro, other.fechaRegistro)) {
            return false;
        }
        if (!Objects.equals(this.pasatiempos, other.pasatiempos)) {
            return false;
        }
        if (!Objects.equals(this.fotos, other.fotos)) {
            return false;
        }
        if (!Objects.equals(this.interaccionesEnviadas, other.interaccionesEnviadas)) {
            return false;
        }
        if (!Objects.equals(this.interaccionesRecibidas, other.interaccionesRecibidas)) {
            return false;
        }
        if (!Objects.equals(this.bloqueosRealizados, other.bloqueosRealizados)) {
            return false;
        }
        if (!Objects.equals(this.bloqueosRecibidos, other.bloqueosRecibidos)) {
            return false;
        }
        if (!Objects.equals(this.mensajesEnviados, other.mensajesEnviados)) {
            return false;
        }
        return Objects.equals(this.chats, other.chats);
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno + ", nombreCompleto=" + nombreCompleto + ", correoElectronico=" + correoElectronico + ", contrasena=" + contrasena + ", carrera=" + carrera + ", biografia=" + biografia + ", urlFotoPerfil=" + urlFotoPerfil + ", genero=" + genero + ", fechaNacimiento=" + fechaNacimiento + ", fechaRegistro=" + fechaRegistro + ", pasatiempos=" + pasatiempos + ", fotos=" + fotos + ", interaccionesEnviadas=" + interaccionesEnviadas + ", interaccionesRecibidas=" + interaccionesRecibidas + ", bloqueosRealizados=" + bloqueosRealizados + ", bloqueosRecibidos=" + bloqueosRecibidos + ", mensajesEnviados=" + mensajesEnviados + ", chats=" + chats + '}';
    }
}
