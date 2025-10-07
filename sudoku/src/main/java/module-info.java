module com.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.sudoku to javafx.fxml;
    exports com.sudoku;
}
