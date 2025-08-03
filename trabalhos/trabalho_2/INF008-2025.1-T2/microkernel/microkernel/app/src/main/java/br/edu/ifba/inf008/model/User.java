package br.edu.ifba.inf008.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String registeredAt;

    public User(int id, String name, String email, String registeredAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getRegisteredAt() { return registeredAt; }

    @Override
    public String toString() {
        return name + " - " + email;
    }
}
