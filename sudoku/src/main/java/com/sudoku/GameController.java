package com.sudoku;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.*;
import javafx.scene.layout.*;

import com.sudoku.GeneratePuzzle;

// handles game logic and user interactions

public class GameController {
    Scene gameScene;
    double gridData[][];
    int gridSize = 9;

    // DEBUG
    boolean debug = true;

    double difficulty = 2; // default medium

    public GameController() {
        // initialize game state
    }

    // MenuController button calls this to start a new game
    public void startNewGame(double difficulty) {
        this.difficulty = difficulty;
        // start a new game
        getPuzzle();

        // create a new game scene
        GridPane gridPane = new GridPane();
        gameScene = new Scene(gridPane, 600, 600);
        gridPane.setGridLinesVisible(true);
        
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);

        // populate the grid with the puzzle data and visually separate 3x3 boxes
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
            TextField cell = new TextField();
            cell.setPrefWidth(60);
            cell.setPrefHeight(60);
            cell.setStyle("-fx-font-size: 20px; -fx-alignment: center;");

            // add thicker borders for 3x3 grid separation
            StringBuilder borderStyle = new StringBuilder();
            borderStyle.append("-fx-border-color: black;");
            borderStyle.append("-fx-border-width: ");
            borderStyle.append((i % 3 == 0 ? "2" : "1")).append(" "); // top
            borderStyle.append((j % 3 == 2 ? "2" : "1")).append(" "); // right
            borderStyle.append((i % 3 == 2 ? "2" : "1")).append(" "); // bottom
            borderStyle.append((j % 3 == 0 ? "2" : "1")).append(";"); // left

            cell.setStyle(cell.getStyle() + borderStyle.toString());

            if (gridData[i][j] != 0) {
                cell.setText(String.valueOf((int) gridData[i][j]));
                cell.setEditable(false);
                cell.setStyle(cell.getStyle() + "-fx-background-color: lightgray;");
            }

            // Add listener to handle user input
            final int row = i;
            final int col = j;
            cell.textProperty().addListener((obs, oldValue, newValue) -> {
                onUpdate(cell, row, col, newValue);
            });

            gridPane.add(cell, j, i);
            }
        }

        // set the scene
        App.setScene(gameScene);
    }

    // update cell value based on user input and do basic validation
    private void onUpdate(TextField cell, int row, int col, String newValue) {
        if (debug) {
            System.out.println("Cell updated at (" + row + ", " + col + "): " + newValue);
        }
        // handle user input in cell at (row, col)
        if (newValue.isEmpty()) {
            gridData[row][col] = 0; // clear cell
            cell.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            return;
        }
        try {
            int val = Integer.parseInt(newValue);
            if (val < 1 || val > 9) {
                System.out.println("Invalid input: " + newValue);
                cell.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
                return;
            } else {
                cell.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            }
            gridData[row][col] = val;
            // Optionally, add logic to check if the current grid state is valid
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + newValue);
        }
    }

    // generate a new Sudoku puzzle based on difficulty
    private void getPuzzle() {
        // generate a new Sudoku puzzle based on difficulty
        double adj_difficulty = difficulty * 20; // scale difficulty to 20, 40, 60
        int[][] puzzle = GeneratePuzzle.sudokuGenerator((int) adj_difficulty);
        gridData = new double[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                gridData[i][j] = puzzle[i][j];
            }
        }

        if (debug) {
            // print the generated puzzle for debugging
            System.out.println("Generated Sudoku Puzzle:");
            for (int[] row : puzzle) {
                for (int cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        }
    }
}
