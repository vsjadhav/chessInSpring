package com.springTut.chess.service;

import com.springTut.chess.Game;
import com.springTut.chess.Player;
import com.springTut.chess.repository.GameDao;
import com.springTut.chess.repository.PlayerDao;
import com.springTut.chess.service.helper.AllowedMoveCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChessServiceImpl implements ChessService {
    @Autowired
    private GameDao gameDao;

    @Autowired
    private PlayerDao playerDao;


    @Override
    public Object postPlayer(String name) {
        var player = new Player(name);
        playerDao.save(player);
        return player;
    }

    @Override
    public List<Player> getPlayers() {
        return playerDao.findAll();
    }

    @Override
    public List<Game> getGames() {
        return gameDao.findAll();
    }

    @Override
    public Game getGameById(int id) {
        return gameDao.findById(id).get();
    }

    @Override
    public Object specificPiecePosition(HashMap<String, Object> temp) {
        int game_Id = (Integer) temp.get("game_Id");
        int player_Id = (Integer) temp.get("player_Id");
        String piece = (String) temp.get("piece");
        Game game = gameDao.findById(game_Id).get();

        String keyForGivenPiece;
        if (player_Id == game.getP1_Id()) {
            keyForGivenPiece = "p1" + piece;
        } else {
            keyForGivenPiece = "p2" + piece;
        }

        return game.getPositions().get(keyForGivenPiece);
    }

    @Override
    public Object postGame(int id1, int id2) {
        var game = new Game(id1, id2);
        Set<Player> players = new HashSet<>(
                Arrays.asList(playerDao.findById(id1).get(), playerDao.findById(id2).get()));
        game.setEnrolledPlayers(players);
        gameDao.save(game);
        return game;
    }

    @Override
    public Object move(HashMap<String, Object> clientRequestJson) {
        int gameId = (Integer) clientRequestJson.get("game_Id");
        int playerId = (Integer) clientRequestJson.get("player_Id");
        Game game = gameDao.findById(gameId).get();
        Player player = playerDao.findById(playerId).get();

        int cur_turn_player;
        if (game.getTurn() == 1) {
            cur_turn_player = game.getP1_Id();
        } else {
            cur_turn_player = game.getP2_Id();
        }

        if (playerId != cur_turn_player) {
            return "you can not play the move\nIt is playerId:" + playerId + "'s turn\n" +
                    player.getName() + "'s turn";
        }
        var allowedMoveCalculator = new AllowedMoveCalculator(clientRequestJson, game, player);

        var varifyMove = allowedMoveCalculator.isMovePossible();
        if (varifyMove.get("success") == "0") {
            return varifyMove.get("value");
        } else {
            gameDao.save(game);
            return varifyMove.get("value");
        }
    }


}
