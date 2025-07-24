package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIController extends Application implements IUIController {
    private ICore core;
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

        // Menus
        Menu menuUsuarios = new Menu("Usuários");
        Menu menuLivros = new Menu("Livros");
        Menu menuEmprestimos = new Menu("Empréstimos");
        Menu menuRelatorios = new Menu("Relatórios");

        // Itens dos menus
        MenuItem itemGerenciarUsuarios = new MenuItem("Gerenciar Usuários");
        MenuItem itemGerenciarLivros = new MenuItem("Gerenciar Livros");
        MenuItem itemGerenciarEmprestimos = new MenuItem("Gerenciar Empréstimos");
        MenuItem itemRelatorioEmprestados = new MenuItem("Livros Emprestados");

        // Ações dos itens
        itemGerenciarUsuarios.setOnAction(e -> openTab("Usuários"));
        itemGerenciarLivros.setOnAction(e -> openTab("Livros"));
        itemGerenciarEmprestimos.setOnAction(e -> openTab("Empréstimos"));
        itemRelatorioEmprestados.setOnAction(e -> openTab("Relatórios"));

        // Organização dos menus
        menuUsuarios.getItems().add(itemGerenciarUsuarios);
        menuLivros.getItems().add(itemGerenciarLivros);
        menuEmprestimos.getItems().add(itemGerenciarEmprestimos);
        menuRelatorios.getItems().add(itemRelatorioEmprestados);
        menuBar.getMenus().addAll(menuUsuarios, menuLivros, menuEmprestimos, menuRelatorios);

        VBox vBox = new VBox(menuBar, tabPane);
        Scene scene = new Scene(vBox, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Abas abertas automaticamente
        itemGerenciarUsuarios.fire();
        itemGerenciarLivros.fire();
        itemGerenciarEmprestimos.fire();
        itemRelatorioEmprestados.fire();

        // Seleciona aba de Usuários
        selectTab("Usuários");

        // Inicializa plugins
        Core.getInstance().getPluginController().init();
    }

    private void openTab(String tabText) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }

        Tab tab = new Tab(tabText);
        tab.setContent(createTabContent(tabText));
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    private Node createTabContent(String section) {
        VBox aside = new VBox(10);
        aside.setPadding(new Insets(10));
        aside.setStyle("-fx-background-color: #f0f0f0;");
        aside.setPrefWidth(200);

        Label asideTitle = new Label("Ações");
        asideTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        aside.getChildren().add(asideTitle);

        VBox mainContent = new VBox();
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().add(new Label("Selecione uma ação no menu lateral."));

        switch (section) {
            case "Usuários":
                createActionButton(aside, mainContent, "Cadastrar Usuário", "Formulário de cadastro de usuário");
                createActionButton(aside, mainContent, "Editar Usuário", "Formulário de edição de usuário");
                createActionButton(aside, mainContent, "Excluir Usuário", "Opção para exclusão de usuário");
                createActionButton(aside, mainContent, "Listar Usuários", "Lista de usuários cadastrados");
                break;
            case "Livros":
                createActionButton(aside, mainContent, "Cadastrar Livro", "Formulário de cadastro de livro");
                createActionButton(aside, mainContent, "Editar Livro", "Formulário de edição de livro");
                createActionButton(aside, mainContent, "Excluir Livro", "Opção para exclusão de livro");
                createActionButton(aside, mainContent, "Listar Livros", "Lista de livros disponíveis");
                break;
            case "Empréstimos":
                createActionButton(aside, mainContent, "Registrar Empréstimo", "Formulário de registro de empréstimo");
                createActionButton(aside, mainContent, "Registrar Devolução", "Formulário de registro de devolução");
                break;
            case "Relatórios":
                createActionButton(aside, mainContent, "Ver Livros Emprestados", "Relatório dos livros atualmente emprestados");
                break;
            default:
                aside.getChildren().add(new Label("Nenhuma ação disponível."));
        }

        HBox tabLayout = new HBox(aside, mainContent);
        HBox.setHgrow(mainContent, Priority.ALWAYS);
        return tabLayout;
    }

