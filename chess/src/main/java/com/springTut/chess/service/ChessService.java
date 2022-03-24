package com.springTut.chess.service;

import com.springTut.chess.Game;
import com.springTut.chess.Player;

import java.util.HashMap;
import java.util.List;

public interface ChessService {

    Object postPlayer(String name);

    List<Player> getPlayers();

    Object postGame(int id1, int id2);

    Object move(HashMap<String, Object> temp);

    List<Game> getGames();

    Game getGameById(int id);

    Object specificPiecePosition(HashMap<String, Object> temp);
}
