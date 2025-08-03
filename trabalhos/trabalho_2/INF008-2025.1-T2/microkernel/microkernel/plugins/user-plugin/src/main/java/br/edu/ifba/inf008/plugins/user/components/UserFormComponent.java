package br.edu.ifba.inf008.plugins.user.components;

import br.edu.ifba.inf008.plugins.user.model.User;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class UserFormComponent extends VBox {
    private TextField nameField;
    private TextField emailField;
    private User currentUser;
    
    private UserFormCallback callback;
    private java.util.function.Consumer<Boolean> validationCallback;
    
    public interface UserFormCallback {
        void onSave(User user);
        void onCancel();
    }
    
    public UserFormComponent(UserFormCallback callback) {
        this.callback = callback;
        initializeComponents();
        setupLayout();
        setupEvents();
        applyStyles();
    }
    
    private void initializeComponents() {
        nameField = new TextField();
        nameField.setPromptText("Nome do usuário");
        
        emailField = new TextField();
        emailField.setPromptText("Email do usuário");
    }
    
    private void setupLayout() {
        setPadding(new Insets(20));
        setSpacing(15);
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        
        formGrid.add(new Label("Nome:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Email:"), 0, 1);
        formGrid.add(emailField, 1, 1);
        
        nameField.setPrefWidth(300);
        emailField.setPrefWidth(300);
        
        getChildren().add(formGrid);
    }
    
    private void setupEvents() {
        // Validation listeners
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> validateForm());
    }
    
    private void validateForm() {
        boolean isValid = !nameField.getText().trim().isEmpty() && 
                         !emailField.getText().trim().isEmpty() &&
                         isValidEmail(emailField.getText().trim());
        
        // Notify external validation callback
        if (validationCallback != null) {
            validationCallback.accept(isValid);
        }
        
        // Visual feedback for invalid fields
        updateFieldStyle(nameField, !nameField.getText().trim().isEmpty());
        updateFieldStyle(emailField, !emailField.getText().trim().isEmpty() && isValidEmail(emailField.getText().trim()));
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
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    public boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        
        if (nameField.getText().trim().isEmpty()) {
            errors.append("- Nome é obrigatório\n");
            nameField.setStyle("-fx-border-color: #f44336; -fx-border-width: 2px;");
        } else {
            nameField.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 1px;");
        }
        
        if (emailField.getText().trim().isEmpty()) {
            errors.append("- Email é obrigatório\n");
            emailField.setStyle("-fx-border-color: #f44336; -fx-border-width: 2px;");
        } else if (!isValidEmail(emailField.getText().trim())) {
            errors.append("- Email deve ter um formato válido\n"); 
            emailField.setStyle("-fx-border-color: #f44336; -fx-border-width: 2px;");
        } else {
            emailField.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 1px;");
        }
        
        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Validação");
            alert.setHeaderText("Por favor, corrija os seguintes erros:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            nameField.setText(user.getName());
            emailField.setText(user.getEmail());
        } else {
            clearForm();
        }
        validateForm();
    }
    
    public void clearForm() {
        nameField.clear();
        emailField.clear();
        currentUser = null;
        validateForm();
    }
    
    public void focusNameField() {
        nameField.requestFocus();
    }
    
    public void setValidationCallback(java.util.function.Consumer<Boolean> callback) {
        this.validationCallback = callback;
    }
    
    public User getCurrentUser() {
        if (validateInput()) {
            User user = currentUser != null ? currentUser : new User();
            user.setName(nameField.getText().trim());
            user.setEmail(emailField.getText().trim());
            return user;
        }
        return null;
    }
    
    private void applyStyles() {
        try {
            String cssPath = getClass().getResource("/user-plugin-styles.css").toExternalForm();
            getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.out.println("CSS file not found: " + e.getMessage());
        }
    }
}
