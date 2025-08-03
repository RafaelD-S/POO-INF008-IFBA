package br.edu.ifba.inf008.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import br.edu.ifba.inf008.model.Loan;
import br.edu.ifba.inf008.model.LoanReport;

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
    String getBookIdSQL = "SELECT book_id FROM loans WHERE loan_id = ?";
    String updateReturnSQL = "UPDATE loans SET return_date = ? WHERE loan_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement getStmt = conn.prepareStatement(getBookIdSQL);
         PreparedStatement updateStmt = conn.prepareStatement(updateReturnSQL)) {

        getStmt.setInt(1, loanId);
        ResultSet rs = getStmt.executeQuery();

        if (rs.next()) {
            int bookId = rs.getInt("book_id");

            updateStmt.setString(1, LocalDate.now().toString());
            updateStmt.setInt(2, loanId);

            boolean updated = updateStmt.executeUpdate() > 0;

            if (updated) {
                BookDAO bookDAO = new BookDAO();
                return bookDAO.increaseAvailableCopies(bookId);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

    
public List<Loan> findActiveLoans() {
    List<Loan> loans = new ArrayList<>();

    String sql = 
        "SELECT l.loan_id, b.title AS book_title, u.name AS user_name, l.loan_date " +
        "FROM loans l " +
        "JOIN books b ON l.book_id = b.book_id " +
        "JOIN users u ON l.user_id = u.user_id " +
        "WHERE l.return_date IS NULL " +
        "ORDER BY l.loan_date DESC";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            loans.add(new Loan(
                rs.getInt("loan_id"),
                rs.getString("book_title"),
                rs.getString("user_name"),
                rs.getString("loan_date")
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return loans;
}


public List<LoanReport> getLoanReport() {
    List<LoanReport> reports = new ArrayList<>();

    String sql = 
        "SELECT b.title AS book_title, b.author AS book_author, " +
        "u.name AS user_name, l.loan_date " +
        "FROM loans l " +
        "JOIN users u ON l.user_id = u.user_id " +
        "JOIN books b ON l.book_id = b.book_id";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            reports.add(new LoanReport(
                rs.getString("book_title"),
                rs.getString("book_author"),
                rs.getString("user_name"),
                rs.getDate("loan_date").toLocalDate()
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return reports;
}

public List<LoanReport> getBorrowedButNotReturnedReport() {
    List<LoanReport> reports = new ArrayList<>();

    String sql = 
        "SELECT b.title AS book_title, b.author AS book_author, " +
        "u.name AS user_name, l.loan_date " +
        "FROM loans l " +
        "JOIN users u ON l.user_id = u.user_id " +
        "JOIN books b ON l.book_id = b.book_id " +
        "WHERE l.return_date IS NULL " +
        "ORDER BY l.loan_date DESC";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            reports.add(new LoanReport(
                rs.getString("book_title"),
                rs.getString("book_author"),
                rs.getString("user_name"),
                rs.getDate("loan_date").toLocalDate()
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return reports;
}

public List<LoanReport> getReturnedBooksReport() {
    List<LoanReport> reports = new ArrayList<>();

    String sql = 
        "SELECT b.title AS book_title, b.author AS book_author, " +
        "u.name AS user_name, l.loan_date " +
        "FROM loans l " +
        "JOIN users u ON l.user_id = u.user_id " +
        "JOIN books b ON l.book_id = b.book_id " +
        "WHERE l.return_date IS NOT NULL " +
        "ORDER BY l.return_date DESC";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            reports.add(new LoanReport(
                rs.getString("book_title"),
                rs.getString("book_author"),
                rs.getString("user_name"),
                rs.getDate("loan_date").toLocalDate()
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return reports;
}

}
