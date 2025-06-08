package uni.org.app;

import uni.org.models.event.*;
import uni.org.ui.Menu;

public class Main {
	public static void main(String[] args) {

		Lecture mathClass = new Lecture("Math Class", "12/12/2004", "Presential", "Romilson", 6);

		mathClass.printEvent();
		mathClass.printParticipants();
		

		// Menu menu = new Menu();
		// menu.start();
	}
}