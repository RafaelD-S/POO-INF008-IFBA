package uni.org.models.participant;

import uni.org.utils.Console;
import uni.org.models.participant.Participant;

public class Student extends Participant {
    private String id;

    public Student(String name, String cpf, String email, String id) {
        super("Student", name, cpf, email);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
	public void print() {
        super.print();
        Console.log("Student Id: " + id);
	}
}