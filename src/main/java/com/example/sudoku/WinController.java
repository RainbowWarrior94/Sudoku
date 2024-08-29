package com.example.sudoku;

import javafx.fxml.FXML;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WinController {

    @FXML
    private Text winText;

    public void initialize() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/AAsianHiro-vmv3L.ttf"), 50);
        winText.setFont(font);
    }

}