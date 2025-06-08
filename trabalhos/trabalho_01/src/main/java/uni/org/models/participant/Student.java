package uni.org.models.participant;

import uni.org.utils.Console;
import uni.org.models.participant.Participant;

public class Student extends Participant {
    private long id;

    public Student(String name, String cpf, String email, long id) {
        super("Student", name, cpf, email);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
	public void print() {
        super.print();
        Console.log("Student Id: " + id);
	}
}