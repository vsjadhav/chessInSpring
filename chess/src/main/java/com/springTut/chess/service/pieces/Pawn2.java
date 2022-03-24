package com.springTut.chess.service.pieces;

import com.springTut.chess.service.Position;

public class Pawn2 implements Piece {
    @Override
    public boolean isAllowed(Position start, Position end) {
        return false;
    }
}
