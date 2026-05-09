package com.example.uno_game.model;

import com.example.uno_game.model.enums.ActionSpecialType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "actions_speciales")
public class ActionSpecial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Partie partie;

    @ManyToOne
    private User joueurCible;

    @Enumerated(EnumType.STRING)
    private ActionSpecialType type;

    public ActionSpecial() {}
    public ActionSpecial(Partie partie, User joueurCible, ActionSpecialType type) {
        this.partie = partie;
        this.joueurCible = joueurCible;
        this.type = type;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Partie getPartie() { return partie; }
    public void setPartie(Partie partie) { this.partie = partie; }
    public User getJoueurCible() { return joueurCible; }
    public void setJoueurCible(User joueurCible) { this.joueurCible = joueurCible; }
    public ActionSpecialType getType() { return type; }
    public void setType(ActionSpecialType type) { this.type = type; }
}