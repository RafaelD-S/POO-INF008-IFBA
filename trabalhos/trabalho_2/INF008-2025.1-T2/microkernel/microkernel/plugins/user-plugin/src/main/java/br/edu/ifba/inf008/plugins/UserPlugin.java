package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.user.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.user.views.UserListView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class UserPlugin implements IPlugin {
    private static final String PLUGIN_NAME = "Gerenciamento de Usuários";
    
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
                System.err.println("UserPlugin: UIController não encontrado");
                return false;
            }
            
            // Create the user management view
            UserListView userListView = new UserListView();
            
            // Create menu item
            boolean menuCreated = uiController.createMenuItem("Cadastros", "Usuários") != null;
            
            // Create tab
            boolean tabCreated = uiController.createTab("Usuários", userListView);
            
            if (menuCreated && tabCreated) {
                System.out.println("UserPlugin: Plugin inicializado com sucesso");
                return true;
            } else {
                System.err.println("UserPlugin: Erro ao criar interface do usuário");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("UserPlugin: Erro durante inicialização - " + e.getMessage());
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
