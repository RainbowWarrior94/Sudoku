package com.example.sudoku;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ModalWindow {

    public static void newWindow(String title) {
        try {
            FXMLLoader loader = new FXMLLoader(ModalWindow.class.getResource("win-view.fxml"));
            Parent root = loader.load();

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setTitle(title);

            window.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
