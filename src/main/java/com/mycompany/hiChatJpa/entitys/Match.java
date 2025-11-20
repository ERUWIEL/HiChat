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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * clase que modela la tabla de matches
 *
 * @author angel
 */
@Entity
@Table(name = "matches")

@NamedQueries({
    @NamedQuery(
            name = "Match.findAll",
            query = "SELECT m FROM Match m ORDER BY m.fechaMatch DESC"
    ),
    @NamedQuery(
            name = "Match.findByUsuarioA",
            query = "SELECT m FROM Match m WHERE m.usuarioA = :usuarioA ORDER BY m.fechaMatch DESC"
    ),
    @NamedQuery(
            name = "Match.findByUsuarioB",
            query = "SELECT m FROM Match m WHERE m.usuarioB = :usuarioB ORDER BY m.fechaMatch DESC"
    )
})
public class Match implements Serializable {

    //seccion de mapeo
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

    /**
     * ocnstructor por ausencia de la clase
     */
    protected Match() {
    }

    /**
     * constructor builder
     *
     * @param builder
     */
    private Match(Builder builder) {
        this.idMatch = builder.idMatch;
        this.usuarioA = builder.usuarioA;
        this.usuarioB = builder.usuarioB;
        this.fechaMatch = builder.fechaMatch;
        this.chat = builder.chat;
    }

    /**
     * clase interna que permite implementar el patron builder
     */
    public static class Builder {

        private Long idMatch;
        private Usuario usuarioA;
        private Usuario usuarioB;
        private LocalDateTime fechaMatch = LocalDateTime.now();
        private Chat chat;

        public Builder idMatch(Long idMatch) {
            this.idMatch = idMatch;
            return this;
        }

        public Builder usuarioA(Usuario usuarioA) {
            this.usuarioA = usuarioA;
            return this;
        }

        public Builder usuarioB(Usuario usuarioB) {
            this.usuarioB = usuarioB;
            return this;
        }

        public Builder fechaMatch(LocalDateTime fechaMatch) {
            this.fechaMatch = fechaMatch;
            return this;
        }

        public Builder chat(Chat chat) {
            this.chat = chat;
            return this;
        }

        public Match build() {
            return new Match(this);
        }
    }

    //getters y setters
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
}
