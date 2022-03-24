package com.springTut.chess.service.helper;

import com.springTut.chess.Game;
import com.springTut.chess.Player;
import com.springTut.chess.repository.GameDao;
import com.springTut.chess.repository.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AllowedMoveCalculator {

//    @Autowired
//    private GameDao gameDao;
//
//    @Autowired
//    private PlayerDao playerDao;

    private int gameId;
    private int playerId;
    private String piece;
    private int newXPos;
    private int newYPos;
    private String keyForGivenPiece;
    private List<Integer> cur_pos;
    private String pieceOnNewPos;
    private String curPlayerAsInGameClass;

    private Game game;
    private Player player;

    public AllowedMoveCalculator(HashMap<String, Object> clientRequestJson, Game game, Player player){
        this.gameId = (Integer)clientRequestJson.get("game_Id");
        this.playerId = (Integer)clientRequestJson.get("player_Id");
        this.piece = (String)clientRequestJson.get("piece");
        this.newXPos = (Integer)clientRequestJson.get("new_x_pos");
        this.newYPos = (Integer)clientRequestJson.get("new_y_pos");

        if (playerId == game.getP1_Id()){this.keyForGivenPiece = "p1"+piece;}
        else{this.keyForGivenPiece = "p2"+ piece;}

        this.cur_pos = game.getPositions().get(keyForGivenPiece);
        this.pieceOnNewPos = game.getKeyByValue(Arrays.asList(newXPos,newYPos));
        this.curPlayerAsInGameClass = keyForGivenPiece.substring(1,2); // gives 1 or 2 as in p1 or p2

        this.game = game;   //gameDao.findById(gameId).get();
        this.player = player;    //playerDao.findById(playerId).get();
    }


    private Object isNewPosEmpty(){return  null;}

    public HashMap<String,String> isMovePossible(){
        String pieceName = piece.substring(0,piece.length()-1);
        HashMap<String,String> isPossible;
        if (pieceName.equalsIgnoreCase("knight")) {
            isPossible = isMovePossibleForKnight();
        }
        else if (pieceName.equalsIgnoreCase("bishop")) {
            isPossible = isMovePossibleForBishop();
        }
        else if (pieceName.equalsIgnoreCase("rook")) {
            isPossible = isMovePossibleForRook();
        }
        else if (pieceName.equalsIgnoreCase("queen")) {
            isPossible = isMovePossibleForQueen();
        }
        else if (pieceName.equalsIgnoreCase("king")) {
            isPossible = isMovePossibleForKing();
        }
        else {
            isPossible = isMovePossibleForPawn();
        }


        if (isPossible.get("success") == "1"){
            String msg;
            if (pieceOnNewPos == null){
                game.getPositions().put(keyForGivenPiece, Arrays.asList(newXPos,newYPos));
                game.flipTurn();
                msg = keyForGivenPiece + " moved successfully to specified position\n"+"no casualty";
                return new HashMap<>(){{put("success", "1");put("value", msg);}};
            }
            else if (pieceOnNewPos.substring(1, 2).equals(curPlayerAsInGameClass)) {
                msg = "move not possible, your piece \""
                        + pieceOnNewPos.substring(2) + "\" is already placed on the position to which you want to move";
                return new HashMap<>(){{put("success", "0");put("value", msg);}};
            }
            else{
                game.getPositions().put(pieceOnNewPos, null);
                game.getPositions().put(keyForGivenPiece, Arrays.asList(newXPos,newYPos));
                game.flipTurn();
                msg = keyForGivenPiece+" moved successfully\n"+"killed "+pieceOnNewPos;
                return new HashMap<>(){{put("success", "1");put("value", msg);}};
            }
        }
        else{
            return new HashMap<>(){{put("success", "0");put("value", isPossible.get("value"));}};
        }
    }

    public boolean isPathClear(int x_increment, int y_increment){
        int x = cur_pos.get(0);
        int y = cur_pos.get(1);
        if (x_increment==0){
            while ((y+y_increment) != newYPos) {
                y = y + y_increment;
                if (game.getKeyByValue(Arrays.asList(x, y)) != null) {
                    return false;
                }
            }
        }
        else if (y_increment==0){
            while ((x+x_increment) != newXPos) {
                x = x + x_increment;
                if (game.getKeyByValue(Arrays.asList(x, y)) != null) {
                    return false;
                }
            }
        }
        else{
            while ((x+x_increment) != newXPos){
                x = x + x_increment;
                y = y + y_increment;
                if (game.getKeyByValue(Arrays.asList(x,y)) != null){
                    return false;
                }
            }
        }
        return true;
    }

    private HashMap<String,String> isMovePossibleForKnight(){
        String msg;
        System.out.println("cur_pos: "+ cur_pos + "\nnewX:"+newXPos+"\tnewY:"+newYPos);
        if((Math.abs(cur_pos.get(0) - newXPos) == 2) && (Math.abs(cur_pos.get(1) - newYPos) == 1)){
            return new HashMap<>(){{put("success", "1");}};
        }
        else if((Math.abs(cur_pos.get(1) - newYPos) == 2) && (Math.abs(cur_pos.get(0) - newXPos) == 1)){
            System.out.println("x1-x=1");
            return new HashMap<>(){{put("success", "1");}};
        }
        else{
            msg = "move failed\nKnight can only move in two and half pattern";
            return new HashMap<>(){{put("success", "0");put("value", msg);}};
        }
    }

    private HashMap<String,String> isMovePossibleForBishop(){
        String msg;
        boolean isPathClear;
        if ((Math.abs(cur_pos.get(0) - newXPos) == Math.abs(cur_pos.get(1) - newYPos))){
            if((newXPos-cur_pos.get((0))>0) && (newYPos-cur_pos.get((1))>0)){
                isPathClear = isPathClear(1,1);
            }
            else if((newXPos-cur_pos.get((0))>0) && (newYPos-cur_pos.get((1))<0)){
                isPathClear = isPathClear(1,-1);
            }
            else if((newXPos-cur_pos.get((0))<0) && (newYPos-cur_pos.get((1))<0)){
                isPathClear = isPathClear(-1,-1);
            }
            else{
                isPathClear = isPathClear(-1,1);
            }
            if (isPathClear == false){
                msg = "move failed\n" +
                        "there is/are one/more obsticals in the path to reach to the destination specified";
                return new HashMap<>(){{put("success", "0");put("value", msg);}};
            }
            else{
                return new HashMap<>(){{put("success", "1");}};
            }
        }
        else{
            msg = "move failed\nBishop can only move diagonally";
            return new HashMap<>(){{put("success", "0");put("value", msg);}};
        }
    }

    private HashMap<String,String> isMovePossibleForRook(){
        String msg;
        boolean isPathClear = false;
        if (((cur_pos.get(0) != newXPos) && (cur_pos.get(1) == newYPos))) {
            if ((newXPos - cur_pos.get((0)) > 0)) {
                isPathClear = isPathClear(1, 0);
            }
            else if ((newXPos - cur_pos.get((0)) < 0)) {
                isPathClear = isPathClear(-1, 0);
            }
        }
        else if (((cur_pos.get(1) != newYPos) && (cur_pos.get(0) == newXPos))){
            if ((newYPos - cur_pos.get((1)) > 0)) {
                isPathClear = isPathClear(0, 1);
            }
            else if ((newYPos - cur_pos.get((1)) < 0)) {
                isPathClear = isPathClear(0, -1);
            }
        }
        else{
            msg = "move failed\nBishop can only move diagonally";
            return new HashMap<>(){{put("success", "0");put("value", msg);}};
        }

        if (isPathClear == false){
            msg = "move failed\n" +
                    "there is/are one/more obsticals in the path to reach to the destination specified";
            return new HashMap<>(){{put("success", "0");put("value", msg);}};
        }
        else{
            return new HashMap<>(){{put("success", "1");}};
        }
    }

    private HashMap<String,String> isMovePossibleForPawn(){
        String msg;
        String direction = keyForGivenPiece.substring(1,2);
        if (direction.equalsIgnoreCase("1")){
            if ((newYPos - (cur_pos.get(1)) == 1) && (cur_pos.get(0) - newXPos==0)){
                return new HashMap<>(){{put("success", "1");}};
            }
            else if (((newYPos - cur_pos.get(1)) == 1) && (Math.abs(cur_pos.get(0) - newXPos)==1)){
                if (pieceOnNewPos != null){
                    return new HashMap<>(){{put("success", "1");}};
                }
                else{
                    msg = "move failed\nPawn can move diagonally only to kill opponent\n" +
                            "but here no one to kill in diagonal position";
                    return new HashMap<>(){{put("success", "0");put("value", msg);}};
                }
            }
            else{
                msg = "move failed\nPawn does not move like that\n" +
                        "Pawn can only move in one direction by one step or two steps for initial move";
                return new HashMap<>(){{put("success", "0");put("value", msg);}};
            }
        }
        else {
            if ((newYPos - (cur_pos.get(1)) == -1) && (cur_pos.get(0) - newXPos==0)){
                return new HashMap<>(){{put("success", "1");}};
            }
            else if (((newYPos - cur_pos.get(1)) == -1) && (Math.abs(cur_pos.get(0) - newXPos)==1)){
                if (pieceOnNewPos != null){
                    return new HashMap<>(){{put("success", "1");}};
                }
                else{
                    msg = "move failed\nPawn can move diagonally only to kill opponent\n" +
                            "but here no one to kill in diagonal position";
                    return new HashMap<>(){{put("success", "0");put("value", msg);}};
                }
            }
            else{
                msg = "move failed\nPawn does not move like that\n" +
                        "Pawn can only move in one direction by one step or two steps for initial move";
                return new HashMap<>(){{put("success", "0");put("value", msg);}};
            }
        }
    }

    private HashMap<String,String> isMovePossibleForQueen(){
        return null;
    }

    private HashMap<String,String> isMovePossibleForKing(){
        return null;
    }

}
