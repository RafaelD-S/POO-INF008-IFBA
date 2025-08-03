package br.edu.ifba.inf008.plugins.loan.views;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import br.edu.ifba.inf008.plugins.loan.dao.LoanDAO;
import br.edu.ifba.inf008.plugins.loan.model.Loan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanCreateView extends BorderPane {
    private ComboBox<UserItem> userComboBox;
    private ComboBox<BookItem> bookComboBox;
    private DatePicker loanDatePicker;
    private Button createButton;
    private Button cancelButton;
    private LoanDAO loanDAO;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    private Runnable onLoanCreated;
    
    // Helper classes for ComboBox items
    public static class UserItem {
        private int id;
        private String name;
        
        public UserItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        
        @Override
        public String toString() { return name; }
    }
    
    public static class BookItem {
        private int id;
        private String title;
        private String author;
        private boolean available;
        
        public BookItem(int id, String title, String author, boolean available) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.available = available;
        }
        
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public boolean isAvailable() { return available; }
        
        @Override
        public String toString() { 
            String status = available ? "" : " (Indisponível)";
            return title + " - " + author + status;
        }
    }
    
    public LoanCreateView() {
        this.loanDAO = new LoanDAO();
        initializeComponents();
        setupLayout();
        loadData();
        applyStyles();
    }
    
    public void setOnLoanCreated(Runnable callback) {
        this.onLoanCreated = callback;
    }
    
    private void initializeComponents() {
        userComboBox = new ComboBox<>();
        userComboBox.setPromptText("Selecione um usuário");
        userComboBox.setMaxWidth(Double.MAX_VALUE);
        
        bookComboBox = new ComboBox<>();
        bookComboBox.setPromptText("Selecione um livro");
        bookComboBox.setMaxWidth(Double.MAX_VALUE);
        
        loanDatePicker = new DatePicker(LocalDate.now());
        loanDatePicker.setMaxWidth(Double.MAX_VALUE);
        
        createButton = new Button("Criar Empréstimo");
        createButton.setOnAction(e -> createLoan());
        createButton.setDefaultButton(true);
        createButton.setDisable(true);
        createButton.getStyleClass().add("create-button");
        
        cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(e -> clearForm());
        cancelButton.getStyleClass().add("cancel-button");
        
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(30, 30);
        
        statusLabel = new Label();
        statusLabel.setVisible(false);
        
        // Enable create button only when both user and book are selected
        userComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateCreateButtonState());
        bookComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateCreateButtonState());
    }
    
    private void setupLayout() {
        setPadding(new Insets(20));
        
        // Title
        Label titleLabel = new Label("Criar Novo Empréstimo");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20, 0, 20, 0));
        
        formGrid.add(new Label("Usuário:"), 0, 0);
        formGrid.add(userComboBox, 1, 0);
        
        formGrid.add(new Label("Livro:"), 0, 1);
        formGrid.add(bookComboBox, 1, 1);
        
        formGrid.add(new Label("Data do Empréstimo:"), 0, 2);
        formGrid.add(loanDatePicker, 1, 2);
        
        // Make the form fields expand
        formGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(120));
        formGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints());
        formGrid.getColumnConstraints().get(1).setHgrow(javafx.scene.layout.Priority.ALWAYS);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(createButton, cancelButton);
        buttonBox.setStyle("-fx-alignment: center-right;");
        
        // Status
        HBox statusBox = new HBox(10);
        statusBox.getChildren().addAll(progressIndicator, statusLabel);
        statusBox.setStyle("-fx-alignment: center;");
        statusBox.setPadding(new Insets(10, 0, 0, 0));
        
        // Main layout
        VBox mainContent = new VBox(20);
        mainContent.getChildren().addAll(titleLabel, formGrid, buttonBox, statusBox);
        
        setCenter(mainContent);
    }
    
    private void loadData() {
        showLoading(true, "Carregando dados...");
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                loadUsers();
                loadBooks();
                return null;
            }
            
            @Override
            protected void succeeded() {
                showLoading(false, null);
                updateStatusLabel("Dados carregados com sucesso");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Erro ao carregar dados", getException());
                updateStatusLabel("Erro ao carregar dados");
            }
        };
        
        new Thread(task).start();
    }
    
    private void loadUsers() throws SQLException {
        String sql = "SELECT user_id, name FROM users ORDER BY name";
        
        try (Connection conn = br.edu.ifba.inf008.plugins.loan.dao.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            javafx.application.Platform.runLater(() -> userComboBox.getItems().clear());
            
            while (rs.next()) {
                UserItem user = new UserItem(rs.getInt("user_id"), rs.getString("name"));
                javafx.application.Platform.runLater(() -> userComboBox.getItems().add(user));
            }
        }
    }
    
    private void loadBooks() throws SQLException {
        String sql = "SELECT b.book_id, b.title, b.author, " +
                     "CASE WHEN l.loan_id IS NULL THEN 1 ELSE 0 END as available " +
                     "FROM books b " +
                     "LEFT JOIN loans l ON b.book_id = l.book_id AND l.return_date IS NULL " +
                     "ORDER BY b.title";
        
        try (Connection conn = br.edu.ifba.inf008.plugins.loan.dao.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            javafx.application.Platform.runLater(() -> bookComboBox.getItems().clear());
            
            while (rs.next()) {
                BookItem book = new BookItem(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getBoolean("available")
                );
                javafx.application.Platform.runLater(() -> bookComboBox.getItems().add(book));
            }
        }
    }
    
    private void updateCreateButtonState() {
        boolean canCreate = userComboBox.getValue() != null && 
                           bookComboBox.getValue() != null &&
                           (bookComboBox.getValue() == null || bookComboBox.getValue().isAvailable());
        createButton.setDisable(!canCreate);
        
        // Visual feedback for ComboBoxes
        updateComboBoxStyle(userComboBox, userComboBox.getValue() != null);
        updateComboBoxStyle(bookComboBox, bookComboBox.getValue() != null && 
                           (bookComboBox.getValue() == null || bookComboBox.getValue().isAvailable()));
    }
    
    private void updateComboBoxStyle(ComboBox<?> comboBox, boolean isValid) {
        comboBox.getStyleClass().removeAll("field-error", "field-valid");
        if (comboBox.getValue() != null) {
            if (isValid) {
                comboBox.getStyleClass().add("field-valid");
            } else {
                comboBox.getStyleClass().add("field-error");
            }
        }
    }
    
    private void createLoan() {
        UserItem selectedUser = userComboBox.getValue();
        BookItem selectedBook = bookComboBox.getValue();
        LocalDate selectedDate = loanDatePicker.getValue();
        
        // Clear previous validations
        clearValidationStyles();
        
        // Validate fields and show visual feedback
        StringBuilder errors = new StringBuilder();
        boolean hasErrors = false;
        
        if (selectedUser == null) {
            errors.append("- Selecione um usuário\n");
            userComboBox.getStyleClass().add("field-error");
            hasErrors = true;
        }
        
        if (selectedBook == null) {
            errors.append("- Selecione um livro\n");
            bookComboBox.getStyleClass().add("field-error");
            hasErrors = true;
        } else if (!selectedBook.isAvailable()) {
            errors.append("- O livro selecionado não está disponível\n");
            bookComboBox.getStyleClass().add("field-error");
            hasErrors = true;
        }
        
        if (selectedDate == null) {
            errors.append("- Selecione uma data\n");
            loanDatePicker.getStyleClass().add("field-error");
            hasErrors = true;
        } else if (selectedDate.isAfter(LocalDate.now())) {
            errors.append("- A data do empréstimo não pode ser futura\n");
            loanDatePicker.getStyleClass().add("field-error");
            hasErrors = true;
        }
        
        if (hasErrors) {
            showValidationError("Por favor, corrija os seguintes erros:", errors.toString());
            return;
        }
        
        showLoading(true, "Criando empréstimo...");
        
        Loan newLoan = new Loan();
        newLoan.setUserId(selectedUser.getId());
        newLoan.setBookId(selectedBook.getId());
        newLoan.setLoanDate(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return loanDAO.save(newLoan);
            }
            
            @Override
            protected void succeeded() {
                showLoading(false, null);
                if (getValue()) {
                    showInfo("Sucesso", "Empréstimo criado com sucesso!");
                    clearForm();
                    if (onLoanCreated != null) {
                        onLoanCreated.run();
                    }
                } else {
                    showError("Erro", "Não foi possível criar o empréstimo.");
                }
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Erro ao criar empréstimo", getException());
            }
        };
        
        new Thread(task).start();
    }
    
    private void clearValidationStyles() {
        userComboBox.getStyleClass().removeAll("field-error", "field-valid");
        bookComboBox.getStyleClass().removeAll("field-error", "field-valid");
        loanDatePicker.getStyleClass().removeAll("field-error", "field-valid");
    }
    
    private void showValidationError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Validação");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void clearForm() {
        userComboBox.setValue(null);
        bookComboBox.setValue(null);
        loanDatePicker.setValue(LocalDate.now());
        statusLabel.setVisible(false);
        clearValidationStyles();
    }
    
    private void showLoading(boolean show, String message) {
        progressIndicator.setVisible(show);
        if (show && message != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        }
    }
    
    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        
        // Hide status after 3 seconds
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(3), e -> statusLabel.setVisible(false))
        );
        timeline.play();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, Throwable exception) {
        String message = exception.getMessage();
        if (message == null) {
            message = exception.getClass().getSimpleName();
        }
        showError(title, message);
        exception.printStackTrace();
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void applyStyles() {
        try {
            String cssPath = getClass().getResource("/loan-plugin-styles.css").toExternalForm();
            getStylesheets().add(cssPath);
            getStyleClass().add("loan-form");
        } catch (Exception e) {
            // CSS file not found, continue without styling
            System.out.println("CSS file not found: " + e.getMessage());
        }
    }
}
