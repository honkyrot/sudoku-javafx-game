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
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.layout.*;

import java.nio.Buffer;

import com.sudoku.GeneratePuzzle;

// handles game logic and user interactions

public class GameController {
    Scene gameScene;
    double grid_data[][];
    int grid_size = 9;

    // DEBUG
    boolean debug = true;

    double difficulty = 2; // default medium
    boolean game_assist = true;

    boolean game_over = false;

    public GameController() {
        // initialize game state
    }

    // MenuController button calls this to start a new game
    public void startNewGame(double difficulty, boolean game_assist) {
        this.difficulty = difficulty;
        this.game_assist = game_assist;

        // start a new game
        getPuzzle();

        // create a new game scene
        GridPane grid_pane = new GridPane();
        gameScene = new Scene(grid_pane, 600, 600);
        grid_pane.setGridLinesVisible(true);
        
        grid_pane.setAlignment(Pos.CENTER);
        grid_pane.setHgap(5);

        // populate the grid with the puzzle data and visually separate 3x3 boxes
        for (int i = 0; i < grid_size; i++) {
            for (int j = 0; j < grid_size; j++) {
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

            if (grid_data[i][j] != 0) {
                cell.setText(String.valueOf((int) grid_data[i][j]));
                cell.setEditable(false);
                cell.setStyle(cell.getStyle() + "-fx-background-color: lightgray;");
            }

            // Add listener to handle user input
            final int row = i;
            final int col = j;
            cell.textProperty().addListener((obs, old_value, new_value) -> {
                onUpdate(cell, row, col, new_value);
            });

            grid_pane.add(cell, j, i);
            }
        }

        // set the scene
        App.setScene(gameScene);
    }

    // update cell value when user inputs a number
    private void onUpdate(TextField cell, int row, int col, String new_value) {

        if (debug) {
            System.out.println("Cell updated at (" + row + ", " + col + "): " + new_value);
        }

        // handle user input in cell at (row, col)
        if (new_value.isEmpty()) {
            grid_data[row][col] = 0; // clear cell
            editCellColor(cell, Color.WHITE);
            return;
        }
        try {
            int val = Integer.parseInt(new_value);
            // numbers must be clamped between 1-9
            if (val < 1 || val > 9) {
                System.out.println("Invalid input: " + new_value);
                editCellColor(cell, Color.PINK);
                return;
            } else {
                editCellColor(cell, Color.WHITE);
            }
            grid_data[row][col] = val;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + new_value);
        }

        // compare the current grid state for user assistance
        if (game_assist) {
            boolean valid_state = compareCells(cell, row, col, new_value);
            if (!valid_state) {
                editCellColor(cell, Color.PINK);
            } else {
                editCellColor(cell, Color.WHITE);
            }
        }

        // check grid
        checkGrid(cell, row, col);
    }

    // compare the current grid with rows and columns for duplicates
    private boolean compareCells(TextField cell, int row, int col, String newValue) {
        if (newValue.isEmpty()) {
            return true; // empty cells are always valid
        }

        int val;
        try {
            val = Integer.parseInt(newValue);
        } catch (NumberFormatException e) {
            return false; // invalid number
        }

        // check row
        for (int j = 0; j < 9; j++) {
            if (j != col && grid_data[row][j] == val) {
                return false;
            }
        }

        // check column
        for (int i = 0; i < 9; i++) {
            if (i != row && grid_data[i][col] == val) {
                return false;
            }
        }

        // check 3x3 box
        int box_row = (row / 3) * 3;
        int box_col = (col / 3) * 3;
        for (int i = box_row; i < box_row + 3; i++) {
            for (int j = box_col; j < box_col + 3; j++) {
                if (i != row && j != col && grid_data[i][j] == val) {
                    return false;
                }
            }
        }

        return true;
    }

    // check if the current grid if the user has solved the puzzle
    private void checkGrid(TextField cell, int row, int col) {
        boolean isComplete = true;

        // check for empty cells
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid_data[i][j] == 0) {
                    isComplete = false;
                    break;
                }
            }
            if (!isComplete) break;
        }

        // check for duplicates in rows, columns, and boxes
        boolean valid = true;
        valid = compareCells(cell, row, col, cell.getText());

        // if conditions are met, the puzzle is solved!!!!
        if (isComplete && valid) {
            win();
        }
    }

    // win condition
    private void win() {
        if (!game_over) {  // fire only once
            game_over = true;
            System.out.println("Congratulations! You solved the puzzle!");

            // add text label under the grid
            Text winText = new Text("Congratulations! You solved the puzzle!");
            winText.setFill(Color.GREEN);
            winText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            // spawn it under the grid
            gameScene.setRoot(new VBox(((GridPane) gameScene.getRoot()), winText));

            // menu button to return to main menu
            Button menuButton = new Button("Return to Main Menu");
            menuButton.setOnAction(e -> {
                App.setScene(MenuController.menuScene);
            });

            ((VBox) gameScene.getRoot()).getChildren().add(menuButton);
        }
    }

    // cell color changer
    private void editCellColor(TextField cell, Color color) {
        cell.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    // generate a new Sudoku puzzle based on difficulty
    private void getPuzzle() {
        double adj_difficulty = difficulty * 20; // scale difficulty to 20, 40, 60
        int[][] puzzle = GeneratePuzzle.sudokuGenerator((int) adj_difficulty);
        grid_data = new double[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid_data[i][j] = puzzle[i][j];
            }
        }

        // print the generated puzzle for debugging
        if (debug) {
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
