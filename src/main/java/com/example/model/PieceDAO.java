package com.example.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.function.Function;

/**
 * DAO (Data Access Object) pour l'entité Piece.
 * Cette classe centralise toutes les opérations de persistance (lecture, écriture, mise à jour, suppression)
 * pour les objets Piece, en utilisant JPA/Hibernate.
 */
public class PieceDAO {

    /**
     * Ajoute une nouvelle pièce à la base de données.
     * 
     * @param piece La pièce à ajouter.
     */
    public void addPiece(Piece piece) {
        executeInsideTransaction(em -> em.persist(piece));
    }

    /**
     * Récupère une pièce par son identifiant.
     * 
     * @param id L'identifiant de la pièce à récupérer.
     * @return La pièce correspondant à l'identifiant, ou null si elle n'existe pas.
     */
    public Piece getPieceById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Piece.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Récupère la liste de toutes les pièces enregistrées dans la base de données.
     * 
     * @return La liste des pièces.
     */
    public List<Piece> getAllPieces() {
        return executeTransactionWithResult(entityManager ->
            entityManager.createQuery("SELECT p FROM Piece p", Piece.class).getResultList()
        );
    }

    /**
     * Met à jour une pièce existante dans la base de données.
     * @param piece La pièce avec les informations mises à jour.
     * 
     * @param piece La pièce à mettre à jour.
     */
    public void updatePiece(Piece piece) {
        executeInsideTransaction(em -> em.merge(piece));
    }

    /**
     * Supprime une pièce de la base de données.
     * 
     * @param i L'identifiant de la pièce à supprimer.
     */
    public void deletePiece(Long id) {
        executeInsideTransaction(em -> {
            Piece piece = em.find(Piece.class, id);
            if (piece != null) {
                em.remove(piece);
            }
        });
    }

    /**
     * Méthode utilitaire privée pour gérer le cycle de vie des transactions JPA.
     * Elle s'assure que chaque opération est bien enveloppée dans une transaction,
     * et gère la création/fermeture de l'EntityManager ainsi que le commit/rollback.
     *
     * @param action L'opération de base de données à exécuter (fournie sous forme d'expression lambda).
     */
    private <T> T executeTransactionWithResult(Function<EntityManager, T> action) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private void executeInsideTransaction(java.util.function.Consumer<EntityManager> action) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
 
