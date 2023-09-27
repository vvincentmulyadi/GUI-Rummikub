package com.example.rummikubfrontscreen;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;


public class GameBoardController {
    @FXML
    private Pane pane;
    @FXML
    private Button button;
    /*@FXML
    private void initialize(){
        node.setOnMouseClicked(this::coButtonClick);
    }*/
    /*@FXML
    private void tilesButtonClick(ActionEvent e) {
        Button clickedButton = (Button)e.getSource();
        String clickedButton = click
        if (buttonValue.isEmpty()) {
            clickedButton.setText(this.label.getText());
            this.label.setText("");
        } else {
            this.label.setText(buttonValue);
            clickedButton.setText("");
        }

    }*/

    public void setButton(Button button){
        this.button = button;
    }

    public Button getButton(){
        return button;
    }

    @FXML
    private void coButtonClick(MouseEvent event) {
        double x = event.getSceneX();
        double y = event.getSceneY();
        button.setLayoutX(x);
        button.setLayoutY(y);
        System.out.println("Mouse clicked at coordinates: X=" + x + ", Y=" + y);
    }
}
