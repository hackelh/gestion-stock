package com.example;

import com.example.view.DashboardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DashboardView dashboard = new DashboardView();
        Scene scene = new Scene(dashboard.getView(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion de Stock - Quincaillerie Mécanique");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); 
    }
}
