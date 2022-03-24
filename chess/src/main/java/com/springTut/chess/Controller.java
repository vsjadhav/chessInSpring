package com.springTut.chess;


import com.springTut.chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private ChessService cs;


    @GetMapping("/home")
    public String home() {
        return "this is home page";
    }

    @GetMapping("/players")
    public List<Player> getPlayers() {return cs.getPlayers();}

    @GetMapping("/games")
    public List<Game> getGames() {return cs.getGames();}

    @GetMapping("/specificPiecePosition")
    public Object specificPiecePosition(@RequestBody HashMap<String, Object> temp) {
        return cs.specificPiecePosition(temp);
    }

    @GetMapping("/game/{id}")
    public Game getGames(@PathVariable int id) {return cs.getGameById(id);}

    @PostMapping("/player/{name}")
    public Object postPlayer(@PathVariable String name) {
        return cs.postPlayer(name);
    }

    @PostMapping("/start_game/{id1}/{id2}")
    public Object postGame(@PathVariable int id1, @PathVariable int id2) {
        return cs.postGame(id1, id2);
    }

    @PostMapping("/move")
    public Object move(@RequestBody HashMap<String, Object> temp) {
        return cs.move(temp);
    }

}
