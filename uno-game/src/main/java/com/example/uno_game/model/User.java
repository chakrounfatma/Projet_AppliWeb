package com.example.uno_game.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private String email;
    @JsonProperty(defaultValue = "0")
    private Integer scoreTotal = 0 ;
    
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "joueurSource")
    private List<Invitation> invitations;

    @JsonIgnore
    @OneToMany(mappedBy = "joueurCible")
    private List<ActionSpecial> actionsSpeciales;

    @JsonIgnore
    @OneToMany(mappedBy = "joueur")
    private List<Historique> historiques;

    @OneToMany(mappedBy = "expediteur")
    private List<Chat> messagesEnvoyes;

    @OneToMany(mappedBy = "destinataire")
    private List<Chat> messagesRecus;

    public User() {}
    public User(String nom, String prenom, String email, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.scoreTotal = 0;
        this.password = password;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getScoreTotal() { return scoreTotal; }
    public void setScoreTotal(Integer scoreTotal) { this.scoreTotal = scoreTotal; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}