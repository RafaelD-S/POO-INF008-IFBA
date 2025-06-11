package uni.org.ui.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Choices {
    MAIN(Arrays.asList("Manage Events", "Manage Participants", "Services")),
    PARTICIPANTS(Arrays.asList("Add a new event", "Delete an existing event", "List all events")),
    EVENTS(Arrays.asList("Add a new event", "Delete an existing event", "List all events")),
    EVENTS_TYPE(Arrays.asList("Course", "Fair", "Lecture", "Workshop")),
    EVENTS_MODALITY(Arrays.asList("Presential", "Remote"));


    private final List<String> listaVeiculos;

    Choices(List<String> listaVeiculos) {
        this.listaVeiculos = new ArrayList<>(listaVeiculos);
    }

    public List<String> getListaVeiculos() {
        return new ArrayList<>(listaVeiculos); // retorna uma cópia para proteger o original
    }

    public static List<String> get(String nome) {
        try {
            return Choices.valueOf(nome.toUpperCase()).getListaVeiculos();
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo inválido: " + nome);
            return new ArrayList<>();
        }
    }
}