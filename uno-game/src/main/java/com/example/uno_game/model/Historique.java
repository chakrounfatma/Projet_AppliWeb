package com.example.uno_game.model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Historique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    private Partie partie;

    @ManyToOne
    private User joueur;

    private LocalDateTime date;

    public Historique() {}
    public Historique(Partie partie, User joueur, LocalDateTime date) {
        this.partie = partie;
        this.joueur = joueur;
        this.date = date;
    }

    public Partie getPartie() {
        return partie;
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public User getJoueur() {
        return joueur;
    }

    public void setJoueur(User joueur) {
        this.joueur = joueur;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}