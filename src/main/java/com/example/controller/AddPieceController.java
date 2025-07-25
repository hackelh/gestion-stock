package com.example.controller;

import com.example.model.Piece;
import com.example.model.PieceDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

public class AddPieceController {

    @FXML private TextField nomField;
    @FXML private ComboBox<String> categorieComboBox;
    @FXML private TextField referenceField;
    @FXML private TextField marqueField;
    @FXML private TextField typeSpecificiteField;
    @FXML private TextField prixUnitaireField;
    @FXML private Spinner<Integer> quantiteSpinner;
    @FXML private Spinner<Integer> seuilCritiqueSpinner;
    @FXML private TextField emplacementField;
    @FXML private TextArea descriptionArea;
    @FXML private Button ajouterButton;

    private PieceDAO pieceDAO;
    private Piece pieceToModify;

    @FXML
    public void initialize() {
        pieceDAO = new PieceDAO();

        // Initialiser la ComboBox des catégories
        categorieComboBox.setItems(FXCollections.observableArrayList(
            "Moteur", "Freinage", "Suspension", "Transmission", "Échappement", 
            "Filtration", "Éclairage", "Carrosserie", "Accessoires", "Liquides"
        ));

        // Initialiser le Spinner pour la quantité
        // Initialiser les Spinners pour la quantité et le seuil
        SpinnerValueFactory<Integer> quantiteValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 1);
        quantiteSpinner.setValueFactory(quantiteValueFactory);

        SpinnerValueFactory<Integer> seuilValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 5);
        seuilCritiqueSpinner.setValueFactory(seuilValueFactory);
    }

    public void setPieceToModify(Piece piece) {
        this.pieceToModify = piece;
        nomField.setText(piece.getNom());
        categorieComboBox.setValue(piece.getCategorie());
        referenceField.setText(piece.getReference());
        marqueField.setText(piece.getMarque());
        typeSpecificiteField.setText(piece.getTypeSpecificite());
        prixUnitaireField.setText(String.valueOf(piece.getPrixUnitaire()));
        quantiteSpinner.getValueFactory().setValue(piece.getQuantite());
        if (piece.getSeuilCritique() != null) {
            seuilCritiqueSpinner.getValueFactory().setValue(piece.getSeuilCritique());
        }
        emplacementField.setText(piece.getEmplacement());
        descriptionArea.setText(piece.getDescription());

        // Changer le texte du bouton
        ajouterButton.setText("Modifier");
    }

    @FXML
    private void handleAjouter() {
        if (!isInputValid()) {
            return; // La validation a échoué, on ne continue pas
        }

        Piece nouvellePiece = new Piece();
        nouvellePiece.setNom(nomField.getText());
        nouvellePiece.setCategorie(categorieComboBox.getValue());
        nouvellePiece.setReference(referenceField.getText());
        nouvellePiece.setMarque(marqueField.getText());
        nouvellePiece.setTypeSpecificite(typeSpecificiteField.getText());
        nouvellePiece.setPrixUnitaire(Double.parseDouble(prixUnitaireField.getText()));
        nouvellePiece.setQuantite(quantiteSpinner.getValue());
        nouvellePiece.setSeuilCritique(seuilCritiqueSpinner.getValue());
        nouvellePiece.setEmplacement(emplacementField.getText());
        nouvellePiece.setDescription(descriptionArea.getText());

        try {
            if (pieceToModify != null) {
                // Mode modification
                nouvellePiece.setId(pieceToModify.getId()); // Ne pas oublier de setter l'ID !
                pieceDAO.updatePiece(nouvellePiece);
            } else {
                // Mode ajout
                pieceDAO.addPiece(nouvellePiece);
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", pieceToModify != null ? "Pièce modifiée avec succès." : "Pièce ajoutée avec succès.");
            // Fermer la fenêtre modale après l'opération
            ((Stage) ajouterButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter la pièce à la base de données.");
        }
    }

    @FXML
    private void handleReinitialiser() {
        nomField.clear();
        categorieComboBox.getSelectionModel().clearSelection();
        referenceField.clear();
        marqueField.clear();
        typeSpecificiteField.clear();
        prixUnitaireField.clear();
        quantiteSpinner.getValueFactory().setValue(0);
        seuilCritiqueSpinner.getValueFactory().setValue(5);
        emplacementField.clear();
        descriptionArea.clear();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage += "Le nom de la pièce est obligatoire.\n";
        }
        if (categorieComboBox.getValue() == null) {
            errorMessage += "La catégorie est obligatoire.\n";
        }
        if (referenceField.getText() == null || referenceField.getText().trim().isEmpty()) {
            errorMessage += "La référence est obligatoire.\n";
        }
        if (prixUnitaireField.getText() == null || prixUnitaireField.getText().trim().isEmpty()) {
            errorMessage += "Le prix unitaire est obligatoire.\n";
        } else {
            try {
                Double.parseDouble(prixUnitaireField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Le prix unitaire doit être un nombre valide (ex: 12.99).\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Champs invalides", errorMessage);
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
