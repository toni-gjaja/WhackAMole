package com.whackamole.wam20;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.*;

public class HelloController implements Initializable {

    @FXML
    TextField tfPlayerName;

    public static String playerName;

    public static Stage mainStage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainStage = HelloApplication.getMainStage();




    }

    public void checkValidity() throws IOException {

        if (tfPlayerName.getText() == "" || tfPlayerName.getText() == null){

            Border border = new Border(new BorderStroke(RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
            tfPlayerName.setBorder(border);

        }
        else {
            startGame();
        }

    }

    public void startGame() throws IOException {

        playerName = tfPlayerName.getText();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1320, 736);

        mainStage.setTitle("Play");

        mainStage.setScene(scene);

        mainStage.show();

    }

    public void howToPlay() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("howToPlay.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1320, 736);

        mainStage.setTitle("How to play");

        mainStage.setScene(scene);

        mainStage.show();

    }

    public void leaderboard() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("leaderboard.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1320, 736);

        mainStage.setTitle("Leaderboard");

        mainStage.setScene(scene);

        mainStage.show();
    }

    public void returnToMenu() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1320, 736);

        mainStage.setTitle("How to play");

        mainStage.setScene(scene);

        mainStage.show();
    }

    public static String getPlayerName(){

        return playerName;

    }


}