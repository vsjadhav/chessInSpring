package com.springTut.chess;


import com.springTut.chess.service.Position;
import com.springTut.chess.service.pieces.Piece;
import lombok.NoArgsConstructor;

import javax.lang.model.element.Name;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Entity
@Table(name = "game")
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gameId;
    private int p1_Id;
    private int p2_Id;
    private String status;
    private int turn;

    @Column(columnDefinition="blob(1024)")
    private HashMap<String, List<Integer>> positions = new HashMap<String, List<Integer>>();


    @ManyToMany
    @JoinTable(
            name = "enrolled_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> enrolledPlayers = new HashSet<>();

    public Game(int p1, int p2){
        this.p1_Id = p1;
        this.p2_Id = p2;
        this.status = "ongoing";
        this.turn = 1;
        initChessBoardPositions();
    }

    private void initChessBoardPositions(){
        var keys = new ArrayList<String>(
                Arrays.asList("rook1", "knight1", "bishop1", "queen", "king", "bishop2", "knight2", "rook2"));
        String pawn = "pawn";

        for (int i : Arrays.asList(1,2,7,8)){
            for(int j=1;j<=8;j++) {
                if (i==1){
                    positions.put(("p1"+keys.get(j-1)),Arrays.asList(i,j));
                }
                else if (i==8){
                    positions.put(("p2"+keys.get(j-1)),Arrays.asList(i,j));
                }
                else if (i==2){
                    positions.put(("p1"+pawn+ String.valueOf(j)),Arrays.asList(i,j));
                }
                else{
                    positions.put(("p2"+pawn+ String.valueOf(j)),Arrays.asList(i,j));
                }
            }
        }
        System.out.println(positions);
    }

//    void movePiece(Piece piece, ) {
//        if(piece.isAllowed(new Position(0, 0). new com.springTut.chess.service.Position()))
//    }

    public void flipTurn(){
        if (this.turn == 1){
            this.turn = 2;
        }
        else {this.turn = 1;}
    }

    public String getKeyByValue(List<Integer> value) {
        for (Map.Entry<String, List<Integer>> entry : positions.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    public Game(Set<Player> players){
        this.enrolledPlayers = players;
    }

    public Set<Player> getEnrolledPlayers() {
        return enrolledPlayers;
    }

    public int getP1_Id() {
        return p1_Id;
    }

    public int getP2_Id() {
        return p2_Id;
    }

    public int getGameId() {
        return gameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEnrolledPlayers(Set<Player> enrolledPlayers) {
        this.enrolledPlayers = enrolledPlayers;
    }

    public HashMap<String, List<Integer>> getPositions() {
        return positions;
    }

    public void setPositions(HashMap<String, List<Integer>> positions) {
        this.positions = positions;
    }

    public int getTurn() {
        return turn;
    }

}
