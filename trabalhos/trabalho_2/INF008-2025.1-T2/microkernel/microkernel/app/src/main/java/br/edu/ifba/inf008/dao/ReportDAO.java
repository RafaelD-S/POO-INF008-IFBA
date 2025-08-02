package br.edu.ifba.inf008.dao;

import br.edu.ifba.inf008.model.LoanReport;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    public List<LoanReport> getActiveLoanReports() {
        List<LoanReport> reports = new ArrayList<>();

        String sql = 
            "SELECT b.title, b.author, u.name, l.loan_date " +
            "FROM loans l " +
            "JOIN users u ON l.user_id = u.user_id " +
            "JOIN books b ON l.book_id = b.book_id " +
            "WHERE l.return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reports.add(new LoanReport(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("name"),
                        rs.getDate("loan_date").toLocalDate()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }
}
