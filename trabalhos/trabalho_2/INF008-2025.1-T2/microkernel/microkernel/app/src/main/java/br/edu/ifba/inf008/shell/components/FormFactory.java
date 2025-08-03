package br.edu.ifba.inf008.shell.components;

import java.util.List;

import br.edu.ifba.inf008.dao.BookDAO;
import br.edu.ifba.inf008.dao.LoanDAO;
import br.edu.ifba.inf008.dao.ReportDAO;
import br.edu.ifba.inf008.dao.UserDAO;
import br.edu.ifba.inf008.model.Book;
import br.edu.ifba.inf008.model.Loan;
import br.edu.ifba.inf008.model.LoanReport;
import br.edu.ifba.inf008.model.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FormFactory {

    private Node generateLoanReportTable(List<LoanReport> data) {
        TableView<LoanReport> reportTable = new TableView<>();

        TableColumn<LoanReport, String> bookCol = new TableColumn<>("Book");
        bookCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBookTitle()));

        TableColumn<LoanReport, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBookAuthor()));

        TableColumn<LoanReport, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUserName()));

        TableColumn<LoanReport, String> loanDateCol = new TableColumn<>("Loan Date");
        loanDateCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLoanDate().toString()));

        reportTable.getColumns().addAll(bookCol, authorCol, userCol, loanDateCol);
        reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        reportTable.getItems().setAll(data);

        return reportTable;
    }


    public static Node getForm(String action) {
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));

        Label title = new Label(action);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        contentBox.getChildren().add(title);

        switch (action) {

            // === USERS ===
            case "Add User":
                TextField nameField = new TextField();
                TextField emailField = new TextField();
                Button saveBtn = new Button("Save");

                saveBtn.setOnAction(ev -> {
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();

                    if (!name.isEmpty() && !email.isEmpty()) {
                        boolean success = new UserDAO().insert(name, email);
                        if (success) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "User added successfully!");
                            alert.showAndWait();
                            nameField.clear();
                            emailField.clear();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Failed to add user.").show();
                        }
                    } else {
                        new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Name:"), nameField,
                    new Label("Email:"), emailField,
                    saveBtn
                );
                break;

            case "Edit User":
                UserDAO userDAO3 = new UserDAO();
                ComboBox<User> userCombo = new ComboBox<>();
                userCombo.getItems().addAll(userDAO3.findAll());

                TextField newNameField = new TextField();
                TextField newEmailField = new TextField();
                Button updateBtn = new Button("Update");

                newNameField.setDisable(true);
                newEmailField.setDisable(true);
                updateBtn.setDisable(true);

                userCombo.setOnAction(ev -> {
                    User selected = userCombo.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        newNameField.setDisable(false);
                        newEmailField.setDisable(false);
                        updateBtn.setDisable(false);
                        newNameField.setText(selected.getName());
                        newEmailField.setText(selected.getEmail());
                    }
                });

                updateBtn.setOnAction(ev -> {
                    User selected = userCombo.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        String newName = newNameField.getText().trim();
                        String newEmail = newEmailField.getText().trim();

                        if (!newName.isEmpty() && !newEmail.isEmpty()) {
                            boolean success = userDAO3.update(selected.getId(), newName, newEmail);
                            if (success) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User updated successfully!");
                                alert.showAndWait();
                                newNameField.clear();
                                newEmailField.clear();
                                userCombo.getItems().setAll(userDAO3.findAll());
                                userCombo.getSelectionModel().clearSelection();
                                newNameField.setDisable(true);
                                newEmailField.setDisable(true);
                                updateBtn.setDisable(true);
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Failed to update user.").show();
                            }
                        } else {
                            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
                        }
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Select User:"), userCombo,
                    new Label("New Name:"), newNameField,
                    new Label("New Email:"), newEmailField,
                    updateBtn
                );
                break;


            case "Delete User":
                UserDAO userDAOdel = new UserDAO();
                ComboBox<User> userDelCombo = new ComboBox<>();
                userDelCombo.getItems().addAll(userDAOdel.findAll());
                Button delUserBtn = new Button("Delete");

                delUserBtn.setOnAction(ev -> {
                    User user = userDelCombo.getValue();
                    if (user != null && userDAOdel.delete(user.getId())) {
                        new Alert(Alert.AlertType.INFORMATION, "User deleted successfully.").showAndWait();
                        userDelCombo.getItems().setAll(userDAOdel.findAll());
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete user.").show();
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Select user to delete:"), userDelCombo, delUserBtn
                );
                break;

            case "List Users":
                UserDAO userDAOList = new UserDAO();
                TableView<User> userTable = new TableView<>();

                TableColumn<User, String> nameCol = new TableColumn<>("Name");
                nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

                TableColumn<User, String> emailCol = new TableColumn<>("Email");
                emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

                userTable.getColumns().addAll(nameCol, emailCol);
                userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                userTable.getItems().setAll(userDAOList.findAll());

                Button refreshUsers = new Button("🔄 Atualizar");
                refreshUsers.setOnAction(e -> {
                    userTable.getItems().setAll(userDAOList.findAll());
                });

                contentBox.getChildren().addAll(new Label("User List:"), refreshUsers, userTable);
                break;


            // === BOOKS ===
            case "Add Book":
                TextField titleField = new TextField();
                TextField authorField = new TextField();
                TextField isbnField = new TextField();
                TextField yearField = new TextField();
                TextField copiesField = new TextField();
                Button saveBookBtn = new Button("Save");

                saveBookBtn.setOnAction(ev -> {
                    try {
                        String title2 = titleField.getText().trim();
                        String author = authorField.getText().trim();
                        String isbn = isbnField.getText().trim();
                        int year = Integer.parseInt(yearField.getText().trim());
                        int copies = Integer.parseInt(copiesField.getText().trim());

                        if (!title2.isEmpty() && !author.isEmpty() && !isbn.isEmpty()) {
                            boolean success = new BookDAO().insert(title2, author, isbn, year, copies);
                            if (success) {
                                new Alert(Alert.AlertType.INFORMATION, "Book added successfully!").showAndWait();
                                titleField.clear(); authorField.clear(); isbnField.clear();
                                yearField.clear(); copiesField.clear();
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Failed to add book.").show();
                            }
                        }
                    } catch (NumberFormatException e) {
                        new Alert(Alert.AlertType.WARNING, "Invalid number input.").show();
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Title:"), titleField,
                    new Label("Author:"), authorField,
                    new Label("ISBN:"), isbnField,
                    new Label("Published Year:"), yearField,
                    new Label("Available Copies:"), copiesField,
                    saveBookBtn
                );
                break;

            case "Edit Book":
                BookDAO bookDAO = new BookDAO();
                ComboBox<Book> bookCombo = new ComboBox<>();
                bookCombo.getItems().addAll(bookDAO.findAll());

                TextField titleField2 = new TextField();
                TextField authorField2 = new TextField();
                TextField isbnField2 = new TextField();
                TextField yearField2 = new TextField();
                TextField copiesField2 = new TextField();
                Button updateBtn2 = new Button("Update");

                titleField2.setDisable(true);
                authorField2.setDisable(true);
                isbnField2.setDisable(true);
                yearField2.setDisable(true);
                copiesField2.setDisable(true);
                updateBtn2.setDisable(true);

                bookCombo.setOnAction(e -> {
                    Book selected = bookCombo.getValue();
                    if (selected != null) {
                        titleField2.setText(selected.getTitle());
                        authorField2.setText(selected.getAuthor());
                        isbnField2.setText(selected.getIsbn());
                        yearField2.setText(String.valueOf(selected.getPublishedYear()));
                        copiesField2.setText(String.valueOf(selected.getCopiesAvailable()));

                        titleField2.setDisable(false);
                        authorField2.setDisable(false);
                        isbnField2.setDisable(false);
                        yearField2.setDisable(false);
                        copiesField2.setDisable(false);
                        updateBtn2.setDisable(false);
                    }
                });

                updateBtn2.setOnAction(ev -> {
                    Book selected = bookCombo.getValue();
                    if (selected != null) {
                        try {
                            String title2 = titleField2.getText().trim();
                            String author = authorField2.getText().trim();
                            String isbn = isbnField2.getText().trim();
                            int year = Integer.parseInt(yearField2.getText().trim());
                            int copies = Integer.parseInt(copiesField2.getText().trim());

                            boolean success = bookDAO.update(selected.getId(), title2, author, isbn, year, copies);
                            if (success) {
                                new Alert(Alert.AlertType.INFORMATION, "Book updated successfully!").showAndWait();
                                bookCombo.getItems().setAll(bookDAO.findAll());
                                bookCombo.getSelectionModel().clearSelection();
                                titleField2.clear(); authorField2.clear(); isbnField2.clear();
                                yearField2.clear(); copiesField2.clear();

                                titleField2.setDisable(true);
                                authorField2.setDisable(true);
                                isbnField2.setDisable(true);
                                yearField2.setDisable(true);
                                copiesField2.setDisable(true);
                                updateBtn2.setDisable(true);
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Failed to update book.").show();
                            }
                        } catch (NumberFormatException ex) {
                            new Alert(Alert.AlertType.WARNING, "Invalid number format.").show();
                        }
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Select Book:"), bookCombo,
                    new Label("Title:"), titleField2,
                    new Label("Author:"), authorField2,
                    new Label("ISBN:"), isbnField2,
                    new Label("Published Year:"), yearField2,
                    new Label("Copies Available:"), copiesField2,
                    updateBtn2
                );
                break;


            case "Delete Book":
                BookDAO bookDAOdel = new BookDAO();
                ComboBox<Book> bookDelCombo = new ComboBox<>();
                bookDelCombo.getItems().addAll(bookDAOdel.findAll());
                Button delBookBtn = new Button("Delete");

                delBookBtn.setOnAction(ev -> {
                    Book book = bookDelCombo.getValue();
                    if (book != null && bookDAOdel.delete(book.getId())) {
                        new Alert(Alert.AlertType.INFORMATION, "Book deleted successfully.").showAndWait();
                        bookDelCombo.getItems().setAll(bookDAOdel.findAll());
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete book.").show();
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Select book to delete:"), bookDelCombo, delBookBtn
                );

                break;
            case "List Books":
                BookDAO bookDAOList = new BookDAO();
                TableView<Book> bookTable = new TableView<>();

                TableColumn<Book, String> tCol = new TableColumn<>("Title");
                tCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

                TableColumn<Book, String> aCol = new TableColumn<>("Author");
                aCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));

                TableColumn<Book, String> iCol = new TableColumn<>("ISBN");
                iCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn()));

                TableColumn<Book, Integer> yCol = new TableColumn<>("Year");
                yCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPublishedYear()).asObject());

                TableColumn<Book, Integer> cCol = new TableColumn<>("Copies");
                cCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCopiesAvailable()).asObject());

                bookTable.getColumns().addAll(tCol, aCol, iCol, yCol, cCol);
                bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                bookTable.getItems().setAll(bookDAOList.findAll());

                Button refreshBooks = new Button("🔄 Atualizar");
                refreshBooks.setOnAction(e -> {
                    bookTable.getItems().setAll(bookDAOList.findAll());
                });

                contentBox.getChildren().addAll(new Label("Books:"), refreshBooks, bookTable);
                break;



            // === LOANS ===
            case "Register Loan":
                UserDAO udaoLoan = new UserDAO();
                BookDAO bdaoLoan = new BookDAO();
                LoanDAO ldaoLoan = new LoanDAO();

                ComboBox<User> userLoanCB = new ComboBox<>();
                userLoanCB.getItems().addAll(udaoLoan.findAll());

                ComboBox<Book> bookLoanCB = new ComboBox<>();
                bookLoanCB.getItems().addAll(bdaoLoan.findAvailable());

                Button registerLoanBtn = new Button("Register");

                registerLoanBtn.setOnAction(ev -> {
                    User user = userLoanCB.getValue();
                    Book book = bookLoanCB.getValue();

                    if (user != null && book != null) {
                        boolean loaned = ldaoLoan.registerLoan(user.getId(), book.getId());
                        boolean updated = bdaoLoan.decreaseAvailableCopies(book.getId());

                        if (loaned && updated) {
                            new Alert(Alert.AlertType.INFORMATION, "Loan registered and inventory updated!").showAndWait();
                            bookLoanCB.getItems().setAll(bdaoLoan.findAvailable());
                            userLoanCB.getSelectionModel().clearSelection();
                            bookLoanCB.getSelectionModel().clearSelection();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Error registering loan or updating inventory.").show();
                        }
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("User:"), userLoanCB,
                    new Label("Book:"), bookLoanCB,
                    registerLoanBtn
                );
                break;


            case "Register Return":
                LoanDAO loanDAO = new LoanDAO();

                ComboBox<Loan> loanCombo = new ComboBox<>();
                loanCombo.getItems().addAll(loanDAO.findActiveLoans());

                Button returnBtn = new Button("Register Return");

                returnBtn.setOnAction(e -> {
                    Loan loan = loanCombo.getValue();

                    if (loan != null) {
                        boolean success = loanDAO.registerReturn(loan.getId());

                        if (success) {
                            new Alert(Alert.AlertType.INFORMATION, "Return registered and book stock updated!").showAndWait();
                            loanCombo.getItems().setAll(loanDAO.findActiveLoans());
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Failed to register return.").show();
                        }
                    }
                });

                contentBox.getChildren().addAll(new Label("Select Loan to Return:"), loanCombo, returnBtn);
                break;


            // === REPORTS ===
            case "View Borrowed Books":
                LoanDAO loanDAO2 = new LoanDAO();
                TableView<LoanReport> reportTable = new TableView<>();

                TableColumn<LoanReport, String> bookCol = new TableColumn<>("Book");
                bookCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));

                TableColumn<LoanReport, String> authorCol = new TableColumn<>("Author");
                authorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookAuthor()));

                TableColumn<LoanReport, String> userCol = new TableColumn<>("User");
                userCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserName()));

                TableColumn<LoanReport, String> loanDateCol = new TableColumn<>("Loan Date");
                loanDateCol.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getLoanDate().toString())
                );

                reportTable.getColumns().addAll(bookCol, authorCol, userCol, loanDateCol);
                reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                reportTable.getItems().setAll(loanDAO2.getLoanReport());

                Button refreshReports = new Button("🔄 Atualizar");
                refreshReports.setOnAction(e -> {
                    reportTable.getItems().setAll(loanDAO2.getLoanReport());
                });

                contentBox.getChildren().addAll(new Label("Borrowed Books Report:"), refreshReports, reportTable);
                break;


            case "Borrowed But Not Returned":
                LoanDAO dao1 = new LoanDAO();
                List<LoanReport> notReturned = dao1.getBorrowedButNotReturnedReport();

                Button refreshNotReturned = new Button("🔄 Atualizar");
                TableView<LoanReport> tableNotReturned = (TableView<LoanReport>) new FormFactory().generateLoanReportTable(notReturned);

                refreshNotReturned.setOnAction(e -> {
                    tableNotReturned.getItems().setAll(dao1.getBorrowedButNotReturnedReport());
                });

                contentBox.getChildren().addAll(
                    new Label("Books Borrowed But Not Returned"),
                    refreshNotReturned,
                    tableNotReturned
                );
                break;

            case "Returned Books":
                LoanDAO dao2 = new LoanDAO();
                List<LoanReport> returned = dao2.getReturnedBooksReport();

                Button refreshReturned = new Button("🔄 Atualizar");
                TableView<LoanReport> tableReturned = (TableView<LoanReport>) new FormFactory().generateLoanReportTable(returned);

                refreshReturned.setOnAction(e -> {
                    tableReturned.getItems().setAll(dao2.getReturnedBooksReport());
                });

                contentBox.getChildren().addAll(
                    new Label("Returned Books Report"),
                    refreshReturned,
                    tableReturned
                );
                break;


            // === DEFAULT ===
            default:
                contentBox.getChildren().add(new Label("Action not recognized."));
        }

        return contentBox;
    }
}
