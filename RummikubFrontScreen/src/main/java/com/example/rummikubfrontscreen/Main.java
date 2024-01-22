package com.example.rummikubfrontscreen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        try {

            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene1 = new Scene(root);
            stage.setScene(scene1);
             stage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

