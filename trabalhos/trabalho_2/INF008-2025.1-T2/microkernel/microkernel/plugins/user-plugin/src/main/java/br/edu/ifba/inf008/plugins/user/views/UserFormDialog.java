package br.edu.ifba.inf008.plugins.user.views;

import br.edu.ifba.inf008.plugins.user.components.UserFormComponent;
import br.edu.ifba.inf008.plugins.user.dao.UserDAO;
import br.edu.ifba.inf008.plugins.user.model.User;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

public class UserFormDialog extends Dialog<User> {
    private UserFormComponent formComponent;
    private User originalUser;
    private UserDAO userDAO;
    
    public UserFormDialog(User user) {
        this.originalUser = user;
        this.userDAO = new UserDAO();
        
        initializeDialog();
        setupDialog();
    }
    
    private void initializeDialog() {
        formComponent = new UserFormComponent(null); // Pass null for callback
        
        if (originalUser != null) {
            setTitle("Editar Usuário");
            setHeaderText("Edite as informações do usuário");
            formComponent.setUser(originalUser);
        } else {
            setTitle("Novo Usuário");
            setHeaderText("Adicione um novo usuário");
            formComponent.clearForm();
        }
    }
    
    private void setupDialog() {
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        
        getDialogPane().setContent(formComponent);
        
        // Add custom buttons
        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
        
        // Set the result converter to properly handle button types
        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                User user = formComponent.getCurrentUser();
                if (user != null && formComponent.validateInput()) {
                    // Validate email uniqueness
                    if (isEmailTaken(user)) {
                        showEmailError();
                        return null; // Don't close dialog
                    }
                    return user;
                }
                return null; // Don't close dialog on validation failure
            }
            return null; // Cancel or close
        });
        
        // Get the actual buttons for additional configuration
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        Button cancelButton = (Button) getDialogPane().lookupButton(cancelButtonType);
        
        // Set initial button state
        saveButton.setDisable(true);
        formComponent.setValidationCallback((isValid) -> saveButton.setDisable(!isValid));
        
        // Focus on name field when dialog opens
        setOnShown(e -> formComponent.focusNameField());
    }
    
    private boolean isEmailTaken(User user) {
        try {
            int excludeId = originalUser != null ? originalUser.getId() : -1;
            return userDAO.emailExists(user.getEmail(), excludeId);
        } catch (Exception e) {
            showDatabaseError("Erro ao verificar email", e);
            return true; // Assume taken to prevent save on error
        }
    }
    
    private void showEmailError() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Email já existe");
        alert.setHeaderText("Email já cadastrado");
        alert.setContentText("Já existe um usuário cadastrado com este email. Por favor, use um email diferente.");
        alert.showAndWait();
    }
    
    private void showDatabaseError(String title, Exception e) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erro de Sistema");
        alert.setHeaderText(title);
        alert.setContentText("Erro ao acessar a base de dados: " + e.getMessage());
        alert.showAndWait();
    }
}
