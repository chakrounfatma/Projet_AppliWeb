package com.example.uno_game.repository;

import com.example.uno_game.model.ActionSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionSpecialRepository extends JpaRepository<ActionSpecial, Integer> {}