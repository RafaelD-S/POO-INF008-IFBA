package br.edu.ifba.inf008.plugins.book.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
    private int copiesAvailable;

    public Book() {
    }

    public Book(int id, String title, String author, String isbn, int publishedYear, int copiesAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.copiesAvailable = copiesAvailable;
    }

    public Book(String title, String author, String isbn, int publishedYear, int copiesAvailable) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.copiesAvailable = copiesAvailable;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublishedYear() { return publishedYear; }
    public int getCopiesAvailable() { return copiesAvailable; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPublishedYear(int publishedYear) { this.publishedYear = publishedYear; }
    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }

    @Override
    public String toString() {
        return title + " - " + author;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
