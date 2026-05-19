package com.example.uno_game.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uno_game.model.Carte;
import com.example.uno_game.model.Historique;
import com.example.uno_game.dto.HistoriqueDTO;
import com.example.uno_game.model.Partie;
import com.example.uno_game.model.User;
import com.example.uno_game.model.enums.CarteType;
import com.example.uno_game.model.enums.EtatPartie;
import com.example.uno_game.model.enums.PositionCarte;
import com.example.uno_game.model.enums.SensJeu;
import com.example.uno_game.repository.CarteRepository;
import com.example.uno_game.repository.HistoriqueRepository;
import com.example.uno_game.repository.PartieRepository;
import com.example.uno_game.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class GameService {
    @Autowired
    private PartieRepository partieRepository;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoriqueRepository historiqueRepository;

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
            carte.setDateJoue(LocalDateTime.now());
            aSauver.add(carte);
        }
    }

    // 3. carte sur table
    Carte first = deck.remove(0);
    first.setPosition(PositionCarte.DEFAUSSE);
    first.setPartie(partie);
    first.setDateJoue(LocalDateTime.now());
    partie.setCouleurActive(first.getCouleur());
    partie.setValeurActive(first.getValeur());
    aSauver.add(first);

    // 4. reste deck = pioche
    for (Carte c : deck) {
        c.setPosition(PositionCarte.PIOCHE);
        c.setJoueur(null);
        c.setPartie(partie);
        c.setDateJoue(LocalDateTime.now());
        aSauver.add(c);
    }

    // 5. sauvegarde UNIQUE
    carteRepository.saveAll(aSauver);

    // joueur initial
    partie.setJoueurActuel(joueurs.get(0));

    return partieRepository.save(partie);
}
public Carte getTopCard(int partieId) {
    return carteRepository.findTopByPartieIdAndPositionOrderByIdDesc(
            partieId,
            PositionCarte.DEFAUSSE
    );
}
// @Transactional
// public void jouerCarte(int joueurId, int carteId, String couleurChoisie) {

//     Carte carte = carteRepository.findById(carteId).orElseThrow();
//     Partie partie = carte.getPartie();

    
//     if (partie.getJoueurActuel().getId() != joueurId) {
//         throw new RuntimeException("Ce n'est pas ton tour !");
//     }

    
//     if (!estCarteValide(carte, partie)) {
//         throw new RuntimeException("Carte invalide !");
//     }
//     carte.setDateJoue(LocalDateTime.now());
//     carte.setPosition(PositionCarte.DEFAUSSE);
   
//     carteRepository.save(carte);


   
//     if (carte.getType() == CarteType.WILD || carte.getType() == CarteType.PLUS4) {
//         partie.setCouleurActive(couleurChoisie);
//         partie.setValeurActive(-1);
//     } else {
//         partie.setCouleurActive(carte.getCouleur());
//         partie.setValeurActive(carte.getValeur());
//     }

    
//     appliquerEffet(carte, partie);

   
//     passerTour(partie);

//     partieRepository.save(partie);
// }
@Transactional
public void jouerCarte(int joueurId, int carteId, String couleurChoisie) {

    Carte carte = carteRepository.findById(carteId).orElseThrow();
    Partie partie = carte.getPartie();

    if (partie.getJoueurActuel().getId() != joueurId) {
        throw new RuntimeException("Ce n'est pas ton tour !");
    }

    if (!estCarteValide(carte, partie)) {
        throw new RuntimeException("Carte invalide !");
    }

    carte.setDateJoue(LocalDateTime.now());
    carte.setPosition(PositionCarte.DEFAUSSE);
    //carte.setJoueur(null);
    carteRepository.save(carte);

    // update couleur active
    if (carte.getType() == CarteType.WILD || carte.getType() == CarteType.PLUS4) {
        partie.setCouleurActive(couleurChoisie);
        partie.setValeurActive(-1);
    } else {
        partie.setCouleurActive(carte.getCouleur());
        partie.setValeurActive(carte.getValeur());
    }

    // effet qui peut modifier le joueur courant
    boolean skipNormalTurn = appliquerEffet(carte, partie);

    // seulement si aucun effet spécial ne gère le tour
    if (!skipNormalTurn) {
        passerTour(partie);
    }
    User joueur = userRepository.findById(joueurId).orElseThrow();
    verifierGagnant(partie, joueur);

    partieRepository.save(partie);
}
private boolean estCarteValide(Carte carte, Partie partie) {
    return 
    (carte.getCouleur() != null && carte.getCouleur().equals(partie.getCouleurActive()))
    || (carte.getValeur() == partie.getValeurActive())
    || carte.getType() == CarteType.WILD
    || carte.getType() == CarteType.PLUS4;
}
// private void appliquerEffet(Carte carte, Partie partie) {

