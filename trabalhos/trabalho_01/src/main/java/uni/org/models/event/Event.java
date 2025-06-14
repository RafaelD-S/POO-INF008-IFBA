package uni.org.models.event;

import uni.org.models.participant.Participant;
import uni.org.utils.Console;
import java.util.ArrayList;
import java.util.List;

public abstract class Event {
	private String name;
	private String type;
	private String date;
	private int capacity;
	private String modality;
	private String location;
	private ArrayList<Participant> participants;
	
	public Event(String type, String name, String date, int capacity, String modality) {
		this.name = name;
		this.type = type;
		this.date = date;
		this.capacity = capacity;
		this.modality = modality;
		participants = new ArrayList<Participant>();
	}

	public Event(String type, String name, String date, int capacity, String modality, String location) {
		this.name = name;
		this.type = type;
		this.date = date;
		this.capacity = capacity;
		this.modality = modality;
		participants = new ArrayList<Participant>();
		this.location = location;
	}

	public void addParticipant(Participant person) {
		participants.add(person);
	}

	public void removeParticipant(int i) {
		participants.remove(i);
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDate() {
		return date;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getModality() {
		return modality;
	}

	public String getLocation() {
		return location;
	}

	public ArrayList<Participant> getParticipants() {
		return participants;
	}

	public int getParticipantsLeght() {
		return participants.size();
	}

	
    public List<String> getParticipantsNames() {
        List<String> names = new ArrayList<>();
        for (Participant participant : participants) {
            names.add(participant.getName());
        }
        return names;
    }

	public Participant getParticipantByIndex(int index) {
        if (participants.get(index) != null) return participants.get(index);
        return null;
    }

	public void printEvent() {
		Console.log("Name: " + name);
		Console.log("Type: " + type);
		Console.log("Date: " + date);
		Console.log("Modality: " + modality);

		if (capacity > 1)
			Console.log("Capacity: " + capacity + " People");
		else 
			Console.log("Capacity: " + capacity + " Person");
	}
}