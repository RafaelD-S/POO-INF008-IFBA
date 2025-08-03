package br.edu.ifba.inf008.plugins.book.views;

import br.edu.ifba.inf008.plugins.book.model.Book;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class BookFormDialog extends Dialog<Book> {
    private TextField titleField;
    private TextField authorField;
    private TextField isbnField;
    private TextField yearField;
    private TextField copiesField;
    private Book currentBook;
    
    public BookFormDialog(Book book) {
        this.currentBook = book;
        
        setTitle(book == null ? "Adicionar Livro" : "Editar Livro");
        setHeaderText(book == null ? "Cadastro de novo livro" : "Edição de livro");
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        titleField = new TextField();
        titleField.setPromptText("Título do livro");
        titleField.setPrefWidth(300);
        
        authorField = new TextField();
        authorField.setPromptText("Autor do livro");
        authorField.setPrefWidth(300);
        
        isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        isbnField.setPrefWidth(300);
        
        yearField = new TextField();
        yearField.setPromptText("Ano de publicação");
        yearField.setPrefWidth(300);
        
        copiesField = new TextField();
        copiesField.setPromptText("Número de cópias");
        copiesField.setPrefWidth(300);
        
        grid.add(new Label("Título:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Ano:"), 0, 3);
        grid.add(yearField, 1, 3);
        grid.add(new Label("Cópias:"), 0, 4);
        grid.add(copiesField, 1, 4);
        
        // Load existing data if editing
        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            yearField.setText(String.valueOf(book.getPublishedYear()));
            copiesField.setText(String.valueOf(book.getCopiesAvailable()));
        }
        
        getDialogPane().setContent(grid);
        
        // Enable/Disable save button depending on whether form is valid
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);
        
        // Validation
        titleField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(saveButton));
        authorField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(saveButton));
        isbnField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(saveButton));
        yearField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(saveButton));
        copiesField.textProperty().addListener((observable, oldValue, newValue) -> validateForm(saveButton));
        
        // Initial validation
        validateForm(saveButton);
        
        // Convert the result when the save button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return createBookFromForm();
            }
            return null;
        });
        
        // Request focus on title field
        titleField.requestFocus();
        
        // Apply styles
        applyStyles();
    }
    
    private void applyStyles() {
        try {
            String cssPath = getClass().getResource("/book-plugin-styles.css").toExternalForm();
            getDialogPane().getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.out.println("CSS file not found: " + e.getMessage());
        }
    }
    
    private void validateForm(Button saveButton) {
        boolean isValid = !titleField.getText().trim().isEmpty() &&
                         !authorField.getText().trim().isEmpty() &&
                         !isbnField.getText().trim().isEmpty() &&
                         isValidYear(yearField.getText()) &&
                         isValidCopies(copiesField.getText());
        
        saveButton.setDisable(!isValid);
        
        // Visual feedback for fields
        updateFieldStyle(titleField, !titleField.getText().trim().isEmpty());
        updateFieldStyle(authorField, !authorField.getText().trim().isEmpty());
        updateFieldStyle(isbnField, !isbnField.getText().trim().isEmpty());
        updateFieldStyle(yearField, isValidYear(yearField.getText()));
        updateFieldStyle(copiesField, isValidCopies(copiesField.getText()));
    }
    
    private void updateFieldStyle(TextField field, boolean isValid) {
        field.getStyleClass().removeAll("field-error", "field-valid");
        if (!field.getText().trim().isEmpty()) {
            if (isValid) {
                field.getStyleClass().add("field-valid");
            } else {
                field.getStyleClass().add("field-error");
            }
        }
    }
    
    private boolean isValidYear(String year) {
        try {
            int yearInt = Integer.parseInt(year);
            return yearInt > 0 && yearInt <= java.time.Year.now().getValue();
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isValidCopies(String copies) {
        try {
            int copiesInt = Integer.parseInt(copies);
            return copiesInt >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private Book createBookFromForm() {
        try {
            // Validate before creating
            StringBuilder errors = new StringBuilder();
            
            if (titleField.getText().trim().isEmpty()) {
                errors.append("- Título é obrigatório\n");
            }
            if (authorField.getText().trim().isEmpty()) {
                errors.append("- Autor é obrigatório\n");
            }
            if (isbnField.getText().trim().isEmpty()) {
                errors.append("- ISBN é obrigatório\n");
            }
            if (!isValidYear(yearField.getText())) {
                errors.append("- Ano deve ser um número válido entre 1 e " + java.time.Year.now().getValue() + "\n");
            }
            if (!isValidCopies(copiesField.getText())) {
                errors.append("- Número de cópias deve ser um número não negativo\n");
            }
            
            if (errors.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Validação");
                alert.setHeaderText("Por favor, corrija os seguintes erros:");
                alert.setContentText(errors.toString());
                alert.showAndWait();
                return null;
            }
            
            Book book = currentBook != null ? currentBook : new Book();
            book.setTitle(titleField.getText().trim());
            book.setAuthor(authorField.getText().trim());
            book.setIsbn(isbnField.getText().trim());
            book.setPublishedYear(Integer.parseInt(yearField.getText()));
            book.setCopiesAvailable(Integer.parseInt(copiesField.getText()));
            return book;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Validação");
            alert.setHeaderText("Dados inválidos");
            alert.setContentText("Por favor, verifique os dados inseridos: " + e.getMessage());
            alert.showAndWait();
            return null;
        }
    }
}
