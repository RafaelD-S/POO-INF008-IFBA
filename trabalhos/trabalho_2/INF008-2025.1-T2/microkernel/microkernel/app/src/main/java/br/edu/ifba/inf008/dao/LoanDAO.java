package br.edu.ifba.inf008.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.model.Loan;

public class LoanDAO {

    public boolean registerLoan(int userId, int bookId) {
        String sql = "INSERT INTO loans (user_id, book_id, loan_date) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerReturn(int loanId) {
        String sql = "UPDATE loans SET return_date = CURRENT_TIMESTAMP WHERE loan_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loanId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Loan> findActiveLoans() {
    List<Loan> loans = new ArrayList<>();

    String sql = 
        "SELECT l.loan_id, b.title, u.name " +
        "FROM loans l " +
        "JOIN books b ON l.book_id = b.book_id " +
        "JOIN users u ON l.user_id = u.user_id " +
        "WHERE l.return_date IS NULL";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            int id = rs.getInt("loan_id");
            String description = rs.getString("title") + " - " + rs.getString("name");
            loans.add(new Loan(id, description));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return loans;
  }

}
