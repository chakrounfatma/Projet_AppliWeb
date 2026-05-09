package com.example.uno_game.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uno_game.model.Carte;
import com.example.uno_game.model.Partie;
import com.example.uno_game.model.enums.PositionCarte;
import com.example.uno_game.repository.CarteRepository;
import com.example.uno_game.repository.PartieRepository;
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
    @PostMapping("/play")
    public void jouerCarte(
            @RequestParam int joueurId,
            @RequestParam int carteId,
            @RequestParam(required = false) String couleur
    ) {
        gameService.jouerCarte(joueurId, carteId, couleur);
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
}

