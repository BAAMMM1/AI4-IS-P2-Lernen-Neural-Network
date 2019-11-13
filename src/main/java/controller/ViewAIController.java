package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.snake.GameAI;
import model.snake.GameHuman;

import java.util.Observable;
import java.util.Observer;

public class ViewAIController implements Observer {

    private GameAI gameAI;
    private int gridColumns;
    private double gridFieldSize = 40;

    @FXML
    GridPane gridPane;

    @FXML
    Label score;

    @FXML
    Label bestScore;


    @FXML
    public void initialize() {
        gameAI = new GameAI(18);
        gameAI.addObserver(this);

        gridColumns = gameAI.getField().getColumns();
        cleargrid();

        new Thread(() -> {
            gameAI.run();
        }).start();

    }

    public void input(KeyEvent e) {

        gameAI.input(e.getCode());

    }

    @Override
    public void update(Observable o, Object arg) {
        //Semaphore semaphore = new Semaphore(0);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                cleargrid(); // TODO Nicht jedes mal das ganze Feld clearen, nur die Schlange clearen

                for (int i = 0; i < gameAI.getSnake().getTails().size(); i++) {
                    Rectangle rectangle = drawRectangle(gridFieldSize, gridFieldSize, "#000000");
                    rectangle.setStroke(Paint.valueOf("grey"));
                    rectangle.setStrokeWidth(0.5);
                    gridPane.add(rectangle, gameAI.getSnake().getTails().get(i).getX(), gameAI.getSnake().getTails().get(i).getY());
                }

                Rectangle rectangle = drawRectangle(gridFieldSize, gridFieldSize, "#5B5B5B");
                rectangle.setStroke(Paint.valueOf("grey"));
                rectangle.setStrokeWidth(0.5);
                gridPane.add(rectangle, gameAI.getSnake().getHead().getX(), gameAI.getSnake().getHead().getY());

                rectangle = drawRectangle(gridFieldSize, gridFieldSize, "#E8436E");
                rectangle.setStroke(Paint.valueOf("grey"));
                rectangle.setStrokeWidth(0.5);
                gridPane.add(rectangle, gameAI.getField().getPickUp().getX(), gameAI.getField().getPickUp().getY());

                drawScroe();

            }
        });

        //semaphore.release();
        //semaphore.acquire();
        //System.out.println(game.visualization());        ;
        System.out.println(gameAI.getScore());


    }

    public void drawScroe(){
        score.setText("Score: " + gameAI.getScore().getScore());
        bestScore.setText("Best Score: " + gameAI.getScore().getBestScore());
    }

    private void cleargrid() {
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();
        for (int a = 0; a < gridColumns; a++) {
            gridPane.getRowConstraints().add(new RowConstraints());
            gridPane.getColumnConstraints().add(new ColumnConstraints());
        }
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridColumns; j++) {
                blankCell(i, j);
            }
        }

    }

    private void blankCell(int column, int row) {
        Rectangle rectangle = drawRectangle(gridFieldSize, gridFieldSize, "#FFFFFF");
        rectangle.setStroke(Paint.valueOf("grey"));
        rectangle.setStrokeWidth(0.5);
        gridPane.add(rectangle, column, row);
    }

    private Rectangle drawRectangle(double width, double height, String colorstring) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.web(colorstring));
        GridPane.setHalignment(rectangle, HPos.CENTER);
        return rectangle;
    }

}
