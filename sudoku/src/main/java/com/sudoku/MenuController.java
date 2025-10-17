package com.sudoku;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

// handles menu logic and user interactions
public class MenuController {
    double w = 1280;
    double h = 720;

    double difficulty = 2; // default medium
    boolean game_assist = true;

    static Scene menuScene;

    public MenuController() {
        // Initialize menu state
        VBox vBox = new VBox();
        menuScene = new Scene(vBox, w, h);

        // title for the menu scene
        vBox.setStyle("-fx-background-color: lightblue;");
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));

        // text
        Text title = new Text("Sudoku Game");
        vBox.getChildren().add(title);

        // button to start game
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-pref-width: 100px;");

        startButton.setOnAction(e -> {
            System.out.println("Start Game button clicked");
            GameController gameController = new GameController();
            gameController.startNewGame(difficulty, game_assist);
        });
        vBox.getChildren().add(startButton);

        // game assist
        Button assistButton = new Button("Toggle Game Assist");
        Label assistLabel = new Label("Game Assist: ON");
        assistButton.setStyle("-fx-pref-width: 150px;");
        assistButton.setOnAction(e -> {
            assistLabel.setText(game_assist ? "Game Assist: OFF" : "Game Assist: ON");
            game_assist = !game_assist;
            System.err.println("Game Assist is now " + (game_assist));
        });

        vBox.getChildren().addAll(assistButton, assistLabel);

        // difficulty selection
        Text difficultyText = new Text("Select Difficulty:");
        Text selectedDifficulty = new Text("Current Difficulty: Medium");

        vBox.getChildren().addAll(difficultyText, selectedDifficulty);

        // difficulty buttons
        Button easyButton = new Button("Easy");
        easyButton.setStyle("-fx-pref-width: 100px;");
        easyButton.setOnAction(e -> {
            difficulty = 1;
            System.out.println("Difficulty set to Easy");
            selectedDifficulty.setText("Current Difficulty: Easy");
        });

        Button mediumButton = new Button("Medium");
        mediumButton.setStyle("-fx-pref-width: 100px;");
        mediumButton.setOnAction(e -> {
            difficulty = 2;
            System.out.println("Difficulty set to Medium");
            selectedDifficulty.setText("Current Difficulty: Medium");
        });

        Button hardButton = new Button("Hard");
        hardButton.setStyle("-fx-pref-width: 100px;");
        hardButton.setOnAction(e -> {
            difficulty = 3;
            System.out.println("Difficulty set to Hard");
            selectedDifficulty.setText("Current Difficulty: Hard");
        });

        vBox.getChildren().addAll(easyButton, mediumButton, hardButton);
    }
}
