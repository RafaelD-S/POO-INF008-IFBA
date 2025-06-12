package uni.org.models.participant;

import uni.org.utils.Console;
import uni.org.models.participant.Participant;

public class ExternalPerson extends Participant {
    private String instituition;

    public ExternalPerson(String name, String cpf, String email) {
        super("External Person", name, cpf, email);
    }

    public ExternalPerson(String name, String cpf, String email, String instituition) {
        super("External Person", name, cpf, email);
        this.instituition = instituition;
    }

    public String getInstituition() {
        if (instituition != null) return instituition;
        else {
            return "No Instituition provided";
        }
    }

    @Override
	public void print() {
        super.print();
        if (instituition != null) Console.log("Teacher Id: " + instituition);
	}
}