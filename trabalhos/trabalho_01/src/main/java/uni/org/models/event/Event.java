package uni.org.models.event;

import uni.org.models.participant.Participant;
import uni.org.utils.Console;
import java.util.ArrayList;

public abstract class Event {
	private String name;
	private String type;
	private String date;
	private int capacity;
	private String location;
	private ArrayList<Participant> participants;
	
	public Event(String type, String name, String date, int capacity, String location) {
		this.name = name;
		this.type = type;
		this.date = date;
		this.capacity = capacity;
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

	public String getLocation() {
		return location;
	}

	public ArrayList<Participant> getParticipants() {
		return participants;
	}

	public void printParticipants() {
		if (participants != null) {
			for (Participant person : participants) {
				Console.log(person.getName());
			}
		}
		else 
			Console.log("There's no one in this " + type + " yet.");
	}

	public void printEvent() {
		Console.log("Event: " + name + "\nType: " + type + "\nDate: " + date + "\nLocation: " + location + "\nCapacity: " + capacity);
	}
}