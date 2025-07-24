package com.example.controller;

import com.example.model.*;
import java.util.List;

public class PieceController {
    private PieceDAO pieceDAO = new PieceDAO();

    public List<Piece> getAllPieces() {
        return pieceDAO.getAllPieces();
    }

    public void addPiece(Piece piece) {
        pieceDAO.addPiece(piece);
    }

    public void updatePiece(Piece piece) {
        pieceDAO.updatePiece(piece);
    }

    public void deletePiece(int id) {
        pieceDAO.deletePiece(id);
    }

    public Piece getPieceById(int id) {
        return pieceDAO.getPieceById(id);
    }
    // Méthodes CRUD à ajouter : addPiece, updatePiece, deletePiece
} 
