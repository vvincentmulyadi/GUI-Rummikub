package com.example.rummikubfrontscreen;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class gameboardC {
    @FXML
    private Pane node;

    @FXML
    private void initialize(){
        node.setOnMouseClicked(this::coButtonClick);
    }

    @FXML
    private void coButtonClick(MouseEvent e) {
        System.out.println("x: "+e.getSceneX()+" y: "+e.getSceneY());
    }
}