private void createActionButton(VBox aside, VBox mainContent, String buttonText, String contentText) {
    Button actionButton = new Button(buttonText);
    actionButton.setMaxWidth(Double.MAX_VALUE);
    actionButton.setOnAction(e -> {
        mainContent.getChildren().clear();

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));

        Label title = new Label(contentText);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        contentBox.getChildren().add(title);

        switch (buttonText) {
            case "Cadastrar Usuário":
                contentBox.getChildren().addAll(
                    new Label("Nome:"),
                    new TextField(),
                    new Label("E-mail:"),
                    new TextField(),
                    new Label("Data de Registro:"),
                    new DatePicker(),
                    new Button("Salvar")
                );
                break;

            case "Editar Usuário":
                contentBox.getChildren().addAll(
                    new Label("ID do Usuário:"),
                    new TextField(),
                    new Label("Novo Nome:"),
                    new TextField(),
                    new Label("Novo E-mail:"),
                    new TextField(),
                    new Button("Atualizar")
                );
                break;

            case "Excluir Usuário":
                contentBox.getChildren().addAll(
                    new Label("Selecione o usuário para exclusão:"),
                    new ComboBox<>(), // Popular via banco futuramente
                    new Button("Excluir")
                );
                break;

            case "Listar Usuários":
                contentBox.getChildren().addAll(
                    new Label("Lista de usuários cadastrados:"),
                    new ListView<>() // Preencher com dados do banco
                );
                break;

            case "Cadastrar Livro":
                contentBox.getChildren().addAll(
                    new Label("Título:"),
                    new TextField(),
                    new Label("Autor:"),
                    new TextField(),
                    new Label("ISBN:"),
                    new TextField(),
                    new Label("Ano de Publicação:"),
                    new TextField(),
                    new Label("Quantidade de Cópias:"),
                    new TextField(),
                    new Button("Salvar")
                );
                break;

            case "Editar Livro":
                contentBox.getChildren().addAll(
                    new Label("ID do Livro:"),
                    new TextField(),
                    new Label("Novo Título:"),
                    new TextField(),
                    new Label("Novo Autor:"),
                    new TextField(),
                    new Button("Atualizar")
                );
                break;

            case "Excluir Livro":
                contentBox.getChildren().addAll(
                    new Label("Selecione o livro para exclusão:"),
                    new ComboBox<>(), // A ser preenchido com dados reais
                    new Button("Excluir")
                );
                break;

            case "Listar Livros":
                contentBox.getChildren().addAll(
                    new Label("Lista de livros disponíveis:"),
                    new ListView<>() // A ser preenchido
                );
                break;

            case "Registrar Empréstimo":
                contentBox.getChildren().addAll(
                    new Label("Usuário:"),
                    new ComboBox<>(), // Listar usuários
                    new Label("Livro:"),
                    new ComboBox<>(), // Listar livros disponíveis
                    new Label("Data do Empréstimo:"),
                    new DatePicker(),
                    new Button("Registrar")
                );
                break;

            case "Registrar Devolução":
                contentBox.getChildren().addAll(
                    new Label("Empréstimo:"),
                    new ComboBox<>(), // Listar empréstimos ativos
                    new Label("Data de Devolução:"),
                    new DatePicker(),
                    new Button("Registrar Devolução")
                );
                break;

            case "Ver Livros Emprestados":
                contentBox.getChildren().addAll(
                    new Label("Relatório de Livros Emprestados:"),
                    new TableView<>() // Pode adicionar colunas futuramente
                );
                break;

            default:
                contentBox.getChildren().add(new Label("Ação não implementada."));
        }

        mainContent.getChildren().add(contentBox);
    });
    aside.getChildren().add(actionButton);
}

    private void selectTab(String tabText) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                tabPane.getSelectionModel().select(tab);
                break;
            }
        }
    }

    public MenuItem createMenuItem(String menuText, String menuItemText) {
        Menu newMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText().equals(menuText)) {
                newMenu = menu;
                break;
            }
        }
        if (newMenu == null) {
            newMenu = new Menu(menuText);
            menuBar.getMenus().add(newMenu);
        }

        MenuItem menuItem = new MenuItem(menuItemText);
        newMenu.getItems().add(menuItem);
        return menuItem;
    }

    public boolean createTab(String tabText, Node contents) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                tabPane.getSelectionModel().select(tab);
                return false;
            }
        }
        Tab tab = new Tab(tabText);
        tab.setContent(contents);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        return true;
    }
}
