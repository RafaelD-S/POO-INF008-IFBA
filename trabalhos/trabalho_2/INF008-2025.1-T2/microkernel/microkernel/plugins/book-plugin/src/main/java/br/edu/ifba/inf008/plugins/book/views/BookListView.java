package br.edu.ifba.inf008.plugins.book.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import br.edu.ifba.inf008.plugins.book.model.Book;
import br.edu.ifba.inf008.plugins.book.dao.BookDAO;

public class BookListView extends BorderPane {
    private TableView<Book> bookTable;
    private ObservableList<Book> bookList;
    private BookDAO bookDAO;
    private TextField searchField;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button refreshButton;
    
    public BookListView() {
        this.bookDAO = new BookDAO();
        this.bookList = FXCollections.observableArrayList();
        
        initializeComponents();
        setupLayout();
        setupEvents();
        loadBooks();
    }
    
    private void initializeComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Buscar livros...");
        searchField.setPrefWidth(300);
        
        // Buttons
        addButton = new Button("Adicionar");
        addButton.getStyleClass().addAll("btn", "btn-primary");
        
        editButton = new Button("Editar");
        editButton.getStyleClass().addAll("btn", "btn-secondary");
        editButton.setDisable(true);
        
        deleteButton = new Button("Excluir");
        deleteButton.getStyleClass().addAll("btn", "btn-danger");
        deleteButton.setDisable(true);
        
        refreshButton = new Button("Atualizar");
        refreshButton.getStyleClass().addAll("btn", "btn-outline");
        
        // Table
        bookTable = new TableView<>();
        bookTable.setItems(bookList);
        setupTableColumns();
    }
    
    private void setupTableColumns() {
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);
        
        TableColumn<Book, String> titleColumn = new TableColumn<>("Título");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(250);
        
        TableColumn<Book, String> authorColumn = new TableColumn<>("Autor");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(200);
        
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setPrefWidth(150);
        
        TableColumn<Book, Integer> yearColumn = new TableColumn<>("Ano");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publishedYear"));
        yearColumn.setPrefWidth(80);
        
        TableColumn<Book, Integer> copiesColumn = new TableColumn<>("Cópias");
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("copiesAvailable"));
        copiesColumn.setPrefWidth(80);
        
        bookTable.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, yearColumn, copiesColumn);
    }
    
    private void setupLayout() {
        // Top toolbar
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10));
        searchBox.getChildren().addAll(
            new Label("Buscar:"), 
            searchField,
            refreshButton
        );
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        // Button toolbar
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        
        VBox topBox = new VBox();
        topBox.getChildren().addAll(searchBox, buttonBox);
        
        // Layout
        setTop(topBox);
        setCenter(bookTable);
        
        setPadding(new Insets(10));
    }
    
    private void setupEvents() {
        // Search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBooks(newVal));
        
        // Table selection
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            editButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
        });
        
        // Double click to edit
        bookTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    editBook();
                }
            });
            return row;
        });
        
        // Button events
        addButton.setOnAction(e -> addBook());
        editButton.setOnAction(e -> editBook());
        deleteButton.setOnAction(e -> deleteBook());
        refreshButton.setOnAction(e -> loadBooks());
    }
    
    private void loadBooks() {
        try {
            bookList.clear();
            bookList.addAll(bookDAO.findAll());
        } catch (Exception e) {
            showError("Erro ao carregar livros", e.getMessage());
        }
    }
    
    private void filterBooks(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadBooks();
        } else {
            try {
                bookList.clear();
                bookList.addAll(bookDAO.findByTitleContaining(searchText.trim()));
            } catch (Exception e) {
                showError("Erro ao filtrar livros", e.getMessage());
            }
        }
    }
    
    private void addBook() {
        BookFormDialog dialog = new BookFormDialog(null);
        dialog.showAndWait().ifPresent(book -> {
            if (bookDAO.insert(book)) {
                loadBooks();
                showSuccess("Livro adicionado com sucesso!");
            } else {
                showError("Erro", "Não foi possível adicionar o livro.");
            }
        });
    }
    
    private void editBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            BookFormDialog dialog = new BookFormDialog(selectedBook);
            dialog.showAndWait().ifPresent(book -> {
                if (bookDAO.update(book)) {
                    loadBooks();
                    showSuccess("Livro atualizado com sucesso!");
                } else {
                    showError("Erro", "Não foi possível atualizar o livro.");
                }
            });
        }
    }
    
    private void deleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmar Exclusão");
            confirmDialog.setHeaderText("Excluir livro");
            confirmDialog.setContentText("Tem certeza que deseja excluir o livro \"" + selectedBook.getTitle() + "\"?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (bookDAO.delete(selectedBook.getId())) {
                        loadBooks();
                        showSuccess("Livro excluído com sucesso!");
                    } else {
                        showError("Erro", "Não foi possível excluir o livro.");
                    }
                }
            });
        }
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
