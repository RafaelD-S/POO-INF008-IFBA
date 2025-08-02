package br.edu.ifba.inf008.model;

public class Book {
    private int id;
    private String title;
    private String author;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public String toString() {
        return title + " - " + author;
    }

    public int getId() {
      return id;
    }
}
