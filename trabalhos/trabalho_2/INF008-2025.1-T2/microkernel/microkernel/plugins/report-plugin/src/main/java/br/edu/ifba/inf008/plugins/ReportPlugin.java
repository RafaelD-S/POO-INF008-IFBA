package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.IUIController;
import br.edu.ifba.inf008.plugins.report.dao.DatabaseConnection;
import br.edu.ifba.inf008.plugins.report.views.LoanReportView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class ReportPlugin implements IPlugin {
    private static final String PLUGIN_NAME = "Relatórios";
    
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
                System.err.println("ReportPlugin: UIController não encontrado");
                return false;
            }
            
            // Create the report view
            LoanReportView reportView = new LoanReportView();
            
            // Create menu item
            boolean menuCreated = uiController.createMenuItem("Relatórios", "Relatório de Empréstimos") != null;
            
            // Create tab
            boolean tabCreated = uiController.createTab("Relatórios", reportView);
            
            if (menuCreated && tabCreated) {
                System.out.println("ReportPlugin: Plugin inicializado com sucesso");
                return true;
            } else {
                System.err.println("ReportPlugin: Erro ao criar interface do usuário");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("ReportPlugin: Erro durante inicialização - " + e.getMessage());
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
