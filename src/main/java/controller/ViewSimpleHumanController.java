package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import model.snake.GameHuman;

import java.util.Observable;
import java.util.Observer;


public class ViewSimpleHumanController implements Observer {

    GameHuman gameHuman;

    @FXML
    TextArea textArea;


    @FXML
    public void initialize(){
        gameHuman = new GameHuman(15);
        gameHuman.addObserver(this);

        textArea.setText(gameHuman.visualization());

        new Thread(() -> {
            gameHuman.run();
        }).start();


    }


    public void input(KeyEvent e) {

        gameHuman.input(e.getCode());

    }

    @Override
    public void update(Observable o, Object arg) {
        textArea.setText(gameHuman.visualization());
    }


}