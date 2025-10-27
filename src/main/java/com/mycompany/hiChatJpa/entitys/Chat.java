package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * clase que modela un chat
 *
 * @author angel
 */
@Entity
@Table(name = "chat")
public class Chat implements Serializable {

    // seccion de mapeo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat")
    private Long idChat;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_match", unique = true)
    private Match match;

    @ManyToMany(mappedBy = "chats")
    private Set<Usuario> participantes = new HashSet<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Mensaje> mensajes = new HashSet<>();

    /**
     * metodo constructor privado para el builder
     *
     * @param builder
     */
    private Chat(Builder builder) {
        this.idChat = builder.idChat;
        this.nombre = builder.nombre;
        this.match = builder.match;
        this.participantes = builder.participantes;
        this.mensajes = builder.mensajes;
    }

    /**
     * constructor por ausencia
     */
    protected Chat() {
    }

    /**
     * clas que permite implementar el builder
     */
    public static class Builder {
        private Long idChat;
        private String nombre;
        private Match match;
        private Set<Usuario> participantes = new HashSet<>();
        private Set<Mensaje> mensajes = new HashSet<>();

        public Builder idChat(Long idChat) {
            this.idChat = idChat;
            return this;
        }
        
        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder match(Match match) {
            this.match = match;
            return this;
        }

        public Builder participantes(Set<Usuario> participantes) {
            this.participantes = participantes;
            return this;
        }

        public Builder mensajes(Set<Mensaje> mensajes) {
            this.mensajes = mensajes;
            return this;
        }

        /**
         * constructor builder con validaciones
         * @return 
         */
        public Chat build() {
            return new Chat(this);
        }
    }

    //getters y setters
    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Set<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Set<Usuario> participantes) {
        this.participantes = participantes;
    }

    public Set<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(Set<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
