package br.edu.ifba.inf008.shell.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
                contentBox.getChildren().addAll(
                    new Label("Name:"), new TextField(),
                    new Label("Email:"), new TextField(),
                    new Label("Registration Date:"), new DatePicker(),
                    new Button("Save")
                );
                break;

            case "Edit User":
                contentBox.getChildren().addAll(
                    new Label("User ID:"), new TextField(),
                    new Label("New Name:"), new TextField(),
                    new Label("New Email:"), new TextField(),
                    new Button("Update")
                );
                break;

            case "Delete User":
                contentBox.getChildren().addAll(
                    new Label("Select user to delete:"),
                    new ComboBox<>(), // To be populated from database
                    new Button("Delete")
                );
                break;

            case "List Users":
                contentBox.getChildren().addAll(
                    new Label("Registered users:"),
                    new ListView<>() // To be populated
                );
                break;

            // === BOOKS ===
            case "Add Book":
                contentBox.getChildren().addAll(
                    new Label("Title:"), new TextField(),
                    new Label("Author:"), new TextField(),
                    new Label("ISBN:"), new TextField(),
                    new Label("Publication Year:"), new TextField(),
                    new Label("Available Copies:"), new TextField(),
                    new Button("Save")
                );
                break;

            case "Edit Book":
                contentBox.getChildren().addAll(
                    new Label("Book ID:"), new TextField(),
                    new Label("New Title:"), new TextField(),
                    new Label("New Author:"), new TextField(),
                    new Button("Update")
                );
                break;

            case "Delete Book":
                contentBox.getChildren().addAll(
                    new Label("Select book to delete:"),
                    new ComboBox<>(), // To be populated
                    new Button("Delete")
                );
                break;

            case "List Books":
                contentBox.getChildren().addAll(
                    new Label("Available books:"),
                    new ListView<>() // To be populated
                );
                break;

            // === LOANS ===
            case "Register Loan":
                contentBox.getChildren().addAll(
                    new Label("User:"), new ComboBox<>(), // Populate with users
                    new Label("Book:"), new ComboBox<>(), // Populate with available books
                    new Label("Loan Date:"), new DatePicker(),
                    new Button("Register")
                );
                break;

            case "Register Return":
                contentBox.getChildren().addAll(
                    new Label("Active Loan:"), new ComboBox<>(), // Populate with active loans
                    new Label("Return Date:"), new DatePicker(),
                    new Button("Confirm Return")
                );
                break;

            // === REPORTS ===
            case "View Borrowed Books":
                contentBox.getChildren().addAll(
                    new Label("Currently borrowed books:"),
                    new TableView<>() // Columns and data to be implemented
                );
                break;

            // === DEFAULT ===
            default:
                contentBox.getChildren().add(new Label("Action not recognized."));
        }

        return contentBox;
    }
}
