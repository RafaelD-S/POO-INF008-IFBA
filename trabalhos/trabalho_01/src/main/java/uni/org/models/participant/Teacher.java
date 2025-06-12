package uni.org.models.participant;

import uni.org.utils.Console;
import uni.org.models.participant.Participant;

public class Teacher extends Participant {
    private String id;

    public Teacher(String name, String cpf, String email, String id) {
        super("Teacher", name, cpf, email);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
	public void print() {
        super.print();
        Console.log("Teacher Id: " + id);
	}
}