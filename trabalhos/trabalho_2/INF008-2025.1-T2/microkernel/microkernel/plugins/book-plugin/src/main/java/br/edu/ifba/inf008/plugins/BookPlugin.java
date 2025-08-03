package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.book.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.book.views.BookListView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class BookPlugin implements IPlugin {
    private static final String PLUGIN_NAME = "Gerenciamento de Livros";
    
    @Override
    public boolean init() {
        try {
            // Test database connection
            if (!DatabaseConnection.testConnection()) {
                showDatabaseError();
                return false;
            }
            
            // Get UI controller from core
            IUIController uiController = ICore.getInstance().getUIController();
            if (uiController == null) {
                System.err.println("BookPlugin: UIController não encontrado");
                return false;
            }
            
            // Create the book management view
            BookListView bookListView = new BookListView();
            
            // Create menu item
            boolean menuCreated = uiController.createMenuItem("Cadastros", "Livros") != null;
            
            // Create tab
            boolean tabCreated = uiController.createTab("Livros", bookListView);
            
            if (menuCreated && tabCreated) {
                System.out.println("BookPlugin: Plugin inicializado com sucesso");
                return true;
            } else {
                System.err.println("BookPlugin: Erro ao criar interface do usuário");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("BookPlugin: Erro durante inicialização - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void showDatabaseError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Conexão");
            alert.setHeaderText("Não foi possível conectar ao banco de dados");
            alert.setContentText("Verifique se o servidor MariaDB está rodando na porta 3307\n" +
                               "e se o banco 'bookstore' existe.");
            alert.showAndWait();
        });
    }
}