//     List<User> joueurs = partie.getListJoueur();
//     int index = joueurs.indexOf(partie.getJoueurActuel());

//     switch (carte.getType()) {

//         case SKIP:
//             index = (index + 2) % joueurs.size();
//             partie.setJoueurActuel(joueurs.get(index));
//             return;

//         case REVERSE:
//             partie.setSens(
//                 partie.getSens() == SensJeu.HORAIRE ?
//                 SensJeu.ANTI_HORAIRE :
//                 SensJeu.HORAIRE
//             );
//             break;

//         case PLUS2:
//             piocherCartes(joueurs.get((index + 1) % joueurs.size()), 2, partie);
//             break;

//         case PLUS4:
//             piocherCartes(joueurs.get((index + 1) % joueurs.size()), 4, partie);
//             break;
//     }
// }

private boolean appliquerEffet(Carte carte, Partie partie) {

    List<User> joueurs = partie.getListJoueur();
    int index = joueurs.indexOf(partie.getJoueurActuel());

    switch (carte.getType()) {

        case SKIP:
            index = (index + 2) % joueurs.size();
            partie.setJoueurActuel(joueurs.get(index));
            return true; // on a déjà géré le tour

        case REVERSE:
            partie.setSens(
                partie.getSens() == SensJeu.HORAIRE ?
                SensJeu.ANTI_HORAIRE :
                SensJeu.HORAIRE
            );
            return false;

        case PLUS2:
            User next = joueurs.get((index + 1) % joueurs.size());
            piocherCartes(next, 2, partie);
            return false;

        case PLUS4:
            User next4 = joueurs.get((index + 1) % joueurs.size());
            piocherCartes(next4, 4, partie);
            return false;

        default:
            return false;
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
@Transactional
public Carte piocherUneCarte( int partieId) {

    Partie partie = partieRepository.findById(partieId).orElseThrow();

    User joueur = partie.getJoueurActuel();

    List<Carte> pioche = carteRepository.findByPartieAndPosition(
            partie,
            PositionCarte.PIOCHE
    );

    if (pioche.isEmpty()) {
        throw new RuntimeException("La pioche est vide !");
    }

    Collections.shuffle(pioche);

    Carte carte = pioche.get(0);

    carte.setJoueur(joueur);
    carte.setPosition(PositionCarte.MAIN);

    carteRepository.save(carte);

    return carte;
}
private void verifierGagnant(Partie partie, User joueur) {

    List<Carte> cartes = carteRepository.findByJoueurIdAndPartieIdAndPosition(
            joueur.getId(),
            partie.getId(),
            PositionCarte.MAIN
    );

    if (cartes.isEmpty()) {

        Historique h = new Historique();
        h.setPartie(partie);
        h.setJoueur(joueur);
        h.setDate(LocalDateTime.now());

        historiqueRepository.save(h);

        partie.setEtat(EtatPartie.terminee);
        partieRepository.save(partie);
    }
}
public List<HistoriqueDTO> getHistorique() {
    return historiqueRepository.findHistorique();
}
}
