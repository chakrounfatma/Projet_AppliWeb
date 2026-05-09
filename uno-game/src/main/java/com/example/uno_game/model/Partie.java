package com.example.uno_game.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.uno_game.model.enums.EtatPartie;
import com.example.uno_game.model.enums.SensJeu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parties")
public class Partie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatPartie etat;

    @ManyToMany
    private List<User> listJoueur;

    @ManyToOne
    private User joueurActuel;

    @Enumerated(EnumType.STRING)
    private SensJeu sens;

    private String couleurActive;

    private Integer valeurActive;

    public Partie() {}
     public Partie(LocalDateTime dateDebut, LocalDateTime dateFin, EtatPartie etat) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.etat = etat;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    public EtatPartie getEtat() { return etat; }
    public void setEtat(EtatPartie etat) { this.etat = etat; }
    public List<User> getListJoueur() { return listJoueur; }
    public void setListJoueur(List<User> listJoueur) { this.listJoueur = listJoueur; }
    public User getJoueurActuel() { return joueurActuel; }
    public void setJoueurActuel(User joueurActuel) { this.joueurActuel = joueurActuel; }
    public SensJeu getSens() { return sens; }   
    public void setSens(SensJeu sens) { this.sens = sens; }
    public String getCouleurActive() { return couleurActive; }
    public void setCouleurActive(String couleurActive) { this.couleurActive = couleurActive; }
    public Integer getValeurActive() { return valeurActive; }
    public void setValeurActive(Integer valeurActive) { this.valeurActive = valeurActive; }
    
}