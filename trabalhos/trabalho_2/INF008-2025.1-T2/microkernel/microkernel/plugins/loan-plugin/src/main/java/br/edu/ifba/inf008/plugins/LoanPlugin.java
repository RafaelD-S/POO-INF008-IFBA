package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.loan.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.loan.views.LoanListView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class LoanPlugin implements IPlugin {
    private static final String PLUGIN_NAME = "Gerenciamento de Empréstimos";
    
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
                System.err.println("LoanPlugin: UIController não encontrado");
                return false;
            }
            
            // Create the loan management view
            LoanListView loanListView = new LoanListView();
            
            // Create menu item
            boolean menuCreated = uiController.createMenuItem("Operações", "Empréstimos") != null;
            
            // Create tab
            boolean tabCreated = uiController.createTab("Empréstimos", loanListView);
            
            if (menuCreated && tabCreated) {
                System.out.println("LoanPlugin: Plugin inicializado com sucesso");
                return true;
            } else {
                System.err.println("LoanPlugin: Erro ao criar interface do usuário");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("LoanPlugin: Erro durante inicialização - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void showDatabaseError() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Conexão");
            alert.setHeaderText("Erro ao conectar com o banco de dados");
            alert.setContentText("Não foi possível estabelecer conexão com o banco de dados.\n" +
                               "Verifique se o MariaDB está rodando e as configurações estão corretas.");
            alert.showAndWait();
        });
    }
}
