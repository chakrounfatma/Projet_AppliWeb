package com.example.uno_game.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uno_game.model.Carte;
import com.example.uno_game.model.Partie;
import com.example.uno_game.model.User;
import com.example.uno_game.model.enums.CarteType;
import com.example.uno_game.model.enums.EtatPartie;
import com.example.uno_game.model.enums.PositionCarte;
import com.example.uno_game.model.enums.SensJeu;
import com.example.uno_game.repository.CarteRepository;
import com.example.uno_game.repository.PartieRepository;
import com.example.uno_game.repository.UserRepository;

@Service
public class GameService {
    @Autowired
    private PartieRepository partieRepository;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private UserRepository userRepository;

    private List<Carte> genererDeck(Partie partie) {
    List<Carte> deck = new ArrayList<>();

    String[] couleurs = {"ROUGE", "BLEU", "VERT", "JAUNE"};

    for (String couleur : couleurs) {

        // cartes 0 (1 seule)
        deck.add(new Carte(couleur, 0, CarteType.NUMBER));

        // cartes 1-9 (2 fois)
        for (int i = 1; i <= 9; i++) {
            deck.add(new Carte(couleur, i, CarteType.NUMBER));
            deck.add(new Carte(couleur, i, CarteType.NUMBER));
        }

        // actions spéciales
        for (int i = 0; i < 2; i++) {
            deck.add(new Carte(couleur, -1, CarteType.SKIP));
            deck.add(new Carte(couleur, -1, CarteType.REVERSE));
            deck.add(new Carte(couleur, -1, CarteType.PLUS2));
        }
    }

    // cartes spéciales sans couleur
    for (int i = 0; i < 4; i++) {
        deck.add(new Carte(null, -1, CarteType.WILD));
        deck.add(new Carte(null, -1, CarteType.PLUS4));
    }

    // associer à la partie + position pioche
    for (Carte c : deck) {
        c.setPartie(partie);
        c.setPosition(PositionCarte.PIOCHE);
    }

    return deck;
}
public Partie createPartie(List<Integer> userIds) {

    List<User> joueurs = userRepository.findAllById(userIds);

    Partie partie = new Partie();
    partie.setDateDebut(LocalDateTime.now());
    partie.setEtat(EtatPartie.en_cours);
    partie.setListJoueur(joueurs);
    partie.setSens(SensJeu.HORAIRE);

    partieRepository.save(partie);

    // 1. générer deck
    List<Carte> deck = genererDeck(partie);
    Collections.shuffle(deck);

    List<Carte> aSauver = new ArrayList<>();

    // 2. distribuer cartes
    for (User joueur : joueurs) {
        for (int i = 0; i < 7; i++) {
            Carte carte = deck.remove(0);
            carte.setJoueur(joueur);
            carte.setPosition(PositionCarte.MAIN);
            carte.setPartie(partie);
            aSauver.add(carte);
        }
    }

    // 3. carte sur table
    Carte first = deck.remove(0);
    first.setPosition(PositionCarte.DEFAUSSE);
    first.setPartie(partie);
    partie.setCouleurActive(first.getCouleur());
    partie.setValeurActive(first.getValeur());
    aSauver.add(first);

    // 4. reste deck = pioche
    for (Carte c : deck) {
        c.setPosition(PositionCarte.PIOCHE);
        c.setJoueur(null);
        c.setPartie(partie);
        aSauver.add(c);
    }

    // 5. sauvegarde UNIQUE
    carteRepository.saveAll(aSauver);

    // joueur initial
    partie.setJoueurActuel(joueurs.get(0));

    return partieRepository.save(partie);
}
public void jouerCarte(int joueurId, int carteId, String couleurChoisie) {

    Carte carte = carteRepository.findById(carteId).orElseThrow();
    Partie partie = carte.getPartie();

    
    if (partie.getJoueurActuel().getId() != joueurId) {
        throw new RuntimeException("Ce n'est pas ton tour !");
    }

    
    if (!estCarteValide(carte, partie)) {
        throw new RuntimeException("Carte invalide !");
    }

    
    carte.setPosition(PositionCarte.DEFAUSSE);
    carte.setJoueur(null);

   
    if (carte.getType() == CarteType.WILD || carte.getType() == CarteType.PLUS4) {
        partie.setCouleurActive(couleurChoisie);
    } else {
        partie.setCouleurActive(carte.getCouleur());
        partie.setValeurActive(carte.getValeur());
    }

    
    appliquerEffet(carte, partie);

   
    passerTour(partie);

    carteRepository.save(carte);
    partieRepository.save(partie);
}
private boolean estCarteValide(Carte carte, Partie partie) {
    return 
    (carte.getCouleur() != null && carte.getCouleur().equals(partie.getCouleurActive()))
    || (carte.getValeur() == partie.getValeurActive())
    || carte.getType() == CarteType.WILD
    || carte.getType() == CarteType.PLUS4;
}
private void appliquerEffet(Carte carte, Partie partie) {

    List<User> joueurs = partie.getListJoueur();
    int index = joueurs.indexOf(partie.getJoueurActuel());

    switch (carte.getType()) {

        case SKIP:
            index = (index + 2) % joueurs.size();
            partie.setJoueurActuel(joueurs.get(index));
            return;

        case REVERSE:
            partie.setSens(
                partie.getSens() == SensJeu.HORAIRE ?
                SensJeu.ANTI_HORAIRE :
                SensJeu.HORAIRE
            );
            break;

        case PLUS2:
            piocherCartes(joueurs.get((index + 1) % joueurs.size()), 2, partie);
            break;

        case PLUS4:
            piocherCartes(joueurs.get((index + 1) % joueurs.size()), 4, partie);
            break;
    }
}
private void passerTour(Partie partie) {

    List<User> joueurs = partie.getListJoueur();
    int index = joueurs.indexOf(partie.getJoueurActuel());

    if (partie.getSens() == SensJeu.HORAIRE) {
        index = (index + 1) % joueurs.size();
    } else {
        index = (index - 1 + joueurs.size()) % joueurs.size();
    }

    partie.setJoueurActuel(joueurs.get(index));
}
private void piocherCartes(User joueur, int nb, Partie partie) {

    List<Carte> pioche = carteRepository.findByPartieAndPosition(partie, PositionCarte.PIOCHE);

    if (pioche.size() < nb) {
        throw new RuntimeException("Pas assez de cartes dans la pioche !");
    }

    List<Carte> cartesPiochees = new ArrayList<>();

    for (int i = 0; i < nb; i++) {
        Carte c = pioche.get(i);
        c.setJoueur(joueur);
        c.setPosition(PositionCarte.MAIN);
        cartesPiochees.add(c);
    }

    carteRepository.saveAll(cartesPiochees);
}
}
