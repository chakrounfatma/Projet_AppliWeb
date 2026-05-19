package com.example.uno_game.model;



import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat")

public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String message;
     
    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private User expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private User destinataire;

    private LocalDateTime date;

    public Chat() {}
    public Chat(String message, User expediteur, User destinataire, LocalDateTime date) {
        this.message = message;
        this.expediteur = expediteur;
        this.destinataire = destinataire;
        this.date = date;
        
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public User getExpediteur() { return expediteur; }
    public void setExpediteur(User expediteur) { this.expediteur = expediteur; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public User getDestinataire() {
        return destinataire;
    }
    public void setDestinataire(User destinataire) {
        this.destinataire = destinataire;
    }
}