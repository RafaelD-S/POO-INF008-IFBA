package br.edu.ifba.inf008.plugins.loan.views;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import br.edu.ifba.inf008.plugins.loan.dao.LoanDAO;
import br.edu.ifba.inf008.plugins.loan.model.Loan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class LoanListView extends BorderPane {
    private TableView<Loan> loanTable;
    private final LoanDAO loanDAO;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    private TextField searchField;
    private ComboBox<String> searchTypeCombo;
    private CheckBox activeOnlyCheckBox;
    private Button newLoanButton;
    
    public LoanListView() {
        this.loanDAO = new LoanDAO();
        initializeComponents();
        setupLayout();
        loadLoans();
        applyStyles();
    }
    
    private void initializeComponents() {
        // Table setup with consistent styling
        loanTable = new TableView<>();
        loanTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableColumns();
        
        // Search components
        searchField = new TextField();
        searchField.setPromptText("Digite para pesquisar...");
        searchField.textProperty().addListener((obs, oldText, newText) -> performSearch());
        
        searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("Todos", "Usuário", "Livro");
        searchTypeCombo.setValue("Todos");
        searchTypeCombo.setOnAction(e -> performSearch());
        
        activeOnlyCheckBox = new CheckBox("Apenas empréstimos ativos");
        activeOnlyCheckBox.setOnAction(e -> performSearch());
        
        newLoanButton = new Button("Novo Empréstimo");
        newLoanButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 16 8 16; -fx-border-radius: 4px; -fx-background-radius: 4px;");
        newLoanButton.setOnAction(e -> showCreateLoanDialog());
        
        // Progress and status
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(50, 50);
        
        statusLabel = new Label("Carregando empréstimos...");
        statusLabel.setVisible(false);
    }
    
    private void setupTableColumns() {
        TableColumn<Loan, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setPrefWidth(50);
        
        TableColumn<Loan, String> userColumn = new TableColumn<>("Usuário");
        userColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserName()));
        userColumn.setPrefWidth(150);
        
        TableColumn<Loan, String> bookColumn = new TableColumn<>("Livro");
        bookColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBookTitle()));
        bookColumn.setPrefWidth(200);
        
        TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Data Empréstimo");
        loanDateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLoanDate()));
        loanDateColumn.setPrefWidth(120);
        
        TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Data Devolução");
        returnDateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReturnDate()));
        returnDateColumn.setPrefWidth(120);
        
        TableColumn<Loan, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().isReturned() ? "Devolvido" : "Ativo"));
        statusColumn.setPrefWidth(80);
        
        // Action column with consistent button styling
        TableColumn<Loan, Void> actionColumn = new TableColumn<>("Ações");
        actionColumn.setCellFactory(param -> new TableCell<Loan, Void>() {
            private final Button returnButton = new Button("Devolver");
            
            {
                returnButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 4 8 4 8; -fx-border-radius: 3px; -fx-background-radius: 3px;");
                returnButton.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (!loan.isReturned()) {
                        returnLoan(loan);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (loan.isReturned()) {
                        Label returnedLabel = new Label("Devolvido");
                        returnedLabel.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-padding: 4 8 4 8; -fx-border-radius: 3px; -fx-background-radius: 3px;");
                        setGraphic(returnedLabel);
                    } else {
                        setGraphic(returnButton);
                    }
                }
            }
        });
        actionColumn.setPrefWidth(100);
        
        loanTable.getColumns().addAll(idColumn, userColumn, bookColumn, 
                                     loanDateColumn, returnDateColumn, statusColumn, actionColumn);
    }
    
    private void setupLayout() {
        setPadding(new Insets(10));
        
        // Title
        Label titleLabel = new Label("Gerenciamento de Empréstimos");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Top controls with consistent styling
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10));
        searchBox.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
        searchBox.getChildren().addAll(
            new Label("Pesquisar:"), searchField,
            new Label("Por:"), searchTypeCombo,
            activeOnlyCheckBox
        );
        
        // Top bar with search and new loan button
        HBox topBar = new HBox(10);
        topBar.getChildren().addAll(searchBox, newLoanButton);
        topBar.setStyle("-fx-alignment: center-left;");
        
        // Make search box grow and button stay on the right
        HBox.setHgrow(searchBox, javafx.scene.layout.Priority.ALWAYS);
        
        // Status bar with consistent styling
        VBox statusBox = new VBox(5);
        statusBox.getChildren().addAll(progressIndicator, statusLabel);
        statusBox.setStyle("-fx-alignment: center; -fx-background-color: #f5f5f5; -fx-border-radius: 5; -fx-padding: 10;");
        
        // Main layout
        VBox mainContent = new VBox(10);
        mainContent.getChildren().addAll(titleLabel, topBar, loanTable, statusBox);
        VBox.setVgrow(loanTable, Priority.ALWAYS);
        
        setCenter(mainContent);
    }
    
    private void loadLoans() {
        showLoading(true, "Carregando empréstimos...");
        
        Task<List<Loan>> task = new Task<List<Loan>>() {
            @Override
            protected List<Loan> call() throws Exception {
                return loanDAO.findAll();
            }
            
            @Override
            protected void succeeded() {
                loanTable.getItems().clear();
                loanTable.getItems().addAll(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + " empréstimo(s) encontrado(s)");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Erro ao carregar empréstimos", getException());
                updateStatusLabel("Erro ao carregar dados");
            }
        };
        
        new Thread(task).start();
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim();
        String searchType = searchTypeCombo.getValue();
        boolean activeOnly = activeOnlyCheckBox.isSelected();
        
        showLoading(true, "Pesquisando...");
        
        Task<List<Loan>> task = new Task<List<Loan>>() {
            @Override
            protected List<Loan> call() throws Exception {
                List<Loan> results;
                
                if (activeOnly) {
                    results = loanDAO.findActiveLoans();
                } else if (searchText.isEmpty()) {
                    results = loanDAO.findAll();
                } else {
                    switch (searchType) {
                        case "Usuário":
                            results = loanDAO.searchByUserName(searchText);
                            break;
                        case "Livro":
                            results = loanDAO.searchByBookTitle(searchText);
                            break;
                        default:
                            // Search in both user name and book title
                            results = loanDAO.searchByUserName(searchText);
                            results.addAll(loanDAO.searchByBookTitle(searchText));
                            // Remove duplicates
                            results = results.stream().distinct().collect(java.util.stream.Collectors.toList());
                            break;
                    }
                }
                
                if (activeOnly && !searchText.isEmpty()) {
                    // Filter active loans from search results
                    results = results.stream()
                        .filter(loan -> !loan.isReturned())
                        .collect(java.util.stream.Collectors.toList());
                }
                
                return results;
            }
            
            @Override
            protected void succeeded() {
                loanTable.getItems().clear();
                loanTable.getItems().addAll(getValue());
                showLoading(false, null);
                updateStatusLabel(getValue().size() + " empréstimo(s) encontrado(s)");
            }
            
            @Override
            protected void failed() {
                showLoading(false, null);
                showError("Erro na pesquisa", getException());
                updateStatusLabel("Erro na pesquisa");
            }
        };
        
        new Thread(task).start();
    }
    
    private void returnLoan(Loan loan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Devolução");
        alert.setHeaderText("Devolver empréstimo");
        alert.setContentText("Deseja confirmar a devolução do livro '" + loan.getBookTitle() + 
                            "' pelo usuário '" + loan.getUserName() + "'?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String returnDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return loanDAO.returnLoan(loan.getId(), returnDate);
                }
                
                @Override
                protected void succeeded() {
                    if (getValue()) {
                        showInfo("Sucesso", "Empréstimo devolvido com sucesso!");
                        performSearch(); // Refresh the list
                    } else {
                        showError("Erro", "Não foi possível devolver o empréstimo.");
                    }
                }
                
                @Override
                protected void failed() {
                    showError("Erro ao devolver empréstimo", getException());
                }
            };
            
            new Thread(task).start();
        }
    }
    
    private void showLoading(boolean show, String message) {
        progressIndicator.setVisible(show);
        if (show && message != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(true);
        } else {
            statusLabel.setVisible(false);
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
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showCreateLoanDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Criar Novo Empréstimo");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        
        LoanCreateView createView = new LoanCreateView();
        createView.setOnLoanCreated(() -> {
            dialog.close();
            performSearch(); // Refresh the loan list
        });
        
        Scene scene = new Scene(createView, 500, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void applyStyles() {
        try {
            String cssPath = getClass().getResource("/loan-plugin-styles.css").toExternalForm();
            getStylesheets().add(cssPath);
            loanTable.getStyleClass().add("loan-table");
        } catch (Exception e) {
            // CSS file not found, continue without styling
            System.out.println("CSS file not found: " + e.getMessage());
        }
    }
}
