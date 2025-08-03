package br.edu.ifba.inf008.plugins.user.views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import br.edu.ifba.inf008.plugins.user.components.UserTableComponent;
import br.edu.ifba.inf008.plugins.user.dao.UserDAO;
import br.edu.ifba.inf008.plugins.user.model.User;

import java.util.List;

public class UserListView extends BorderPane implements UserTableComponent.UserTableCallback {
    private UserTableComponent userTable;
    private UserDAO userDAO;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    
    public UserListView() {
        this.userDAO = new UserDAO();
        initializeComponents();
        setupLayout();
        loadStylesheet();
        loadUsers();
    }
    
    private void initializeComponents() {
        userTable = new UserTableComponent(this);
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(50, 50);
        
        statusLabel = new Label("Carregando usuários...");
        statusLabel.setVisible(false);
    }
    
    private void setupLayout() {
        setPadding(new Insets(10));
        
        // Status bar
        VBox statusBox = new VBox(5);
        statusBox.getChildren().addAll(progressIndicator, statusLabel);
        statusBox.setStyle("-fx-alignment: center;");
        
        setCenter(userTable);
        setBottom(statusBox);
    }
    
    private void loadUsers() {
        showLoading(true, "Carregando usuários...");
        
        Task<List<User>> task = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                return userDAO.findAll();
            }
            
            @Override
            protected void succeeded() {
                userTable.setUsers(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + " usuário(s) encontrado(s)");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Erro ao carregar usuários", getException());
                updateStatusLabel("Erro ao carregar dados");
            }
        };
        
        new Thread(task).start();
    }
    
    private void searchUsers(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadUsers();
            return;
        }
        
        showLoading(true, "Pesquisando usuários...");
        
        Task<List<User>> task = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                return userDAO.findByNameContaining(searchText.trim());
            }
            
            @Override
            protected void succeeded() {
                userTable.setUsers(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + " usuário(s) encontrado(s) para '" + searchText + "'");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Erro ao pesquisar usuários", getException());
                updateStatusLabel("Erro na pesquisa");
            }
        };
        
        new Thread(task).start();
    }
    
    @Override
    public void onAdd() {
        UserFormDialog dialog = new UserFormDialog(null);
        dialog.showAndWait().ifPresent(user -> {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return userDAO.insert(user);
                }
                
                @Override
                protected void succeeded() {
                    if (getValue()) {
                        userTable.addUser(user);
                        showSuccess("Usuário adicionado com sucesso!");
                        updateStatusLabel("Usuário adicionado");
                    } else {
                        showError("Erro ao adicionar usuário", null);
                    }
                }
                
                @Override
                protected void failed() {
                    showError("Erro ao adicionar usuário", getException());
                }
            };
            
            new Thread(task).start();
        });
    }
    
    @Override
    public void onEdit(User user) {
        UserFormDialog dialog = new UserFormDialog(user);
        dialog.showAndWait().ifPresent(updatedUser -> {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return userDAO.update(updatedUser);
                }
                
                @Override
                protected void succeeded() {
                    if (getValue()) {
                        userTable.updateUser(updatedUser);
                        showSuccess("Usuário atualizado com sucesso!");
                        updateStatusLabel("Usuário atualizado");
                    } else {
                        showError("Erro ao atualizar usuário", null);
                    }
                }
                
                @Override
                protected void failed() {
                    showError("Erro ao atualizar usuário", getException());
                }
            };
            
            new Thread(task).start();
        });
    }
    
    @Override
    public void onDelete(User user) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return userDAO.delete(user.getId());
            }
            
            @Override
            protected void succeeded() {
                if (getValue()) {
                    userTable.removeUser(user);
                    showSuccess("Usuário excluído com sucesso!");
                    updateStatusLabel("Usuário excluído");
                } else {
                    showError("Erro ao excluir usuário", null);
                }
            }
            
            @Override
            protected void failed() {
                showError("Erro ao excluir usuário", getException());
            }
        };
        
        new Thread(task).start();
    }
    
    @Override
    public void onRefresh() {
        loadUsers();
    }
    
    @Override
    public void onSearch(String searchText) {
        searchUsers(searchText);
    }
    
    private void showLoading(boolean show, String message) {
        progressIndicator.setVisible(show);
        if (show && message != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        } else if (!show) {
            statusLabel.setVisible(false);
        }
    }
    
    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        // Hide after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(3),
            e -> statusLabel.setVisible(false)
        ));
        timeline.play();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String message, Throwable exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(message);
        
        if (exception != null) {
            alert.setContentText(exception.getMessage());
        }
        
        alert.showAndWait();
    }
    
    private void loadStylesheet() {
        try {
            String css = getClass().getResource("/styles/user-plugin.css").toExternalForm();
            getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("UserListView: Could not load stylesheet - " + e.getMessage());
        }
    }
}
