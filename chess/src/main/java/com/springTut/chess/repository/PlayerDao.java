package com.springTut.chess.repository;

import com.springTut.chess.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerDao extends JpaRepository<Player, Integer> {
}
