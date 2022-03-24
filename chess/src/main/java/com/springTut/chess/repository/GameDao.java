package com.springTut.chess.repository;

import com.springTut.chess.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDao extends JpaRepository<Game, Integer> {
}
