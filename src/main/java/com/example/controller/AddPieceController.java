package com.example.controller;

import com.example.model.Piece;
import com.example.model.PieceDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class AddPieceController {

    // Champs FXML pour les données de la pièce
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

    // Champs FXML pour la gestion de l'image
    @FXML private ImageView imagePreview;
    @FXML private Button choisirImageButton;

    private PieceDAO pieceDAO;
    private Piece pieceToModify; // Utilisé en mode modification
    private File selectedImageFile; // Fichier image sélectionné par l'utilisateur

    @FXML
    public void initialize() {
        pieceDAO = new PieceDAO();

        // Initialiser la ComboBox des catégories
        categorieComboBox.setItems(FXCollections.observableArrayList(
            "Moteur", "Freinage", "Suspension", "Transmission", "Échappement",
            "Filtration", "Éclairage", "Carrosserie", "Accessoires", "Liquides"
        ));

        // Initialiser les Spinners pour la quantité et le seuil
        SpinnerValueFactory<Integer> quantiteValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 1);
        quantiteSpinner.setValueFactory(quantiteValueFactory);

        SpinnerValueFactory<Integer> seuilValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 5);
        seuilCritiqueSpinner.setValueFactory(seuilValueFactory);
    }

    /**
     * Pré-remplit le formulaire avec les données d'une pièce existante pour la modification.
     * @param piece La pièce à modifier.
     */
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

        // Charger et afficher l'image existante
        if (piece.getImagePath() != null && !piece.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/" + piece.getImagePath()));
                imagePreview.setImage(image);
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image : " + piece.getImagePath());
                imagePreview.setImage(null); // Afficher une image vide en cas d'erreur
            }
        }

        ajouterButton.setText("Modifier");
    }

    /**
     * Gère l'événement du clic sur le bouton "Ajouter" ou "Modifier".
     * Valide les entrées, sauvegarde l'image, puis ajoute ou met à jour la pièce.
     */
    @FXML
    private void handleAjouter() {
        if (!isInputValid()) return;

        Piece nouvellePiece = (pieceToModify != null) ? pieceToModify : new Piece();

        // Sauvegarder l'image si une nouvelle a été sélectionnée
        if (selectedImageFile != null) {
            try {
                String imagePath = saveImage(selectedImageFile);
                nouvellePiece.setImagePath(imagePath);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de sauvegarde", "Impossible de sauvegarder l'image de la pièce.");
                e.printStackTrace();
                return; // Arrêter si la sauvegarde de l'image échoue
            }
        } else if (pieceToModify != null) {
            nouvellePiece.setImagePath(pieceToModify.getImagePath()); // Conserver l'ancienne image
        }

        // Mettre à jour les propriétés de la pièce
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
                pieceDAO.updatePiece(nouvellePiece);
            } else {
                pieceDAO.addPiece(nouvellePiece);
            }
            showAlert(Alert.AlertType.INFORMATION, "Succès", pieceToModify != null ? "Pièce modifiée avec succès." : "Pièce ajoutée avec succès.");
            ((Stage) ajouterButton.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Impossible de sauvegarder la pièce.");
        }
    }

    /**
     * Ouvre une boîte de dialogue pour que l'utilisateur choisisse une image.
     * Affiche un aperçu de l'image sélectionnée.
     */
    @FXML
    private void handleChoisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image pour la pièce");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"),
            new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        File file = fileChooser.showOpenDialog(ajouterButton.getScene().getWindow());
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());
                imagePreview.setImage(image);
                selectedImageFile = file;
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur d'image", "Impossible de charger l'image sélectionnée.");
            }
        }
    }

    /**
     * Gère le clic sur le bouton "Réinitialiser" pour vider tous les champs.
     */
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
        imagePreview.setImage(null);
        selectedImageFile = null;
    }

    /**
     * Valide les champs de saisie obligatoires.
     * @return true si les entrées sont valides, sinon false.
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) errorMessage += "Le nom de la pièce est obligatoire.\n";
        if (categorieComboBox.getValue() == null) errorMessage += "La catégorie est obligatoire.\n";
        if (referenceField.getText() == null || referenceField.getText().trim().isEmpty()) errorMessage += "La référence est obligatoire.\n";
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

    /**
     * Sauvegarde le fichier image dans un dossier du projet et retourne son chemin relatif.
     * @param imageFile Le fichier à sauvegarder.
     * @return Le chemin relatif de l'image sauvegardée.
     * @throws IOException Si une erreur de copie se produit.
     */
    private String saveImage(File imageFile) throws IOException {
        Path imageDirectory = Paths.get("src/main/resources/images/pieces");
        if (!Files.exists(imageDirectory)) {
            Files.createDirectories(imageDirectory);
        }
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getName();
        Path destinationPath = imageDirectory.resolve(fileName);
        Files.copy(imageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return "images/pieces/" + fileName;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
