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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * clase que modela un usuario
 *
 * @author angel
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(
        name = "Usuario.findAll",
        query = "SELECT u FROM Usuario u ORDER BY u.nombre ASC, u.apellidoPaterno ASC"
    ),
    @NamedQuery(
        name = "Usuario.findByCorreo",
        query = "SELECT u FROM Usuario u WHERE u.correoElectronico = :correo"
    ),
    @NamedQuery(
        name = "Usuario.findByNombreCompleto",
        query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.apellidoPaterno = :apellidoPaterno"
    )
})
public class Usuario implements Serializable {

    //seccion de mapeo
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

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "carrera", length = 100)
    private String carrera;

    @Column(name = "biografia", columnDefinition = "TEXT", length = 500)
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

    /**
     * constructor privado para implementar buider
     *
     * @param builder
     */
    private Usuario(Builder builder) {
        this.idUsuario = builder.idUsuario;
        this.nombre = builder.nombre;
        this.apellidoPaterno = builder.apellidoPaterno;
        this.apellidoMaterno = builder.apellidoMaterno;
        this.correoElectronico = builder.correoElectronico;
        this.contrasena = builder.contrasena;
        this.carrera = builder.carrera;
        this.biografia = builder.biografia;
        this.urlFotoPerfil = builder.urlFotoPerfil;
        this.genero = builder.genero;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.fechaRegistro = builder.fechaRegistro;
        this.pasatiempos = builder.pasatiempos;
        this.fotos = builder.fotos;
        this.interaccionesEnviadas = builder.interaccionesEnviadas;
        this.interaccionesRecibidas = builder.interaccionesRecibidas;
        this.bloqueosRealizados = builder.bloqueosRealizados;
        this.bloqueosRecibidos = builder.bloqueosRecibidos;
        this.mensajesEnviados = builder.mensajesEnviados;
        this.chats = builder.chats;
    }

    /**
     * constructor por ausencia
     */
    protected Usuario() {
    }

    /**
     * clase interna que permite usar builder
     */
    public static class Builder {

        private Long idUsuario;
        private String nombre;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String correoElectronico;
        private String contrasena;
        private String carrera;
        private String biografia;
        private String urlFotoPerfil;
        private Genero genero;
        private LocalDate fechaNacimiento;
        private LocalDateTime fechaRegistro = LocalDateTime.now();
        private Set<Pasatiempo> pasatiempos = new HashSet<>();
        private Set<Foto> fotos = new HashSet<>();
        private Set<Interaccion> interaccionesEnviadas = new HashSet<>();
        private Set<Interaccion> interaccionesRecibidas = new HashSet<>();
        private Set<Bloqueo> bloqueosRealizados = new HashSet<>();
        private Set<Bloqueo> bloqueosRecibidos = new HashSet<>();
        private Set<Mensaje> mensajesEnviados = new HashSet<>();
        private Set<Chat> chats = new HashSet<>();

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder apellidoPaterno(String apellidoPaterno) {
            this.apellidoPaterno = apellidoPaterno;
            return this;
        }

        public Builder apellidoMaterno(String apellidoMaterno) {
            this.apellidoMaterno = apellidoMaterno;
            return this;
        }

        public Builder correoElectronico(String correoElectronico) {
            this.correoElectronico = correoElectronico;
            return this;
        }

        public Builder contrasena(String contrasena) {
            this.contrasena = contrasena;
            return this;
        }

        public Builder carrera(String carrera) {
            this.carrera = carrera;
            return this;
        }

        public Builder biografia(String biografia) {
            this.biografia = biografia;
            return this;
        }

        public Builder urlFotoPerfil(String urlFotoPerfil) {
            this.urlFotoPerfil = urlFotoPerfil;
            return this;
        }

        public Builder genero(Genero genero) {
            this.genero = genero;
            return this;
        }

        public Builder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public Builder fechaRegistro(LocalDateTime fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
            return this;
        }

        public Builder pasatiempos(Set<Pasatiempo> pasatiempos) {
            this.pasatiempos = pasatiempos;
            return this;
        }

        public Builder fotos(Set<Foto> fotos) {
            this.fotos = fotos;
            return this;
        }

        public Builder chats(Set<Chat> chats) {
            this.chats = chats;
            return this;
        }

        /**
         * constructor builder con validaciones
         *
         * @return
         */
        public Usuario build() {
            return new Usuario(this);
        }
    }

    //getters y setters
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
}
