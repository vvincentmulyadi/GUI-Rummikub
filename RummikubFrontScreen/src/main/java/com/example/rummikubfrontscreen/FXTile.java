package com.example.rummikubfrontscreen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class FXTile implements EventHandler<MouseEvent> {

    Button fxTile = new Button();

    // Number and first letter of colour e.g. 4B -- is 4 black
    private String fxTileID;



   /* public FXTile(double x, double y, String fxTileID){
        fxTile.setLayoutX(x);
        fxTile.setLayoutY(y);
        //fxTile.setTileID(fxTileID);
    }*/

    public void setFXTileID(String tileID){
        this.fxTileID = fxTileID;
    }
    public String getTileID(){
        return fxTileID;
    }

    public void handle(MouseEvent event){
        fxTile.setLayoutX(event.getSceneX());
        fxTile.setLayoutY(event.getSceneY());
    }

}
