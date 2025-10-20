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
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "chat")
public class Chat implements Serializable {

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

    public Chat() {
    }

    public Chat(Long idChat, String nombre, Match match) {
        this.idChat = idChat;
        this.nombre = nombre;
        this.match = match;
    }

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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.idChat);
        hash = 73 * hash + Objects.hashCode(this.nombre);
        hash = 73 * hash + Objects.hashCode(this.match);
        hash = 73 * hash + Objects.hashCode(this.participantes);
        hash = 73 * hash + Objects.hashCode(this.mensajes);
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
        final Chat other = (Chat) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.idChat, other.idChat)) {
            return false;
        }
        if (!Objects.equals(this.match, other.match)) {
            return false;
        }
        if (!Objects.equals(this.participantes, other.participantes)) {
            return false;
        }
        return Objects.equals(this.mensajes, other.mensajes);
    }

    @Override
    public String toString() {
        return "Chat{" + "idChat=" + idChat + ", nombre=" + nombre + ", match=" + match + ", participantes=" + participantes + ", mensajes=" + mensajes + '}';
    }
}
