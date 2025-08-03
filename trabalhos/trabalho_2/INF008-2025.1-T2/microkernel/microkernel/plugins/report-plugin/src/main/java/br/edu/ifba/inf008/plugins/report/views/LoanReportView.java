package br.edu.ifba.inf008.plugins.report.views;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifba.inf008.plugins.report.dao.LoanReportDAO;
import br.edu.ifba.inf008.plugins.report.models.Loan;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoanReportView extends VBox {
    private LoanReportDAO dao;
    private TableView<Loan> tableView;
    private ObservableList<Loan> loans;
    
    // Filter controls
    private TextField userFilterField;
    private TextField bookFilterField;
    private ComboBox<String> statusFilter;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button searchButton;
    private Button clearButton;
    private Button exportButton;
    
    // Stats labels
    private Label totalLoansLabel;
    private Label activeLoansLabel;
    private Label returnedLoansLabel;

    public LoanReportView() {
        this.dao = new LoanReportDAO();
        this.loans = FXCollections.observableArrayList();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
    }

    private void initializeComponents() {
        // Title
        Label titleLabel = new Label("Relatório de Empréstimos");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Filter controls
        userFilterField = new TextField();
        userFilterField.setPromptText("Filtrar por usuário...");
        
        bookFilterField = new TextField();
        bookFilterField.setPromptText("Filtrar por livro...");
        
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Todos", "Ativos", "Devolvidos");
        statusFilter.setValue("Todos");
        
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Data inicial");
        
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Data final");
        
        searchButton = new Button("Filtrar");
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        clearButton = new Button("Limpar");
        clearButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        
        exportButton = new Button("Exportar");
        exportButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        // Stats labels
        totalLoansLabel = new Label("Total de Empréstimos: 0");
        activeLoansLabel = new Label("Empréstimos Ativos: 0");
        returnedLoansLabel = new Label("Empréstimos Devolvidos: 0");
        
        // Table
        setupTable();
    }

    private void setupTable() {
        tableView = new TableView<>();
        tableView.setItems(loans);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // ID Column
        TableColumn<Loan, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);
        
        // User Column
        TableColumn<Loan, String> userColumn = new TableColumn<>("Usuário");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userColumn.setPrefWidth(150);
        
        // Book Column
        TableColumn<Loan, String> bookColumn = new TableColumn<>("Livro");
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        bookColumn.setPrefWidth(200);
        
        // Loan Date Column
        TableColumn<Loan, String> loanDateColumn = new TableColumn<>("Data Empréstimo");
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        loanDateColumn.setPrefWidth(120);
        
        // Return Date Column
        TableColumn<Loan, String> returnDateColumn = new TableColumn<>("Data Devolução");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateColumn.setPrefWidth(120);
        
        // Status Column
        TableColumn<Loan, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> {
            boolean returned = cellData.getValue().isReturned();
            return new javafx.beans.property.SimpleStringProperty(returned ? "Devolvido" : "Ativo");
        });
        statusColumn.setPrefWidth(80);
        
        // Style status column
        statusColumn.setCellFactory(column -> new TableCell<Loan, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Ativo".equals(item)) {
                        setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
                    }
                }
            }
        });
        
        tableView.getColumns().addAll(idColumn, userColumn, bookColumn, 
                                     loanDateColumn, returnDateColumn, statusColumn);
    }

    private void setupLayout() {
        // Title
        Label titleLabel = new Label("Relatório de Empréstimos");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Filter panel
        GridPane filterPanel = new GridPane();
        filterPanel.setHgap(10);
        filterPanel.setVgap(10);
        filterPanel.setPadding(new Insets(10));
        filterPanel.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");
        
        filterPanel.add(new Label("Usuário:"), 0, 0);
        filterPanel.add(userFilterField, 1, 0);
        filterPanel.add(new Label("Livro:"), 2, 0);
        filterPanel.add(bookFilterField, 3, 0);
        
        filterPanel.add(new Label("Status:"), 0, 1);
        filterPanel.add(statusFilter, 1, 1);
        filterPanel.add(new Label("Data Inicial:"), 2, 1);
        filterPanel.add(startDatePicker, 3, 1);
        
        filterPanel.add(new Label("Data Final:"), 0, 2);
        filterPanel.add(endDatePicker, 1, 2);
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(searchButton, clearButton, exportButton);
        filterPanel.add(buttonBox, 2, 2, 2, 1);
        
        // Stats panel
        HBox statsPanel = new HBox(30);
        statsPanel.setPadding(new Insets(10));
        statsPanel.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 5;");
        statsPanel.getChildren().addAll(totalLoansLabel, activeLoansLabel, returnedLoansLabel);
        
        // Main layout
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(titleLabel, filterPanel, statsPanel, tableView);
        
        VBox.setVgrow(tableView, Priority.ALWAYS);
    }

    private void setupEventHandlers() {
        searchButton.setOnAction(e -> applyFilters());
        clearButton.setOnAction(e -> clearFilters());
        exportButton.setOnAction(e -> exportData());
        
        // Real-time filtering
        userFilterField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                applyFilters();
            }
        });
        
        bookFilterField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                applyFilters();
            }
        });
    }

    private void loadData() {
        // Run database operation in background thread
        new Thread(() -> {
            try {
                List<Loan> allLoans = dao.getAllLoans();
                
                // Update UI in JavaFX Application Thread
                Platform.runLater(() -> {
                    loans.clear();
                    loans.addAll(allLoans);
                    updateStats();
                });
                
            } catch (SQLException e) {
                Platform.runLater(() -> {
                    showError("Erro ao carregar dados", "Erro na consulta ao banco: " + e.getMessage());
                });
            }
        }).start();
    }

    private void applyFilters() {
        // Run database operation in background thread
        new Thread(() -> {
            try {
                String userFilter = userFilterField.getText().trim();
                String bookFilter = bookFilterField.getText().trim();
                String statusFilterValue = statusFilter.getValue();
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();

                List<Loan> filteredLoansTemp;

                // Apply date range filter first if both dates are selected
                if (startDate != null && endDate != null) {
                    filteredLoansTemp = dao.getLoansByDateRange(startDate.toString(), endDate.toString());
                } else {
                    filteredLoansTemp = dao.getAllLoans();
                }

                // Apply other filters
                if (!userFilter.isEmpty()) {
                    filteredLoansTemp = filteredLoansTemp.stream()
                        .filter(loan -> loan.getUserName().toLowerCase().contains(userFilter.toLowerCase()))
                        .toList();
                }

                if (!bookFilter.isEmpty()) {
                    filteredLoansTemp = filteredLoansTemp.stream()
                        .filter(loan -> loan.getBookTitle().toLowerCase().contains(bookFilter.toLowerCase()))
                        .toList();
                }

                // Apply status filter
                if (!"Todos".equals(statusFilterValue)) {
                    boolean showReturned = "Devolvidos".equals(statusFilterValue);
                    filteredLoansTemp = filteredLoansTemp.stream()
                        .filter(loan -> loan.isReturned() == showReturned)
                        .toList();
                }

                final List<Loan> filteredLoans = filteredLoansTemp;

                // Update UI in JavaFX Application Thread
                Platform.runLater(() -> {
                    loans.clear();
                    loans.addAll(filteredLoans);
                    updateStats();
                });

            } catch (SQLException e) {
                Platform.runLater(() -> {
                    showError("Erro ao aplicar filtros", "Erro na consulta ao banco: " + e.getMessage());
                });
            }
        }).start();
    }

    private void clearFilters() {
        userFilterField.clear();
        bookFilterField.clear();
        statusFilter.setValue("Todos");
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        loadData();
    }

    private void exportData() {
        // Simple export to console (could be enhanced to save to file)
        StringBuilder sb = new StringBuilder();
        sb.append("RELATÓRIO DE EMPRÉSTIMOS\n");
        sb.append("========================\n\n");
        
        for (Loan loan : loans) {
            sb.append(String.format("ID: %d | Usuário: %s | Livro: %s | Data: %s | Status: %s\n",
                    loan.getId(),
                    loan.getUserName(),
                    loan.getBookTitle(),
                    loan.getLoanDate(),
                    loan.isReturned() ? "Devolvido" : "Ativo"));
        }
        
        sb.append(String.format("\nEstatísticas:\n"));
        sb.append(String.format("Total: %d empréstimos\n", loans.size()));
        sb.append(String.format("Ativos: %d\n", (int) loans.stream().filter(l -> !l.isReturned()).count()));
        sb.append(String.format("Devolvidos: %d\n", (int) loans.stream().filter(Loan::isReturned).count()));
        
        System.out.println(sb.toString());
        
        showInfo("Exportação realizada", "Relatório exportado para o console. Verifique a saída do sistema.");
    }

    private void updateStats() {
        int total = loans.size();
        long active = loans.stream().filter(loan -> !loan.isReturned()).count();
        long returned = loans.stream().filter(Loan::isReturned).count();
        
        totalLoansLabel.setText("Total de Empréstimos: " + total);
        activeLoansLabel.setText("Empréstimos Ativos: " + active);
        returnedLoansLabel.setText("Empréstimos Devolvidos: " + returned);
    }

    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação");
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
