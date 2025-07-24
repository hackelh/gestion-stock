package com.example.controller;

import com.example.model.*;

public class DashboardController {
    private PieceDAO pieceDAO = new PieceDAO();

    public int getTotalPieces() {
        return pieceDAO.getAllPieces().size();
    }
    // Autres méthodes pour seuil critique, obsolescence, etc. à ajouter
} 