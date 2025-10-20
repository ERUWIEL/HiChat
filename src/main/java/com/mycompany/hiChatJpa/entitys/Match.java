package com.mycompany.hiChatJpa.entitys;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author angel
 */
@Entity
@Table(name = "matches")
public class Match implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_match")
    private Long idMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_a", nullable = false)
    private Usuario usuarioA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_b", nullable = false)
    private Usuario usuarioB;

    @Column(name = "fecha_match", nullable = false)
    private LocalDateTime fechaMatch;

    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private Chat chat;

    @PrePersist
    protected void onCreate() {
        fechaMatch = LocalDateTime.now();
    }

    public Match() {
    }

    public Match(Long idMatch, Usuario usuarioA, Usuario usuarioB, LocalDateTime fechaMatch, Chat chat) {
        this.idMatch = idMatch;
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
        this.fechaMatch = fechaMatch;
        this.chat = chat;
    }

    public Long getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Long idMatch) {
        this.idMatch = idMatch;
    }

    public Usuario getUsuarioA() {
        return usuarioA;
    }

    public void setUsuarioA(Usuario usuarioA) {
        this.usuarioA = usuarioA;
    }

    public Usuario getUsuarioB() {
        return usuarioB;
    }

    public void setUsuarioB(Usuario usuarioB) {
        this.usuarioB = usuarioB;
    }

    public LocalDateTime getFechaMatch() {
        return fechaMatch;
    }

    public void setFechaMatch(LocalDateTime fechaMatch) {
        this.fechaMatch = fechaMatch;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.idMatch);
        hash = 83 * hash + Objects.hashCode(this.usuarioA);
        hash = 83 * hash + Objects.hashCode(this.usuarioB);
        hash = 83 * hash + Objects.hashCode(this.fechaMatch);
        hash = 83 * hash + Objects.hashCode(this.chat);
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
        final Match other = (Match) obj;
        if (!Objects.equals(this.idMatch, other.idMatch)) {
            return false;
        }
        if (!Objects.equals(this.usuarioA, other.usuarioA)) {
            return false;
        }
        if (!Objects.equals(this.usuarioB, other.usuarioB)) {
            return false;
        }
        if (!Objects.equals(this.fechaMatch, other.fechaMatch)) {
            return false;
        }
        return Objects.equals(this.chat, other.chat);
    }

    @Override
    public String toString() {
        return "Match{" + "idMatch=" + idMatch + ", usuarioA=" + usuarioA + ", usuarioB=" + usuarioB + ", fechaMatch=" + fechaMatch + ", chat=" + chat + '}';
    }
}
