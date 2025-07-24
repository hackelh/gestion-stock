package com.example.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class PieceDAO {

    public void addPiece(Piece piece) {
        executeInsideTransaction(em -> em.persist(piece));
    }

    public Piece getPieceById(int id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Piece.class, id);
        } finally {
            em.close();
        }
    }

    public List<Piece> getAllPieces() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("FROM Piece", Piece.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void updatePiece(Piece piece) {
        executeInsideTransaction(em -> em.merge(piece));
    }

    public void deletePiece(int id) {
        executeInsideTransaction(em -> {
            Piece piece = em.find(Piece.class, id);
            if (piece != null) {
                em.remove(piece);
            }
        });
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
 
