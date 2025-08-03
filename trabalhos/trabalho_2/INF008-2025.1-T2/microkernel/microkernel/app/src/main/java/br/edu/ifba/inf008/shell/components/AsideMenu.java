package br.edu.ifba.inf008.shell.components;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AsideMenu extends VBox {

    public AsideMenu(String[] actions, Consumer<String> onActionClick) {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(200);
        setStyle("-fx-background-color: #f0f0f0;");

        Label title = new Label("Ações");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        getChildren().add(title);

        for (String action : actions) {
            Button btn = new Button(action);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> onActionClick.accept(action));
            getChildren().add(btn);
        }

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);
    }
}