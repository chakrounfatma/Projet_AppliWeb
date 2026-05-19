package com.example.uno_game.dto;

public class HistoriqueDTO {

    private String joueur;
    private String partie;
    private String date;

    public HistoriqueDTO(String joueur, String partie, String date) {
        this.joueur = joueur;
        this.partie = partie;
        this.date = date;
    }

    public String getJoueur() {
        return joueur;
    }

    public String getPartie() {
        return partie;
    }

    public String getDate() {
        return date;
    }

    public void setJoueur(String joueur) {
        this.joueur = joueur;
    }

    public void setPartie(String partie) {
        this.partie = partie;
    }

    public void setDate(String date) {
        this.date = date;
    }
}