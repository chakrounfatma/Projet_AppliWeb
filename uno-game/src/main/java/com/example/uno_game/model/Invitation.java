package com.example.uno_game.model;
import com.example.uno_game.model.enums.EtatInvitation;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invitations")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User joueurSource;

    @ManyToOne
    private User joueurCible;

    @Enumerated(EnumType.STRING)
    private EtatInvitation etat;

    public Invitation() {}
    
    public Invitation(User joueur_source, User joueur_cible, EtatInvitation etat) {
        this.joueurSource = joueur_source;
        this.joueurCible = joueur_cible;
        this.etat = etat;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getJoueur_source() { return joueurSource; }
    public void setJoueur_source(User joueur_source) { this.joueurSource = joueur_source; }
    public User getJoueur_cible() { return joueurCible; }
    public void setJoueur_cible(User joueur_cible) { this.joueurCible = joueur_cible; }
    public EtatInvitation getEtat() { return etat; }
    public void setEtat(EtatInvitation etat) { this.etat = etat; }
}