package com.example.rummikubfrontscreen;

import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BoardController {
    @FXML
    private Button draw;
    private final String[] randomValues = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Label label;

    public BoardController() {
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void addRandomValue() {
        Button[] var1 = new Button[]{this.button1, this.button2, this.button3, this.button4};
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Button button = var1[var3];
            if (button.getText().isEmpty()) {
                int randomIndex = (new Random()).nextInt(this.randomValues.length);
                String randomValue = this.randomValues[randomIndex];
                button.setText(randomValue);
                break;
            }
        }

    }

    /*@FXML
    private void handleButtonClick(ActionEvent e) {
        GameBoardController gc = new GameBoardController();
        Button clickedButton = (Button)e.getSource();
        gc.setButton(clickedButton);
        String buttonValue = clickedButton.getText();
        if (buttonValue.isEmpty()) {
            clickedButton.setText(this.label.getText());
            this.label.setText("");
        } else {
            this.label.setText(buttonValue);
            clickedButton.setText("");
        }
     }
     */
}

