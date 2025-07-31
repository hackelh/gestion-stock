package com.example.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Représente une pièce de rechange automobile.
 * Cette classe est une entité JPA, mappée à la table "pieces_auto" dans la base de données.
 * Elle contient toutes les informations relatives à une pièce, ainsi que la logique de persistance.
 */
@Entity
@Table(name = "pieces_auto") // Nouveau nom de table
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nom;

    private String categorie;

    private String reference;

    private String marque;

    @Column(name = "type_specificite")
    private String typeSpecificite;

    @Column(name = "prix_unitaire", nullable = false)
    private double prixUnitaire;

    @Column(nullable = false)
    private int quantite;

    private String emplacement;

    @Column(length = 1000) // Augmenter la taille pour la description
    private String description;

    @Column(name = "seuil_critique")
    private int seuilCritique;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "date_derniere_activite")
    private LocalDate dateDerniereActivite;

    // Constructeurs
    public Piece() {}

    /**
     * Cette méthode est automatiquement appelée par Hibernate avant chaque création (persist)
     * ou mise à jour (update) de l'entité en base de données.
     * Elle garantit que la date de dernière activité est toujours à jour.
     */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        dateDerniereActivite = LocalDate.now();
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public StringProperty getNomProperty() { return new SimpleStringProperty(nom); }
    public void setNom(String nom) { this.nom = nom; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getTypeSpecificite() { return typeSpecificite; }
    public void setTypeSpecificite(String typeSpecificite) { this.typeSpecificite = typeSpecificite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public String getEmplacement() { return emplacement; }
    public void setEmplacement(String emplacement) { this.emplacement = emplacement; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getSeuilCritique() { return seuilCritique; }
    public void setSeuilCritique(Integer seuilCritique) { this.seuilCritique = seuilCritique; }
    public LocalDate getDateDerniereActivite() { return dateDerniereActivite; }
    public void setDateDerniereActivite(LocalDate dateDerniereActivite) { this.dateDerniereActivite = dateDerniereActivite; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
