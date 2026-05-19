package com.example.uno_game.Controller;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uno_game.model.Carte;
import com.example.uno_game.model.Historique;
import com.example.uno_game.dto.HistoriqueDTO;
import com.example.uno_game.model.Partie;
import com.example.uno_game.model.User;
import com.example.uno_game.model.enums.EtatPartie;
import com.example.uno_game.model.enums.PositionCarte;
import com.example.uno_game.repository.CarteRepository;
import com.example.uno_game.repository.PartieRepository;
import com.example.uno_game.repository.UserRepository;
import com.example.uno_game.service.GameService;

@RestController
@RequestMapping("/game")
@CrossOrigin
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private CarteRepository carteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartieRepository partieRepository;
    @GetMapping("/debug/hand/{id}")
    public List<Carte> debug(@PathVariable int id) {
        return carteRepository.findAll();
    }

    @PostMapping("/create")
    public Partie createPartie(@RequestBody List<Integer> userIds) {
        return gameService.createPartie(userIds);
    }
    @GetMapping("/hand/{joueurId}")
    public List<Carte> getMain(@PathVariable int joueurId) {
        return carteRepository.findByJoueurIdAndPosition(joueurId, PositionCarte.MAIN);
    }
    @GetMapping("/top-card/{partieId}")
    public Carte getCarteTable(@PathVariable int partieId) {
        return carteRepository.findByPartieIdAndPosition(partieId, PositionCarte.DEFAUSSE)
                .stream()
                .reduce((first, second) -> second) 
                .orElse(null);
    }
//     @GetMapping("/top/{partieId}")
//     public Carte getTopCard(@PathVariable int partieId) {
//     return carteRepository
//         .findByPartieIdAndPosition(partieId, PositionCarte.DEFAUSSE)
//         .stream()
//         .max((a, b) -> Integer.compare(a.getId(), b.getId()))
//         .orElse(null);
// }
@GetMapping("/top/{partieId}")
public Carte getTopCard(@PathVariable Integer partieId) {

    return carteRepository
            .findByPartieIdAndPosition(partieId, PositionCarte.DEFAUSSE)
            .stream()
            .filter(Objects::nonNull)
            .filter(c -> c.getDateJoue() != null)
            .max(Comparator.comparing(Carte::getDateJoue))
            .orElse(null);
}
   @PostMapping("/play")
    public ResponseEntity<Void> jouerCarte(
            @RequestParam int joueurId,
            @RequestParam int carteId,
            @RequestParam(required = false) String couleur
    ) {
        gameService.jouerCarte(joueurId, carteId, couleur);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/state/{partieId}")
    public Partie getEtatPartie(@PathVariable int partieId) {
        return partieRepository.findById(partieId).orElse(null);
    }
    @GetMapping("/hand/{joueurId}/{partieId}")
    public List<Carte> getMain( @PathVariable int joueurId, @PathVariable int partieId) {

        return carteRepository
                .findByJoueurIdAndPartieIdAndPosition(
                        joueurId,
                        partieId,
                        PositionCarte.MAIN
                );
    }

    @PostMapping("/draw")
    public Carte piocher(@RequestParam int joueurId,@RequestParam int partieId) {
        return gameService.piocherUneCarte( partieId);
    }
    @GetMapping("/random-player")
    public User getRandomPlayer() {
        List<User> users = userRepository.findAll();
        return users.get(new Random().nextInt(users.size()));
    }
    @PostMapping("/reset/{partieId}")
    public void resetPartie(@PathVariable int partieId) {
        Partie partie = partieRepository.findById(partieId).orElseThrow();

        partie.setEtat(EtatPartie.en_cours);
        partie.setJoueurActuel(null);
        partie.setCouleurActive(null);
        partie.setValeurActive(null);

        partieRepository.save(partie);
    }
    @GetMapping("/historique")
    public List<HistoriqueDTO> getHistorique() {
        return gameService.getHistorique();
    }
}

