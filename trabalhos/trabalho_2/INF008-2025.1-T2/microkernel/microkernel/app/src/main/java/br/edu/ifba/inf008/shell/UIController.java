package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIController extends Application implements IUIController {
    private MenuBar menuBar;
    private TabPane tabPane;
    private static UIController uiController;

    public UIController() {}

    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gerenciamento de Livraria");

        menuBar = new MenuBar();
        tabPane = new TabPane();
        tabPane.setSide(Side.TOP);

        VBox vBox = new VBox(menuBar, tabPane);
        Scene scene = new Scene(vBox, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicializa plugins que criarão suas próprias interfaces
        Core.getInstance().getPluginController().init();
    }

    @Override
    public MenuItem createMenuItem(String menuText, String menuItemText) {
        Menu targetMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText().equals(menuText)) {
                targetMenu = menu;
                break;
            }
        }
        if (targetMenu == null) {
            targetMenu = new Menu(menuText);
            menuBar.getMenus().add(targetMenu);
        }

        MenuItem menuItem = new MenuItem(menuItemText);
        targetMenu.getItems().add(menuItem);
        
        // Configura ação para abrir a aba correspondente
        menuItem.setOnAction(e -> openTab(menuItemText));
        
        return menuItem;
    }

    public boolean createTab(String tabText, Node contents) {
        // Verifica se a aba já existe
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                tabPane.getSelectionModel().select(tab);
                return false; // Aba já existe
            }
        }
        
        // Cria nova aba
        Tab tab = new Tab(tabText);
        tab.setContent(contents);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        return true; // Nova aba criada
    }

    private void openTab(String tabText) {
        // Procura por aba existente
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }
        
        // Se não encontrou a aba, cria uma aba vazia 
        // (os plugins devem criar suas próprias abas via createTab)
        Tab tab = new Tab(tabText);
        tab.setContent(new VBox(new Label("Conteúdo será fornecido pelos plugins.")));
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
}
