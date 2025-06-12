package uni.org.controlers;

import java.util.List;
import java.util.ArrayList;

import uni.org.models.participant.Participant;
import uni.org.utils.Console;

public class ParticipantControl {
    private static final ParticipantControl instance = new ParticipantControl();
    private final List<Participant> participantList = new ArrayList<Participant>();

    private ParticipantControl() {}

    public static ParticipantControl getLocalInstance() {
       return instance;
    }

    public List<Participant> getParticipants() {
        return new ArrayList<>(participantList);
    }

    public Boolean hasParticipant() {
        return participantList.size() > 0;
    }

    public Participant getParticipantByIndex(int index) {
        if (participantList.get(index) != null) return participantList.get(index);
        return null;
    }

    public void add(Participant item) {
        participantList.add(item);
    }

    public void remove(int index) {
        participantList.remove(index);
    }

    public List<String> getParticipantsNames() {
        List<String> names = new ArrayList<>();
        for (Participant participant : participantList) {
            names.add(participant.getName());
        }
        return names;
    }

    public void listParticipants() {
        for (Participant participant : participantList) {
            Console.log(participant.getName());
            Console.log(participant.getType());
        }
    }
}