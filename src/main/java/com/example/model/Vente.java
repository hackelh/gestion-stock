package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une vente effectuée dans le système.
 * Chaque vente a une date et une liste de produits vendus (lignes de vente).
 */
@Entity
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate dateVente;

    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneVente> lignesVente = new ArrayList<>();

    private double total;

    // Constructeurs
    public Vente() {
        this.dateVente = LocalDate.now();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }

    public List<LigneVente> getLignesVente() {
        return lignesVente;
    }

    public void setLignesVente(List<LigneVente> lignesVente) {
        this.lignesVente = lignesVente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Ajoute une ligne de vente à cette vente et met à jour le total.
     * @param ligne Ligne de vente à ajouter.
     */
    public void addLigneVente(LigneVente ligne) {
        lignesVente.add(ligne);
        ligne.setVente(this);
        recalculateTotal();
    }

    /**
     * Recalcule le montant total de la vente en se basant sur les lignes de vente.
     */
    public void recalculateTotal() {
        this.total = lignesVente.stream()
                .mapToDouble(LigneVente::getPrixTotal)
                .sum();
    }
}
