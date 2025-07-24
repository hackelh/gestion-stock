package com.example.view;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import com.example.controller.*;

public class DashboardView {
    private VBox root;
    private DashboardController controller;

    public DashboardView() {
        controller = new DashboardController();
        root = new VBox(10);
        Label totalPieces = new Label("Total pièces : " + controller.getTotalPieces());
        // Ajouter d'autres labels pour les alertes, obsolescence, etc.
        root.getChildren().addAll(totalPieces);
    }

    public VBox getView() {
        return root;
    }
}