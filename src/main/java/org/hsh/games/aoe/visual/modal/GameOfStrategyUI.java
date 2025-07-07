package org.hsh.games.aoe.visual.modal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hsh.games.aoe.GameOfStrategy;
import org.hsh.games.aoe.services.PlayerService;
import org.hsh.games.aoe.entities.Player;

public class GameOfStrategyUI extends Application {

    private GameOfStrategy game = new GameOfStrategy();
    private PlayerService playerService;

    @Override
    public void start(Stage primaryStage) {
        // Criar elementos da interface gráfica
        Label nameLabel = new Label("Nome da aldeia:");
        TextField nameField = new TextField();
        Button startButton = new Button("Iniciar Jogo");

        // Adicionar evento ao botão de iniciar jogo
        startButton.setOnAction(event -> {
            String farmName = nameField.getText();
            if (!farmName.isEmpty()) {
                playerService = new PlayerService(new Player(farmName));
                showMainMenu(primaryStage);
            } else {
                System.out.println("Por favor, insira um nome para a aldeia.");
            }
        });

        // Organizar elementos em um layout VBox
        VBox root = new VBox(10);
        root.getChildren().addAll(nameLabel, nameField, startButton);

        // Configurar a cena e exibir a janela
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game of Strategy");
        primaryStage.show();
    }

    private void showMainMenu(Stage primaryStage) {
        // Criar elementos da interface gráfica
        Button searchButton = new Button("Procurar por recursos");
        Button buildButton = new Button("Construir/Atualizar edifícios");
        Button statusButton = new Button("Ver status");

        // Adicionar eventos aos botões
        searchButton.setOnAction(event -> displayResourcesAvailableToSearchFor());
        buildButton.setOnAction(event -> displayBuildingTypes());
        statusButton.setOnAction(event -> displayStatus());

        // Organizar elementos em um layout VBox
        VBox root = new VBox(10);
        root.getChildren().addAll(searchButton, buildButton, statusButton);

        // Configurar a cena e exibir a janela
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu Principal");
        primaryStage.show();
    }


    private void displayResourcesAvailableToSearchFor() {
        // Implementar lógica para mostrar recursos disponíveis para pesquisa
        System.out.println("Lista de Recursos:");
    }

    private void displayBuildingTypes() {
        // Implementar lógica para mostrar tipos de edifícios disponíveis para construção/atualização
        System.out.println("Lista de Edifícios:");
    }

    private void displayStatus() {
        // Implementar lógica para mostrar status do jogo
        System.out.println("Status do Jogo:");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
