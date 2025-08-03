package br.edu.ifba.inf008.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;
    private int copiesAvailable;

    public Book(int id, String title, String author, String isbn, int publishedYear, int copiesAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.copiesAvailable = copiesAvailable;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    @Override
    public String toString() {
        return title + " - " + author;
    }
}
