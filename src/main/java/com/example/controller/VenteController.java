package com.example.controller;

import com.example.model.LigneVente;
import com.example.model.Piece;
import com.example.model.Vente;
import com.example.service.PieceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class VenteController {

    @FXML private TableView<Piece> produitsTableView;
    @FXML private TableColumn<Piece, String> nomProduitColumn;
    @FXML private TableColumn<Piece, String> refProduitColumn;
    @FXML private TableColumn<Piece, Double> prixProduitColumn;
    @FXML private TableColumn<Piece, Integer> stockProduitColumn;
    @FXML private TextField filtreProduitField;
    @FXML private Spinner<Integer> quantiteSpinner;

    @FXML private TableView<LigneVente> panierTableView;
    @FXML private TableColumn<LigneVente, String> nomPanierColumn;
    @FXML private TableColumn<LigneVente, Integer> quantitePanierColumn;
    @FXML private TableColumn<LigneVente, Double> prixTotalPanierColumn;

    @FXML private Label totalVenteLabel;
    @FXML private Button retourButton;

    private PieceService pieceService;
    private ObservableList<Piece> produitsEnStock;
    private ObservableList<LigneVente> panier;

    public VenteController() {
        this.pieceService = new PieceService();
        this.panier = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Configuration des colonnes du tableau des produits
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        refProduitColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
        prixProduitColumn.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        stockProduitColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Configuration des colonnes du tableau du panier
        nomPanierColumn.setCellValueFactory(cellData -> cellData.getValue().getPiece().getNomProperty());
        quantitePanierColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        prixTotalPanierColumn.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));

        chargerProduits();
        configurerFiltre();
        panierTableView.setItems(panier);
    }

    private void chargerProduits() {
        produitsEnStock = FXCollections.observableArrayList(pieceService.findAll());
        produitsTableView.setItems(produitsEnStock);
    }

    private void configurerFiltre() {
        FilteredList<Piece> filteredData = new FilteredList<>(produitsEnStock, p -> true);
        filtreProduitField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(piece -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (piece.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (piece.getReference().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        produitsTableView.setItems(filteredData);
    }

    @FXML
    void ajouterAuPanier(ActionEvent event) {
        Piece selectedPiece = produitsTableView.getSelectionModel().getSelectedItem();
        int quantite = quantiteSpinner.getValue();

        if (selectedPiece == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun produit sélectionné", "Veuillez sélectionner un produit à ajouter.");
            return;
        }

        if (quantite > selectedPiece.getQuantite()) {
            showAlert(Alert.AlertType.ERROR, "Stock insuffisant", "La quantité demandée est supérieure au stock disponible.");
            return;
        }

        // Vérifier si le produit est déjà dans le panier
        Optional<LigneVente> existingLigne = panier.stream()
                .filter(ligne -> ligne.getPiece().getId() == selectedPiece.getId())
                .findFirst();

        if (existingLigne.isPresent()) {
            LigneVente ligne = existingLigne.get();
            int nouvelleQuantite = ligne.getQuantite() + quantite;
            if (nouvelleQuantite > selectedPiece.getQuantite()) {
                showAlert(Alert.AlertType.ERROR, "Stock insuffisant", "La quantité totale dans le panier dépasse le stock disponible.");
                return;
            }
            ligne.setQuantite(nouvelleQuantite);
        } else {
            panier.add(new LigneVente(selectedPiece, quantite));
        }

        panierTableView.refresh();
        mettreAJourTotal();
    }

    @FXML
    void retirerDuPanier(ActionEvent event) {
        LigneVente selectedLigne = panierTableView.getSelectionModel().getSelectedItem();
        if (selectedLigne != null) {
            panier.remove(selectedLigne);
            mettreAJourTotal();
        }
    }

    @FXML
    void validerVente(ActionEvent event) {
        if (panier.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Panier vide", "Le panier est vide. Ajoutez des produits avant de valider.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer la vente ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                Vente vente = new Vente();
                panier.forEach(vente::addLigneVente);
                pieceService.enregistrerVente(vente);
                showAlert(Alert.AlertType.INFORMATION, "Vente réussie", "La vente a été enregistrée et le stock mis à jour.");
                panier.clear();
                chargerProduits(); // Recharger pour mettre à jour les stocks visibles
                mettreAJourTotal();
            }
        });
    }

    private void mettreAJourTotal() {
        double total = panier.stream().mapToDouble(LigneVente::getPrixTotal).sum();
        totalVenteLabel.setText(String.format("%.2f €", total));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void retourDashboard(ActionEvent event) throws IOException {
        Stage stage = (Stage) retourButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/view/DashboardView.fxml"));
        stage.setScene(new Scene(root));
    }
}
