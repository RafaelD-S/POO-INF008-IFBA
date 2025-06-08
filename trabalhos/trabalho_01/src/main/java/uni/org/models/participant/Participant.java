package uni.org.models.participant;

public abstract class Participant {
    private String type;
    private String name;
    private long cpf;
    private String email;

	public Participant(String type, String name, long cpf, String email) {
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

    public long getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }
}