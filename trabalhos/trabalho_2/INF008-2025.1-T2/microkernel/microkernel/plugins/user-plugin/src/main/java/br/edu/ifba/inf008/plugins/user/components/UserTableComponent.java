package br.edu.ifba.inf008.plugins.user.components;

import br.edu.ifba.inf008.plugins.user.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class UserTableComponent extends VBox {
    private TextField searchField;
    private TableView<User> userTable;
    private ObservableList<User> users;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;
    
    private UserTableCallback callback;
    
    public interface UserTableCallback {
        void onAdd();
        void onEdit(User user);
        void onDelete(User user);
        void onRefresh();
        void onSearch(String searchText);
    }
    
    public UserTableComponent(UserTableCallback callback) {
        this.callback = callback;
        this.users = FXCollections.observableArrayList();
        initializeComponents();
        setupLayout();
        setupEvents();
    }
    
    private void initializeComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Pesquisar por nome...");
        
        // Table
        userTable = new TableView<>();
        userTable.setItems(users);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        userTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    handleEdit();
                }
            });
            return row;
        });
        
        setupTableColumns();
        
        // Buttons with consistent styling
        addButton = new Button("Adicionar");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-border-radius: 4px; -fx-background-radius: 4px;");
        
        editButton = new Button("Editar");
        editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-border-radius: 4px; -fx-background-radius: 4px;");
        editButton.setDisable(true);
        
        deleteButton = new Button("Excluir");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-border-radius: 4px; -fx-background-radius: 4px;");
        deleteButton.setDisable(true);
        
        refreshButton = new Button("Atualizar");
        refreshButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-border-radius: 4px; -fx-background-radius: 4px;");
    }
    
    private void setupTableColumns() {
        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);
        idColumn.setResizable(false);
        
        TableColumn<User, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);
        
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(250);
        
        TableColumn<User, String> registeredColumn = new TableColumn<>("Data de Cadastro");
        registeredColumn.setCellValueFactory(new PropertyValueFactory<>("registeredAt"));
        registeredColumn.setPrefWidth(150);
        
        userTable.getColumns().addAll(idColumn, nameColumn, emailColumn, registeredColumn);
    }
    
    private void setupLayout() {
        setPadding(new Insets(10));
        setSpacing(10);
        
        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(
            new Label("Pesquisar:"), 
            searchField,
            refreshButton
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        // Button bar
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        
        // Table
        VBox.setVgrow(userTable, Priority.ALWAYS);
        
        getChildren().addAll(searchBox, userTable, buttonBox);
    }
    
    private void setupEvents() {
        // Selection listener
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            editButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
        });
        
        // Button events
        addButton.setOnAction(e -> handleAdd());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        refreshButton.setOnAction(e -> handleRefresh());
        
        // Search events
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (callback != null) {
                callback.onSearch(newVal);
            }
        });
    }
    
    private void handleAdd() {
        if (callback != null) {
            callback.onAdd();
        }
    }
    
    private void handleEdit() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null && callback != null) {
            callback.onEdit(selectedUser);
        }
    }
    
    private void handleDelete() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar Exclusão");
            confirmAlert.setHeaderText("Deseja excluir o usuário?");
            confirmAlert.setContentText("Nome: " + selectedUser.getName() + "\nEmail: " + selectedUser.getEmail());
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK && callback != null) {
                    callback.onDelete(selectedUser);
                }
            });
        }
    }
    
    private void handleRefresh() {
        if (callback != null) {
            callback.onRefresh();
        }
    }
    
    public void setUsers(java.util.List<User> userList) {
        users.clear();
        users.addAll(userList);
    }
    
    public void addUser(User user) {
        users.add(user);
    }
    
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                break;
            }
        }
    }
    
    public void removeUser(User user) {
        users.remove(user);
    }
    
    public User getSelectedUser() {
        return userTable.getSelectionModel().getSelectedItem();
    }
    
    public void clearSelection() {
        userTable.getSelectionModel().clearSelection();
    }
    
    public void selectUser(User user) {
        userTable.getSelectionModel().select(user);
    }
}
