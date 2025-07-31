package com.example.service;

import com.example.model.JPAUtil;
import com.example.model.LigneVente;
import com.example.model.Piece;
import com.example.model.Vente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class PieceService {

    private EntityManager entityManager;

    public PieceService() {
        this.entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    public List<Piece> findAll() {
        return entityManager.createQuery("SELECT p FROM Piece p", Piece.class).getResultList();
    }

    public void save(Piece piece) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(piece);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Piece piece) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(piece);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void delete(Piece piece) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(piece) ? piece : entityManager.merge(piece));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Enregistre une vente de manière transactionnelle.
     * Pour chaque ligne de vente, décrémente le stock de la pièce correspondante.
     * @param vente La vente à enregistrer.
     */
    public void enregistrerVente(Vente vente) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Enregistrer la vente et ses lignes
            entityManager.persist(vente);

            // Mettre à jour le stock pour chaque pièce vendue
            for (LigneVente ligne : vente.getLignesVente()) {
                Piece piece = ligne.getPiece();
                int nouvelleQuantite = piece.getQuantite() - ligne.getQuantite();
                if (nouvelleQuantite < 0) {
                    // Cette vérification devrait déjà être faite dans le contrôleur, mais c'est une sécurité
                    throw new IllegalStateException("Stock insuffisant pour la pièce: " + piece.getNom());
                }
                piece.setQuantite(nouvelleQuantite);
                entityManager.merge(piece);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            // Propager l'exception pour que le contrôleur puisse informer l'utilisateur
            throw new RuntimeException("Erreur lors de l'enregistrement de la vente", e);
        }
    }

    public void close() {
        if (entityManager != null) {
            entityManager.close();
        }
    }
}
