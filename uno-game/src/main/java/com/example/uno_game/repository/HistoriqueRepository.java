package com.example.uno_game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.uno_game.model.Historique;
import com.example.uno_game.dto.HistoriqueDTO;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, Integer> {
    List<Historique> findAllByOrderByDateDesc();
    @Query("""
        SELECT new com.example.uno_game.dto.HistoriqueDTO(
            CONCAT(h.joueur.prenom, ' ', h.joueur.nom),
            CONCAT('Partie ', h.partie.id),
            CAST(h.date AS string)
        )
        FROM Historique h
        ORDER BY h.date DESC
    """)

    List<HistoriqueDTO> findHistorique();
}