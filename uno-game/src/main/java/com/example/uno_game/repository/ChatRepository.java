package com.example.uno_game.repository;

import com.example.uno_game.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findByDestinataireId(Integer id);
    List<Chat> findByExpediteur_Email(String email);

    List<Chat> findByDestinataire_EmailOrderByDateDesc(String email);
}