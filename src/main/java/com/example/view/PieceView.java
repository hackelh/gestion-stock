package com.example.view;

import javafx.scene.layout.VBox;
import com.example.controller.*;

public class PieceView {
    private VBox root;
    private PieceController controller;

    public PieceView() {
        controller = new PieceController();
        root = new VBox(10);
        // TableView à ajouter ici
    }

    public VBox getView() {
        return root;
    }
} 