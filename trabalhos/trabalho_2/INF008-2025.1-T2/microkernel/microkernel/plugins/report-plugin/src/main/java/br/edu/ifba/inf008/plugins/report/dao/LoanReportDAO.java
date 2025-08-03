package br.edu.ifba.inf008.plugins.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.plugins.report.models.Loan;

public class LoanReportDAO {
    
    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                    "u.name as user_name, b.title as book_title " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("loan_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserName(rs.getString("user_name"));
                loan.setBookTitle(rs.getString("book_title"));
                loan.setLoanDate(rs.getString("loan_date"));
                String returnDate = rs.getString("return_date");
                loan.setReturnDate(returnDate);
                loan.setReturned(returnDate != null);
                loans.add(loan);
            }
        }
        
        return loans;
    }
    
    public List<Loan> getLoansByUser(String userName) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                    "u.name as user_name, b.title as book_title " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "WHERE u.name LIKE ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + userName + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("loan_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setUserName(rs.getString("user_name"));
                    loan.setBookTitle(rs.getString("book_title"));
                    loan.setLoanDate(rs.getString("loan_date"));
                    String returnDate = rs.getString("return_date");
                    loan.setReturnDate(returnDate);
                    loan.setReturned(returnDate != null);
                    loans.add(loan);
                }
            }
        }
        
        return loans;
    }
    
    public List<Loan> getLoansByBook(String bookTitle) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                    "u.name as user_name, b.title as book_title " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "WHERE b.title LIKE ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + bookTitle + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("loan_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setUserName(rs.getString("user_name"));
                    loan.setBookTitle(rs.getString("book_title"));
                    loan.setLoanDate(rs.getString("loan_date"));
                    String returnDate = rs.getString("return_date");
                    loan.setReturnDate(returnDate);
                    loan.setReturned(returnDate != null);
                    loans.add(loan);
                }
            }
        }
        
        return loans;
    }
    
    public List<Loan> getLoansByStatus(boolean returned) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                    "u.name as user_name, b.title as book_title " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "WHERE (l.return_date IS " + (returned ? "NOT NULL" : "NULL") + ") " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("loan_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserName(rs.getString("user_name"));
                loan.setBookTitle(rs.getString("book_title"));
                loan.setLoanDate(rs.getString("loan_date"));
                String returnDate = rs.getString("return_date");
                loan.setReturnDate(returnDate);
                loan.setReturned(returnDate != null);
                loans.add(loan);
            }
        }
        
        return loans;
    }
    
    public List<Loan> getLoansByDateRange(String startDate, String endDate) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.loan_id, l.user_id, l.book_id, l.loan_date, l.return_date, " +
                    "u.name as user_name, b.title as book_title " +
                    "FROM loans l " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "WHERE l.loan_date BETWEEN ? AND ? " +
                    "ORDER BY l.loan_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("loan_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setUserName(rs.getString("user_name"));
                    loan.setBookTitle(rs.getString("book_title"));
                    loan.setLoanDate(rs.getString("loan_date"));
                    String returnDate = rs.getString("return_date");
                    loan.setReturnDate(returnDate);
                    loan.setReturned(returnDate != null);
                    loans.add(loan);
                }
            }
        }
        
        return loans;
    }
}
