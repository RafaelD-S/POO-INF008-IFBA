package br.edu.ifba.inf008.model;

import java.time.LocalDate;

public class LoanReport {
    private String bookTitle;
    private String bookAuthor;
    private String userName;
    private LocalDate loanDate;

    public LoanReport(String bookTitle, String bookAuthor, String userName, LocalDate loanDate) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.userName = userName;
        this.loanDate = loanDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }
}
