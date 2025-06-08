package uni.org.models.participant;

import uni.org.utils.Console;

public abstract class Participant {
    private String type;
    private String name;
    private String cpf;
    private String email;

	public Participant(String type, String name, String cpf, String email) {
        this.type = type;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public void print() {
        Console.log("Origin: " + type);
        Console.log("Name: " + name);
        Console.log("CPF: " + cpf);
        Console.log("Email: " + email);
    }
}