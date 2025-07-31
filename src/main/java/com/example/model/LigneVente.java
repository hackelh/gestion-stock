package com.example.model;

import jakarta.persistence.*;

/**
 * Représente une ligne dans une vente, correspondant à un produit spécifique et une quantité.
 */
@Entity
public class LigneVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "vente_id")
    private Vente vente;

    @ManyToOne
    @JoinColumn(name = "piece_id")
    private Piece piece;

    private int quantite;

    private double prixUnitaire;

    private double prixTotal;

    // Constructeurs
    public LigneVente() {
    }

    public LigneVente(Piece piece, int quantite) {
        this.piece = piece;
        this.quantite = quantite;
        this.prixUnitaire = piece.getPrixUnitaire();
        this.prixTotal = this.prixUnitaire * quantite;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        this.prixTotal = this.prixUnitaire * quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }
}
