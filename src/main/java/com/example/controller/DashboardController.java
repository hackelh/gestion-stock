package com.example.controller;

import com.example.model.Piece;
import com.example.model.PieceDAO;
import com.example.service.StockService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la vue du tableau de bord intelligent (DashboardView.fxml).
 * Il orchestre l'affichage des pièces, applique la logique métier du StockService
 * et gère les interactions de l'utilisateur avec le tableau de bord.
 */
public class DashboardController {

    @FXML private TableView<Piece> pieceTableView;
    @FXML private TableColumn<Piece, String> nomColumn;
    @FXML private TableColumn<Piece, String> categorieColumn;
    @FXML private TableColumn<Piece, String> referenceColumn;
    @FXML private TableColumn<Piece, Integer> quantiteColumn;
    @FXML private TableColumn<Piece, Integer> seuilCritiqueColumn;
    @FXML private TableColumn<Piece, String> statutColumn;
    @FXML private TableColumn<Piece, Integer> suggestionColumn;
    @FXML private TableColumn<Piece, LocalDate> derniereActiviteColumn;

    @FXML private TextField searchField;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;

    private PieceDAO pieceDAO;
    private StockService stockService;
    private final ObservableList<Piece> pieceList = FXCollections.observableArrayList();

    /**
     * Méthode appelée automatiquement par JavaFX après le chargement du fichier FXML.
     * C'est le point d'entrée pour initialiser le contrôleur.
     */
    @FXML
    public void initialize() {
        this.pieceDAO = new PieceDAO();
        this.stockService = new StockService();

        setupTableColumns();
        setupTableRowStyling();
        setupSearchFilter();
        setupButtonStateListeners();
        loadData();
    }

    /**
     * Configure les colonnes du TableView.
     * Les colonnes simples sont liées directement aux propriétés de l'objet Piece.
     * Les colonnes complexes ("Statut", "Suggestion") sont calculées à la volée.
     */
    private void setupTableColumns() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        referenceColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        seuilCritiqueColumn.setCellValueFactory(new PropertyValueFactory<>("seuilCritique"));
        derniereActiviteColumn.setCellValueFactory(new PropertyValueFactory<>("dateDerniereActivite"));
        
        // Colonnes calculées
        statutColumn.setCellValueFactory(cellData -> {
            Piece piece = cellData.getValue();
            String status = "OK";
            if (stockService.isObsolete(piece, 6)) {
                status = "Obsolète";
            } else if (stockService.isLowStock(piece)) {
                status = "Stock bas";
            }
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        suggestionColumn.setCellValueFactory(cellData -> {
            int suggestion = stockService.getReorderSuggestion(cellData.getValue());
            // On spécifie explicitement le type <Integer> pour aider le compilateur Java
            return new javafx.beans.property.SimpleObjectProperty<Integer>(suggestion > 0 ? suggestion : null);
        });
    }

    /**
     * Applique un style visuel dynamique à chaque ligne du tableau.
     * La couleur de la ligne change en fonction du statut de la pièce (stock bas ou obsolète),
     * en utilisant des classes de style définies dans `dashboard.css`.
     */
    private void setupTableRowStyling() {
        pieceTableView.setRowFactory(tv -> new TableRow<Piece>() {
            @Override
            protected void updateItem(Piece piece, boolean empty) {
                super.updateItem(piece, empty);
                getStyleClass().removeAll("low-stock-row", "obsolete-row");
                if (empty || piece == null) {
                    setStyle("");
                } else {
                    if (stockService.isObsolete(piece, 6)) {
                        getStyleClass().add("obsolete-row");
                    } else if (stockService.isLowStock(piece)) {
                        getStyleClass().add("low-stock-row");
                    }
                }
            }
        });
    }

    /**
     * Charge ou recharge les données depuis la base de données via le DAO
     * et met à jour la liste observable, ce qui rafraîchit automatiquement le TableView.
     */
    private void loadData() {
        List<Piece> pieces = pieceDAO.getAllPieces();
        pieceList.setAll(pieces);
        // On applique le filtre de recherche au chargement initial
        FilteredList<Piece> filteredData = new FilteredList<>(pieceList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(piece -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (piece.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (piece.getCategorie() != null && piece.getCategorie().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (piece.getReference() != null && piece.getReference().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Piece> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(pieceTableView.comparatorProperty());
        pieceTableView.setItems(sortedData);
    }

    private void setupSearchFilter() {
        // La logique de filtrage est maintenant dans loadData pour être réutilisable
    }

    private void setupButtonStateListeners() {
        // Désactiver les boutons si aucune sélection
        modifierButton.disableProperty().bind(pieceTableView.getSelectionModel().selectedItemProperty().isNull());
        supprimerButton.disableProperty().bind(pieceTableView.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void handleModifierPiece() {
        Piece selectedPiece = pieceTableView.getSelectionModel().getSelectedItem();
        if (selectedPiece == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une pièce à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/AddPieceView.fxml"));
            Parent root = loader.load(); // 1. Charger le FXML et ses composants

            // 2. Le contrôleur est maintenant entièrement initialisé, on peut le récupérer
            AddPieceController controller = loader.getController();
            
            // 3. On peut maintenant appeler ses méthodes en toute sécurité
            controller.setPieceToModify(selectedPiece);

            Stage stage = new Stage();
            stage.setTitle("Modifier une Pièce");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(pieceTableView.getScene().getWindow());
            stage.setScene(new Scene(root));
            
            // showAndWait bloque l'exécution jusqu'à ce que la fenêtre soit fermée
            stage.showAndWait();

            // Recharger les données pour refléter les changements potentiels
            loadData();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger le formulaire de modification.");
        }
    }

    @FXML
    private void handleSupprimerPiece() {
        Piece selectedPiece = pieceTableView.getSelectionModel().getSelectedItem();
        if (selectedPiece == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une pièce à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer la pièce : " + selectedPiece.getNom());
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette pièce ? Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            pieceDAO.deletePiece((long) selectedPiece.getId());
            loadData(); // Recharger les données
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gère l'événement de clic sur le bouton "Ajouter une pièce".
     * Ouvre le formulaire d'ajout dans une nouvelle fenêtre modale.
     * Après la fermeture de la fenêtre, les données du tableau sont rechargées pour afficher la nouvelle pièce.
     */
    @FXML
    private void handleAjouterPiece() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/AddPieceView.fxml"));
        Parent root = loader.load();
        
        Stage stage = new Stage();
        stage.setTitle("Ajouter une nouvelle pièce");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        
        stage.showAndWait();
        
        // Recharger les données après la fermeture de la fenêtre d'ajout
        loadData();
    }
}
