package com.example.rummikubfrontscreen;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


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
        btn1.setCenterX(x);
        btn1.setCenterY(y);
      System.out.println("Mouse clicked at coordinates: X=" + x + ", Y=" + y);
    }
}
