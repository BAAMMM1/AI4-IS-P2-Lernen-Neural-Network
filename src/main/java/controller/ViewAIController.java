package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.snake.GameAI;

import java.util.Observable;
import java.util.Observer;

public class ViewAIController implements Observer {

    private GameAI gameAI;
    private int gridColumns;
    private double gridFieldWidth = 40;

    private Thread gameThread;

    @FXML
    GridPane gridPane;

    @FXML
    Label score;

    @FXML
    Label bestScore;

    @FXML
    Label average;

    @FXML
    Slider speedSlider;

    @FXML
    Button start;

    @FXML
    Button reset;

    @FXML
    Label labelRunCounter;

    @FXML
    TextField textFieldRuns;


    @FXML
    public void initialize() {

        gameAI = new GameAI(18);
        gameAI.addObserver(this);

        gridColumns = gameAI.getField().getColumns();
        cleargrid();

        gameAI.stop();

        initSpeedSlider();
        initTextFieldRuns();



    }


    @Override
    public void update(Observable o, Object arg) {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                cleargrid(); // TODO Nicht jedes mal das ganze Feld clearen, nur die Schlange clearen

                for (int i = 0; i < gameAI.getSnake().getTails().size(); i++) {
                    Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#000000");
                    rectangle.setStroke(Paint.valueOf("grey"));
                    rectangle.setStrokeWidth(0.5);
                    gridPane.add(rectangle, gameAI.getSnake().getTails().get(i).getX(), gameAI.getSnake().getTails().get(i).getY());
                }

                Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#5B5B5B");
                rectangle.setStroke(Paint.valueOf("grey"));
                rectangle.setStrokeWidth(0.5);
                gridPane.add(rectangle, gameAI.getSnake().getHead().getX(), gameAI.getSnake().getHead().getY());

                rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#E8436E");
                rectangle.setStroke(Paint.valueOf("grey"));
                rectangle.setStrokeWidth(0.5);
                gridPane.add(rectangle, gameAI.getField().getPickUp().getX(), gameAI.getField().getPickUp().getY());

                drawScroe();

            }
        });


        System.out.println(gameAI.getScore());
    }

    public void input(KeyEvent e) {

        gameAI.getPlayer().inputKey(e.getCode());

    }

    public void drawScroe() {
        score.setText("Score: " + gameAI.getScore().getScore());
        bestScore.setText("Best Score: " + gameAI.getScore().getBestScore());
        average.setText("Average Score: " + gameAI.getScore().getAverageScore());
        labelRunCounter.setText("runs: " + gameAI.getPlayer().getRunCounter() + " / ");
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
        Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#FFFFFF");
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

    @FXML
    public void initSpeedSlider(){
        speedSlider.valueProperty().addListener((ov, old_val, new_val) -> gameAI.getClock().setTime(new_val.intValue()));
    }

    @FXML
    public void buttonStart(){

        if(gameThread != null){
            this.gameThread.stop();
        }

        this.gameThread = new Thread(() -> {
            gameAI.play();
        });

        this.gameThread.start();

    }

    @FXML
    public void buttonReset(){
        if(gameThread != null){
            this.gameThread.stop();
            this.gameAI.stop();
        }
    }

    public void initTextFieldRuns(){
        textFieldRuns.setText(gameAI.getRuns() + "");

        textFieldRuns.setOnKeyReleased(event -> {
            try{
                int value = Integer.parseInt(textFieldRuns.getText());
                gameAI.setRuns(value);
            } catch (NumberFormatException e){
                System.out.println(e.getStackTrace());
            }

            });
    }

}
