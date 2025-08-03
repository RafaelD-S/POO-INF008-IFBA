package br.edu.ifba.inf008.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.model.Book;

public class BookDAO {
public List<Book> findAll() {
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM books";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            books.add(new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("published_year"),
                    rs.getInt("copies_available")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return books;
}


    public List<Book> findAvailable() {
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM books WHERE copies_available > 0";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            books.add(new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("published_year"),
                    rs.getInt("copies_available")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return books;
  }

  public boolean insert(String title, String author, String isbn, int year, int copies) {
    String sql = "INSERT INTO books (title, author, isbn, published_year, copies_available) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, isbn);
        stmt.setInt(4, year);
        stmt.setInt(5, copies);
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean update(int bookId, String title, String author, String isbn, int year, int copies) {
    String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, published_year = ?, copies_available = ? WHERE book_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, isbn);
        stmt.setInt(4, year);
        stmt.setInt(5, copies);
        stmt.setInt(6, bookId);
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean delete(int bookId) {
    String sql = "DELETE FROM books WHERE book_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, bookId);
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean decreaseAvailableCopies(int bookId) {
    String sql = "UPDATE books SET copies_available = copies_available - 1 WHERE book_id = ? AND copies_available > 0";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, bookId);
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean increaseAvailableCopies(int bookId) {
    String sql = "UPDATE books SET copies_available = copies_available + 1 WHERE book_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, bookId);
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}
