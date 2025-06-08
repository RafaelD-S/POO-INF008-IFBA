package uni.org.app;

import uni.org.models.event.*;
import uni.org.models.participant.*;
import uni.org.services.Certificate;
import uni.org.ui.Menu;


public class Main {
	public static void main(String[] args) {

		Lecture mathClass = new Lecture("Math Class", "12/12/2004", "Presential", "Romilson", 6);
		Teacher Romsilson = new Teacher("Romilson", "123.456.789-12", "cu", 1);

		mathClass.printEvent();
		mathClass.printParticipants();
		
		Certificate.generate(Romsilson, mathClass);

		// Menu menu = new Menu();
		// menu.start();
	}
}