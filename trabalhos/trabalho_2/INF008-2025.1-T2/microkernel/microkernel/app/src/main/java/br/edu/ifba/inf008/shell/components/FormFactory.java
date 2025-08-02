package br.edu.ifba.inf008.shell.components;

import br.edu.ifba.inf008.dao.BookDAO;
import br.edu.ifba.inf008.dao.LoanDAO;
import br.edu.ifba.inf008.dao.ReportDAO;
import br.edu.ifba.inf008.dao.UserDAO;
import br.edu.ifba.inf008.model.Book;
import br.edu.ifba.inf008.model.Loan;
import br.edu.ifba.inf008.model.LoanReport;
import br.edu.ifba.inf008.model.User;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FormFactory {

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
                        newNameField.setText(selected.toString().split(" \\(")[0]); // crude extract
                        newEmailField.setText(selected.toString().split("\\(")[1].replace(")", ""));
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
                ListView<User> userList = new ListView<>();
                userList.getItems().addAll(userDAOList.findAll());
                contentBox.getChildren().addAll(new Label("Registered users:"), userList);
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
                BookDAO bookDAOedit = new BookDAO();
                ComboBox<Book> bookCombo2 = new ComboBox<>();
                bookCombo2.getItems().addAll(bookDAOedit.findAll());

                TextField titleEdit2 = new TextField();
                TextField authorEdit2 = new TextField();
                TextField isbnEdit2 = new TextField();
                TextField yearEdit2 = new TextField();
                TextField copiesEdit2 = new TextField();
                Button updateBookBtn2 = new Button("Update");

                titleEdit2.setDisable(true); authorEdit2.setDisable(true);
                isbnEdit2.setDisable(true); yearEdit2.setDisable(true);
                copiesEdit2.setDisable(true); updateBookBtn2.setDisable(true);

                bookCombo2.setOnAction(e -> {
                    Book selected = bookCombo2.getValue();
                    if (selected != null) {
                        // (Simular carregamento real com campos fictícios se Book.java ainda não tem todos os campos)
                        titleEdit2.setText(selected.toString().split(" - ")[0]);
                        authorEdit2.setText(selected.toString().split(" - ")[1]);
                        isbnEdit2.setText("1234567890");  // Substitua por selected.getIsbn() quando disponível
                        yearEdit2.setText("2000");        // Substitua por selected.getPublishedYear()
                        copiesEdit2.setText("5");         // Substitua por selected.getCopiesAvailable()

                        titleEdit2.setDisable(false); authorEdit2.setDisable(false);
                        isbnEdit2.setDisable(false); yearEdit2.setDisable(false);
                        copiesEdit2.setDisable(false); updateBookBtn2.setDisable(false);
                    }
                });

                updateBookBtn2.setOnAction(ev -> {
                    Book selected = bookCombo2.getValue();
                    if (selected != null) {
                        try {
                            String title2 = titleEdit2.getText();
                            String author = authorEdit2.getText();
                            String isbn = isbnEdit2.getText();
                            int year = Integer.parseInt(yearEdit2.getText());
                            int copies = Integer.parseInt(copiesEdit2.getText());

                            boolean success = bookDAOedit.update(selected.getId(), title2, author, isbn, year, copies);
                            if (success) {
                                new Alert(Alert.AlertType.INFORMATION, "Book updated successfully!").showAndWait();
                                bookCombo2.getItems().setAll(bookDAOedit.findAll());
                                bookCombo2.getSelectionModel().clearSelection();
                                titleEdit2.clear(); authorEdit2.clear(); isbnEdit2.clear();
                                yearEdit2.clear(); copiesEdit2.clear();
                                titleEdit2.setDisable(true); authorEdit2.setDisable(true);
                                isbnEdit2.setDisable(true); yearEdit2.setDisable(true);
                                copiesEdit2.setDisable(true); updateBookBtn2.setDisable(true);
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Failed to update book.").show();
                            }
                        } catch (NumberFormatException e) {
                            new Alert(Alert.AlertType.WARNING, "Invalid numeric values").show();
                        }
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Select Book:"), bookCombo2,
                    new Label("New Title:"), titleEdit2,
                    new Label("New Author:"), authorEdit2,
                    new Label("New ISBN:"), isbnEdit2,
                    new Label("New Year:"), yearEdit2,
                    new Label("New Copies:"), copiesEdit2,
                    updateBookBtn2
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
                ListView<Book> bookList = new ListView<>();
                bookList.getItems().addAll(bookDAOList.findAll());
                contentBox.getChildren().addAll(new Label("Available books:"), bookList);
                break;

            // === LOANS ===
            case "Register Loan":
                UserDAO udao = new UserDAO();
                BookDAO bdao = new BookDAO();
                LoanDAO ldao = new LoanDAO();

                ComboBox<User> userCB = new ComboBox<>();
                userCB.getItems().addAll(udao.findAll());

                ComboBox<Book> bookCB = new ComboBox<>();
                bookCB.getItems().addAll(bdao.findAvailable());

                Button registerBtn = new Button("Register");

                registerBtn.setOnAction(ev -> {
                    User u = userCB.getValue();
                    Book b = bookCB.getValue();
                    if (u != null && b != null) {
                        boolean success = ldao.registerLoan(u.getId(), b.getId());
                        if (success) {
                            new Alert(Alert.AlertType.INFORMATION, "Loan registered!").showAndWait();
                            bookCB.getItems().setAll(bdao.findAvailable());
                            userCB.getSelectionModel().clearSelection();
                            bookCB.getSelectionModel().clearSelection();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Error registering loan").show();
                        }
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("User:"), userCB,
                    new Label("Book:"), bookCB,
                    registerBtn
                );
                break;

            case "Register Return":
                LoanDAO ldao2 = new LoanDAO();
                ComboBox<Loan> loanCB = new ComboBox<>();
                loanCB.getItems().addAll(ldao2.findActiveLoans());

                Button returnBtn = new Button("Register Return");

                returnBtn.setOnAction(ev -> {
                    Loan loan = loanCB.getValue();
                    if (loan != null) {
                        boolean success = ldao2.registerReturn(loan.getId());
                        if (success) {
                            new Alert(Alert.AlertType.INFORMATION, "Return registered!").showAndWait();
                            loanCB.getItems().setAll(ldao2.findActiveLoans());
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Failed to register return").show();
                        }
                    }
                });

                contentBox.getChildren().addAll(
                    new Label("Select active loan:"), loanCB,
                    returnBtn
                );

                break;

            // === REPORTS ===
            case "View Borrowed Books":
                TableView<LoanReport> table = new TableView<>();

                TableColumn<LoanReport, String> titleCol = new TableColumn<>("Title");
                titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBookTitle()));

                TableColumn<LoanReport, String> authorCol = new TableColumn<>("Author");
                authorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBookAuthor()));

                TableColumn<LoanReport, String> userCol = new TableColumn<>("User");
                userCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUserName()));

                TableColumn<LoanReport, String> dateCol = new TableColumn<>("Loan Date");
                dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLoanDate().toString()));

                table.getColumns().addAll(titleCol, authorCol, userCol, dateCol);
                ReportDAO reportDAO = new ReportDAO();
                table.getItems().addAll(reportDAO.getActiveLoanReports());
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                contentBox.getChildren().addAll(new Label("Currently borrowed books:"), table);
                break;

            // === DEFAULT ===
            default:
                contentBox.getChildren().add(new Label("Action not recognized."));
        }

        return contentBox;
    }
}
