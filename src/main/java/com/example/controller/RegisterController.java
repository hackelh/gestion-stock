package com.example.controller;

import com.example.model.User;
import com.example.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister(ActionEvent event) {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (fullName.isBlank() || email.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            messageLabel.setText("Tous les champs sont obligatoires.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }
        if (userDAO.findByUsername(username) != null) {
            messageLabel.setText("Ce nom d'utilisateur existe déjà.");
            return;
        }
        try {
            userDAO.register(new User(fullName, username, email, password));
            messageLabel.setText("Inscription réussie. Vous pouvez vous connecter.");
            goToLogin(null);
        } catch (Exception e) {
            messageLabel.setText("Erreur lors de l'inscription.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Connexion - Gestion de Stock Auto");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            messageLabel.setText("Erreur lors du chargement de la connexion");
            e.printStackTrace();
        }
    }
}
