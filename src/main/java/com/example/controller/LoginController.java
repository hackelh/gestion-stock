package com.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.model.UserDAO;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs");
            return;
        }

        if (userDAO.authenticate(username, password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/DashboardView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("Tableau de Bord - Gestion de Stock Auto");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                errorLabel.setText("Erreur lors du chargement du dashboard");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
            passwordField.clear();
        }
    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/RegisterView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Inscription - Gestion de Stock Auto");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            errorLabel.setText("Erreur lors du chargement de l'inscription");
            e.printStackTrace();
        }
    }
}