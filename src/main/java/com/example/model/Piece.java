package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pieces")
public class Piece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nom", nullable = false)
    private String nom;
    @Column(name = "description")
    private String description;
    @Column(name = "quantite", nullable = false)
    private int quantite;
    @Column(name = "prix_unitaire", nullable = false)
    private double prixUnitaire;
    @Column(name = "seuil_min", nullable = false)
    private int seuilMin;
    @Column(name = "date_derniere_vente")
    private LocalDate dateDerniereVente;
    @Column(name = "compatible_avec")
    private String compatibleAvec;

    public Piece() {}

    public Piece(int id, String nom, String description, int quantite, double prixUnitaire, int seuilMin, LocalDate dateDerniereVente, String compatibleAvec) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.seuilMin = seuilMin;
        this.dateDerniereVente = dateDerniereVente;
        this.compatibleAvec = compatibleAvec;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public int getSeuilMin() { return seuilMin; }
    public void setSeuilMin(int seuilMin) { this.seuilMin = seuilMin; }
    public LocalDate getDateDerniereVente() { return dateDerniereVente; }
    public void setDateDerniereVente(LocalDate dateDerniereVente) { this.dateDerniereVente = dateDerniereVente; }
    public String getCompatibleAvec() { return compatibleAvec; }
    public void setCompatibleAvec(String compatibleAvec) { this.compatibleAvec = compatibleAvec; }
} 
