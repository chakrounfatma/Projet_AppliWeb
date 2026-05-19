package com.example.uno_game.model;

import java.time.LocalDateTime;

import com.example.uno_game.model.enums.CarteType;
import com.example.uno_game.model.enums.PositionCarte;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartes")
public class Carte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String couleur;
    private int valeur;
    private LocalDateTime dateJoue;

    @Enumerated(EnumType.STRING)
    private CarteType type;

    @ManyToOne
    private User joueur; // si la carte est en main d’un joueur

    @ManyToOne
    private Partie partie;

    @Enumerated(EnumType.STRING)
    private PositionCarte position;

    public Carte() {}
    public Carte(String couleur, int valeur, CarteType type) {
        this.couleur = couleur;
        this.valeur = valeur;
        this.type = type;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public int getValeur() { return valeur; }
    public void setValeur(int valeur) { this.valeur = valeur; }
    public CarteType getType() { return type; }
    public void setType(CarteType type) { this.type = type; }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }
    public Partie getPartie() {
        return partie;
    }
     public PositionCarte getPosition() {
        return position;
    }
    
    public void setPosition(PositionCarte position) {
        this.position = position;
    }
    public void setJoueur(User joueur) {
        this.joueur = joueur;
    }
    public User getJoueur() {
        return joueur;
    }

    public LocalDateTime getDateJoue() {
        return dateJoue;
    }
    public void setDateJoue(LocalDateTime dateJoue) {
        this.dateJoue = dateJoue;
    }

}