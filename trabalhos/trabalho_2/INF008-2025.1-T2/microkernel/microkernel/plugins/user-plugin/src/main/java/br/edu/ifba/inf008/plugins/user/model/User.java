package br.edu.ifba.inf008.plugins.user.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String registeredAt;

    public User() {
    }

    public User(int id, String name, String email, String registeredAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRegisteredAt() { return registeredAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRegisteredAt(String registeredAt) { this.registeredAt = registeredAt; }

    @Override
    public String toString() {
        return name + " - " + email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
