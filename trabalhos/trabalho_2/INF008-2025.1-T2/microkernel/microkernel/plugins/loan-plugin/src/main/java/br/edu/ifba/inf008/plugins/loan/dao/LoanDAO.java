package br.edu.ifba.inf008.plugins.loan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.plugins.loan.model.Loan;

public class LoanDAO {
    
    public List<Loan> findAll() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                     "u.name as user_name, b.title as book_title " +
                     "FROM loans l " +
                     "JOIN users u ON l.user_id = u.user_id " +
                     "JOIN books b ON l.book_id = b.book_id " +
                     "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("loan_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserName(rs.getString("user_name"));
                loan.setBookTitle(rs.getString("book_title"));
                loan.setLoanDate(rs.getString("loan_date"));
                loan.setReturnDate(rs.getString("return_date"));
                // Set returned based on whether return_date is null or not
                loan.setReturned(rs.getString("return_date") != null);
                loans.add(loan);
            }
        }
        
        return loans;
    }
    
    public List<Loan> findActiveLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                     "u.name as user_name, b.title as book_title " +
                     "FROM loans l " +
                     "JOIN users u ON l.user_id = u.user_id " +
                     "JOIN books b ON l.book_id = b.book_id " +
                     "WHERE l.return_date IS NULL " +
                     "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("loan_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserName(rs.getString("user_name"));
                loan.setBookTitle(rs.getString("book_title"));
                loan.setLoanDate(rs.getString("loan_date"));
                loan.setReturnDate(rs.getString("return_date"));
                loan.setReturned(false); // All active loans are not returned
                loans.add(loan);
            }
        }
        
        return loans;
    }
    
    public boolean save(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (user_id, book_id, loan_date) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getBookId());
            stmt.setString(3, loan.getLoanDate());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean returnLoan(int loanId, String returnDate) throws SQLException {
        String sql = "UPDATE loans SET return_date = ? WHERE loan_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, returnDate);
            stmt.setInt(2, loanId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public List<Loan> searchByUserName(String userName) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                     "u.name as user_name, b.title as book_title " +
                     "FROM loans l " +
                     "JOIN users u ON l.user_id = u.user_id " +
                     "JOIN books b ON l.book_id = b.book_id " +
                     "WHERE u.name LIKE ? " +
                     "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + userName + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("loan_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setUserName(rs.getString("user_name"));
                    loan.setBookTitle(rs.getString("book_title"));
                    loan.setLoanDate(rs.getString("loan_date"));
                    loan.setReturnDate(rs.getString("return_date"));
                    loan.setReturned(rs.getString("return_date") != null);
                    loans.add(loan);
                }
            }
        }
        
        return loans;
    }
    
    public List<Loan> searchByBookTitle(String bookTitle) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                     "u.name as user_name, b.title as book_title " +
                     "FROM loans l " +
                     "JOIN users u ON l.user_id = u.user_id " +
                     "JOIN books b ON l.book_id = b.book_id " +
                     "WHERE b.title LIKE ? " +
                     "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + bookTitle + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("loan_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setUserName(rs.getString("user_name"));
                    loan.setBookTitle(rs.getString("book_title"));
                    loan.setLoanDate(rs.getString("loan_date"));
                    loan.setReturnDate(rs.getString("return_date"));
                    loan.setReturned(rs.getString("return_date") != null);
                    loans.add(loan);
                }
            }
        }
        

        return loans;
    }
}
