package com.example.rummikubfrontscreen;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class gameboardC {
    @FXML
    private Pane pane;

    /*@FXML
    private void initialize(){
        node.setOnMouseClicked(this::coButtonClick);
    }*/

    @FXML
    private void coButtonClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
      System.out.println("Mouse clicked at coordinates: X=" + x + ", Y=" + y);
    }
}
