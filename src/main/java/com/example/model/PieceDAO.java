package com.example.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PieceDAO {
    public List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();
        String sql = "SELECT * FROM pieces";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Piece p = new Piece();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setDescription(rs.getString("description"));
                p.setQuantite(rs.getInt("quantite"));
                p.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                p.setSeuilMin(rs.getInt("seuil_min"));
                Date date = rs.getDate("date_derniere_vente");
                if (date != null) {
                    p.setDateDerniereVente(date.toLocalDate());
                }
                p.setCompatibleAvec(rs.getString("compatible_avec"));
                pieces.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pieces;
    }

    public boolean addPiece(Piece piece) {
        String sql = "INSERT INTO pieces (nom, description, quantite, prix_unitaire, seuil_min, date_derniere_vente, compatible_avec) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, piece.getNom());
            pstmt.setString(2, piece.getDescription());
            pstmt.setInt(3, piece.getQuantite());
            pstmt.setDouble(4, piece.getPrixUnitaire());
            pstmt.setInt(5, piece.getSeuilMin());
            if (piece.getDateDerniereVente() != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(piece.getDateDerniereVente()));
            } else {
                pstmt.setDate(6, null);
            }
            pstmt.setString(7, piece.getCompatibleAvec());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePiece(Piece piece) {
        String sql = "UPDATE pieces SET nom=?, description=?, quantite=?, prix_unitaire=?, seuil_min=?, date_derniere_vente=?, compatible_avec=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, piece.getNom());
            pstmt.setString(2, piece.getDescription());
            pstmt.setInt(3, piece.getQuantite());
            pstmt.setDouble(4, piece.getPrixUnitaire());
            pstmt.setInt(5, piece.getSeuilMin());
            if (piece.getDateDerniereVente() != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(piece.getDateDerniereVente()));
            } else {
                pstmt.setDate(6, null);
            }
            pstmt.setString(7, piece.getCompatibleAvec());
            pstmt.setInt(8, piece.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePiece(int id) {
        String sql = "DELETE FROM pieces WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Piece getPieceById(int id) {
        String sql = "SELECT * FROM pieces WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Piece p = new Piece();
                    p.setId(rs.getInt("id"));
                    p.setNom(rs.getString("nom"));
                    p.setDescription(rs.getString("description"));
                    p.setQuantite(rs.getInt("quantite"));
                    p.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                    p.setSeuilMin(rs.getInt("seuil_min"));
                    java.sql.Date date = rs.getDate("date_derniere_vente");
                    if (date != null) {
                        p.setDateDerniereVente(date.toLocalDate());
                    }
                    p.setCompatibleAvec(rs.getString("compatible_avec"));
                    return p;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 
