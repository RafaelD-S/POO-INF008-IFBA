package uni.org.ui.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Choices {
    MAIN(Arrays.asList("Manage Events", "Manage Participants", "Services")),
    PARTICIPANTS(Arrays.asList("Add a new event", "Delete an existing event", "List all events")),
    EVENTS(Arrays.asList("Add a new event", "Delete an existing event", "List all events")),
    EVENTS_TYPE(Arrays.asList("Course", "Fair", "Lecture", "Workshop")),
    EVENTS_MODALITY(Arrays.asList("Presential", "Remote", "Hybrid"));


    private final List<String> choiceList;

    Choices(List<String> choiceList) {
        this.choiceList = new ArrayList<>(choiceList);
    }

    public List<String> getChoiceList() {
        return new ArrayList<>(choiceList); 
    }

    public static List<String> get(String nome) {
        try {
            return Choices.valueOf(nome.toUpperCase()).getChoiceList();
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo inválido: " + nome);
            return new ArrayList<>();
        }
    }
}