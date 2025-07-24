package com.example.controller;

import com.example.model.*;
import java.util.List;

public class PieceController {
    private PieceDAO pieceDAO = new PieceDAO();

    public List<Piece> getAllPieces() {
        return pieceDAO.getAllPieces();
    }

    public boolean addPiece(Piece piece) {
        return pieceDAO.addPiece(piece);
    }

    public boolean updatePiece(Piece piece) {
        return pieceDAO.updatePiece(piece);
    }

    public boolean deletePiece(int id) {
        return pieceDAO.deletePiece(id);
    }

    public Piece getPieceById(int id) {
        return pieceDAO.getPieceById(id);
    }
    // Méthodes CRUD à ajouter : addPiece, updatePiece, deletePiece
} 
