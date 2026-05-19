package com.example.uno_game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uno_game.model.Carte;
import com.example.uno_game.model.Partie;
import com.example.uno_game.model.enums.PositionCarte;

@Repository
public interface CarteRepository extends JpaRepository<Carte, Integer> {
    public List<Carte> findByPartieAndPosition(Partie partie, PositionCarte position);
    List<Carte> findByJoueurIdAndPosition(int joueurId, PositionCarte position);
    List<Carte> findByPartieIdAndPosition(int partieId, PositionCarte position);
    List<Carte> findByJoueurIdAndPartieIdAndPosition(int joueurId,int partieId, PositionCarte position);
    Carte findTopByPartieIdAndPositionOrderByIdDesc(int partieId,PositionCarte position);
}

