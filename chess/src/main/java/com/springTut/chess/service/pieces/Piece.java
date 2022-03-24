package com.springTut.chess.service.pieces;

import com.springTut.chess.service.Position;

public interface Piece {
    boolean isAllowed(Position start, Position end);
        }
