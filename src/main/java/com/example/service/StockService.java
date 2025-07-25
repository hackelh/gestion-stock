package com.example.service;

import com.example.model.Piece;
import java.time.LocalDate;


/**
 * Classe de service contenant toute la logique métier "intelligente" de l'application.
 * Elle est responsable d'analyser les données des pièces (modèle) pour en déduire des informations
 * à plus haute valeur ajoutée (statut, suggestions), sans se soucier de la base de données ou de l'interface.
 */
public class StockService {

    /**
     * Vérifie si la quantité en stock d'une pièce est inférieure ou égale à son seuil critique.
     * @param piece La pièce à vérifier.
     * @return true si le stock est bas, false sinon.
     */
    public boolean isLowStock(Piece piece) {
        if (piece == null) {
            return false;
        }
        // Si le seuil n'est pas défini, on considère que le stock n'est pas bas.
        if (piece.getSeuilCritique() == null) {
            return false;
        }
        // Le seuil critique est atteint si la quantité est inférieure ou égale.
        return piece.getQuantite() <= piece.getSeuilCritique();
    }

    /**
     * Vérifie si une pièce est considérée comme obsolète, c'est-à-dire sans activité
     * depuis un certain nombre de mois.
     * @param piece La pièce à vérifier.
     * @param obsolescenceMonths Le nombre de mois d'inactivité pour qu'une pièce soit obsolète.
     * @return true si la pièce est obsolète, false sinon.
     */
    public boolean isObsolete(Piece piece, int obsolescenceMonths) {
        if (piece == null || piece.getDateDerniereActivite() == null) {
            return false; // Ne peut pas être obsolète si on n'a pas de date
        }

        LocalDate today = LocalDate.now();
        LocalDate obsolescenceDate = today.minusMonths(obsolescenceMonths);

        return piece.getDateDerniereActivite().isBefore(obsolescenceDate);
    }

    /**
     * Suggère une quantité à recommander pour une pièce en stock bas.
     * La suggestion vise à ramener le stock à un niveau cible (ex: 2 fois le seuil critique).
     * @param piece La pièce pour laquelle faire une suggestion.
     * @return La quantité à recommander. Retourne 0 si le stock n'est pas bas.
     */
    public int getReorderSuggestion(Piece piece) {
        // On ne fait de suggestion que si le stock est bas (ce qui implique que le seuil existe).
        if (isLowStock(piece)) {
            // Suggestion simple : recommander assez pour doubler le seuil critique.
            return (piece.getSeuilCritique() * 2) - piece.getQuantite();
        }
        return 0;
    }
}
