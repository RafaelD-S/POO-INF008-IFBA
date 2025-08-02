package br.edu.ifba.inf008.model;

public class User {
    private int id;
    private String name;
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String toString() {
        return name + " (" + email + ")";
    }

    public int getId() {
      return id;
    }
}
