package br.edu.ifba.inf008.plugins.report.models;

public class Loan {
    private int id;
    private int userId;
    private int bookId;
    private String bookTitle;
    private String userName;
    private String loanDate;
    private String returnDate;
    private boolean returned;

    public Loan() {
    }

    public Loan(int id, int userId, int bookId, String bookTitle, String userName, String loanDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.loanDate = loanDate;
        this.returned = false;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public String getUserName() { return userName; }
    public String getLoanDate() { return loanDate; }
    public String getReturnDate() { return returnDate; }
    public boolean isReturned() { return returned; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setLoanDate(String loanDate) { this.loanDate = loanDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public void setReturned(boolean returned) { this.returned = returned; }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", bookTitle, userName, loanDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Loan loan = (Loan) obj;
        return id == loan.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
