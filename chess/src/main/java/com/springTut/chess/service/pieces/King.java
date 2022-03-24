package com.springTut.chess.service.pieces;

import com.springTut.chess.service.Position;

public class King implements Piece {

    @Override
    public boolean isAllowed(Position start, Position end) {
        if(((start.getX() - end.getX()) == 1 || (start.getX() - end.getX()) == -1) &&
                ((start.getY() - end.getY()) == 0 ))
            return true;

        if(((start.getY() - end.getY()) == 1 || (start.getY() - end.getY()) == -1) &&
                ((start.getX() - end.getX()) == 0 ))
            return true;

        return false;
    }
}
