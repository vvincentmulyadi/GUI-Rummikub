package com.example.rummikubfrontscreen;

import com.example.rummikubfrontscreen.setup.GameApp;
import com.example.rummikubfrontscreen.setup.GameSetup;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;



    public void switchToMenuScene(ActionEvent event) throws IOException{
        initializeScene(event, "Main.fxml");
    }

    public void switchToChooseOpponentScene(ActionEvent event) throws IOException {
        initializeScene(event, "choose-opponent.fxml");
    }

    public void switchToBoardScene(ActionEvent event) throws IOException{
        initializeScene(event, "game-board-copy.fxml");




    }

    public void switchToMakeNewPlayersScene(ActionEvent event) throws IOException{
        initializeScene(event, "make-new-players.fxml");
    }



    private void initializeScene(ActionEvent event, String sceneName) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(sceneName));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
