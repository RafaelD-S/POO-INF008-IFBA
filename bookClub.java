import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

class Member {
	private String name;
	private ArrayList<String> readBooks;
	private ArrayList<String> recomendedBooks;
	private int id;

	public Member(String name, int id) {
		this.name = name;
		this.readBooks = new ArrayList<>();
		this.recomendedBooks = new ArrayList<>();
		this.id = id;
	}

	public void addReadBook(String book) {
		readBooks.add(book);
	}

	public void addRecomendedBook(String book) {
		recomendedBooks.add(book);
	}

	public ArrayList<String> returnRecomendedBooks() {
		return recomendedBooks;
	}

	public void printMember() {
		System.out.println("Nome: " + name);
		System.out.println("id: " + id);
		for (int i = 0; i < readBooks.size(); i++) {
			System.out.println("Livro num " + (i + 1) + ":" + readBooks.get(i));
		}
		System.out.println("\n");
	}
	
	public void printMemberName() {
	    System.out.print(name);
	}
}

class Meetup {
	private String date;
	private String theme;
	private ArrayList<Member> meetupMembers;
	private ArrayList<String> meetupBooks;

	public Meetup(String date, String theme) {
		this.date = date;
		this.meetupMembers = new ArrayList<>();
		this.meetupBooks = new ArrayList<>();
		this.theme = theme;
	}

	public void addMember(Member person) {
		meetupMembers.add(person);
	}

	public void addBook(String book) {
		meetupBooks.add(book);
	}

	public void printMeetup() {
		System.out.println("Data: " + date);
		System.out.println("Tema: " + theme);

		System.out.print("Membros presentes: ");
		for (Member member : meetupMembers) {
		    System.out.print("\n");
			member.printMemberName();
		}
	    System.out.print("\n\n");
	}
}

class Club {
	private String name;
	private ArrayList<Member> registeredMembers;
	private ArrayList<Meetup> meetupList;
	private ArrayList<String> recomendedBooks;

	Scanner scanner = new Scanner(System.in);

	public Club(String name) {
		this.name = name;
		this.registeredMembers = new ArrayList<>();
		this.meetupList = new ArrayList<>();
		this.recomendedBooks = new ArrayList<>();
	}

	public void addMember(Member person) {
		registeredMembers.add(person);

		for (String book : person.returnRecomendedBooks())
			recomendedBooks.add(book);
	}

	public void addMeetup(Meetup meetup) {
		meetupList.add(meetup);
	}

	public void printMeetups() {
	   System.out.println("Printando todos os Encontros: \n");
	    
		for (Meetup meetupInstance : meetupList)
			meetupInstance.printMeetup();
	}

	private void PrintMostRecomendedBook() {
	    int counterMostQuantity = 0;
	    String nameMostQuantity = "nenhum";
	    
	    for (String book : recomendedBooks) {
	        int contador = Collections.frequency(recomendedBooks, book);
	        if (contador > counterMostQuantity) {
	            counterMostQuantity = contador;
	            nameMostQuantity = book;
	        }
	    }
        
		System.out.println("O livro com mais recomendacoes foi '" + nameMostQuantity + "' com " + counterMostQuantity + " recomendacoes!");
	}

	public void printStatics() {
		System.out.println("Printando informacoes do Clube " + name);
		System.out.println("Quantidade de membros: " + registeredMembers.size());
		System.out.println("Quantidade de encontros: " + meetupList.size());
		PrintMostRecomendedBook();
	}
}

public class Main {
	public static void main(String[] args) {
		Member roberto = new Member("Roberto", 0);
		roberto.addReadBook("Clean Code");
		roberto.addReadBook("Arte da Guerra");
		roberto.addRecomendedBook("Harry Potter");
		roberto.addRecomendedBook("O Principe");

		Member maria = new Member("maria", 1);
		maria.addReadBook("Divergente");
		maria.addReadBook("Percy Jackson");
		maria.addRecomendedBook("1984");

		Member carlos = new Member("Carlos", 2);
		carlos.addReadBook("O Senhor dos Aneis");
		carlos.addReadBook("Codigo Limpo");
		carlos.addRecomendedBook("Arquitetura Limpa");
		carlos.addRecomendedBook("1984");

		Member ana = new Member("Ana", 3);
		ana.addReadBook("Orgulho e Preconceito");
		ana.addReadBook("O Pequeno Principe");
		ana.addRecomendedBook("O Nome do Vento");
		ana.addRecomendedBook("1984");

		Member pedro = new Member("Pedro", 4);
		pedro.addReadBook("O Hobbit");
		pedro.addReadBook("As Cronicas de Narnia");
		pedro.addRecomendedBook("O Nome do Vento");
		
	   // -------------------------------------------
	   
	    Meetup encontroTerror = new Meetup("12/04/2024", "Terror");
	    encontroTerror.addBook("It");
        encontroTerror.addBook("A Freira");
        encontroTerror.addMember(pedro);
        encontroTerror.addMember(ana);
        encontroTerror.addMember(carlos);
        encontroTerror.addMember(maria);
	   
	    Meetup encontroFantasia = new Meetup("15/04/2024", "Fantasia");
        encontroFantasia.addBook("O Senhor dos Aneis");
        encontroFantasia.addBook("O Nome do Vento");
        encontroFantasia.addMember(carlos);
        encontroFantasia.addMember(pedro);
        encontroFantasia.addMember(ana);
	
	    Meetup encontroClassicos = new Meetup("20/04/2024", "Clássicos");
        encontroClassicos.addBook("1984"); 
        encontroClassicos.addBook("A Revolucao dos Bichos");
        encontroClassicos.addMember(maria); 
        encontroClassicos.addMember(ana);
        
        // ---------------------------------------------
        
        Club clubeDoLivro = new Club("Arroz com feijao");
        clubeDoLivro.addMember(roberto);
        clubeDoLivro.addMember(maria);
        clubeDoLivro.addMember(carlos);
        clubeDoLivro.addMember(ana);
        clubeDoLivro.addMember(pedro);
        
        clubeDoLivro.addMeetup(encontroTerror);
        clubeDoLivro.addMeetup(encontroFantasia);
        clubeDoLivro.addMeetup(encontroClassicos);
        
        clubeDoLivro.printMeetups();
        
        clubeDoLivro.printStatics();
	}
}