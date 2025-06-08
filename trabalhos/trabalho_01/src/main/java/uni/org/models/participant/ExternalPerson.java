package uni.org.models.participant;

import uni.org.utils.Console;
import uni.org.models.participant.Participant;

public class ExternalPerson extends Participant {
    private String instituition;

    public ExternalPerson(String name, String cpf, String email) {
        super("External Person", name, cpf, email);
    }

    public void addInstituition(String instituition) {
        this.instituition = instituition;
    }

    public String getInstituition() {
        if (instituition != null) return instituition;
        else {
            Console.log("No Instituition provided");
            return null;
        }
    }

    @Override
	public void print() {
        super.print();
        if (instituition != null) Console.log("Teacher Id: " + instituition);
	}
}