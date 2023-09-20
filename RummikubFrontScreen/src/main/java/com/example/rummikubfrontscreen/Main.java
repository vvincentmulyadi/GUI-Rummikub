package com.example.rummikubfrontscreen;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        try {
        /*FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main .fxml"));
        Scene scene = new Scene(fxmlLoader.load());*/
        /*Group root = new Group();
        Scene scene = new Scene(root, 600, 500, Color.GRAY);
        Image icon = new Image("C:/Users/Lenovo/OneDrive/Desktop/png-clipart-rummy-rummikub-joker-tile-based-game-board-game-joker-game-face.png");
        stage.getIcons().add(icon);
        stage.setHeight(600);
        stage.setWidth(500);
        stage.setTitle("Rummikub");*/
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

