package com.sudoku;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// logics


/**
 * JavaFX App
 */
public class App extends Application {

    // Keep a static reference to the primary stage so other controllers can
    // switch scenes without needing to pass the Stage around
    private static Stage primaryStage;
    Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        primaryStage = stage; // store for static access
        stage.setTitle("Sudoku Game");

        MenuController menuController = new MenuController();
        Scene scene = menuController.menuScene;
        stage.setScene(scene);
        stage.show();
    }

    public static void setScene(Scene scene) {
        // switches scenes
        if (primaryStage == null) {
            System.err.println("[App] ERROR: primaryStage is null; cannot set scene.");
            return;
        }
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}