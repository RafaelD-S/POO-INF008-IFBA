package br.edu.ifba.inf008.shell.views;

import br.edu.ifba.inf008.shell.components.AsideMenu;
import br.edu.ifba.inf008.shell.components.FormFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BookView extends HBox {

    private VBox mainContent;

    public BookView() {
        String[] actions = {
            "Add Book",
            "Edit Book",
            "Delete Book",
            "List Books"
        };

        this.mainContent = new VBox();
        this.mainContent.setPadding(new Insets(20));
        this.mainContent.getChildren().add(new Label("Select an action."));

        AsideMenu aside = new AsideMenu(actions, this::loadAction);
        this.getChildren().addAll(aside, mainContent);
        HBox.setHgrow(mainContent, Priority.ALWAYS);
    }

    private void loadAction(String action) {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(FormFactory.getForm(action));
    }
}
