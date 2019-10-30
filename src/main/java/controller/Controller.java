package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import model.snake.Game;

import java.util.Observable;
import java.util.Observer;


public class Controller implements Observer {

    Game game;

    @FXML
    TextArea textArea;


    @FXML
    public void initialize(){
        game = new Game(15);
        game.addObserver(this);

        textArea.setText(game.visualization());

        new Thread(() -> {
            game.run();
        }).start();


    }


    public void input(KeyEvent e) {

        game.input(e.getCode());

    }

    @Override
    public void update(Observable o, Object arg) {
        textArea.setText(game.visualization());
    }


}
