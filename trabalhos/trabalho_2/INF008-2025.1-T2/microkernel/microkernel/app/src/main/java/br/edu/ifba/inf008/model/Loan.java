package br.edu.ifba.inf008.model;

public class Loan {
    private int id;
    private String bookTitle;
    private String userName;
    private String loanDate;

    public Loan(int id, String bookTitle, String userName, String loanDate) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.loanDate = loanDate;
    }

    public int getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getLoanDate() {
        return loanDate;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", bookTitle, userName, loanDate);
    }
}
