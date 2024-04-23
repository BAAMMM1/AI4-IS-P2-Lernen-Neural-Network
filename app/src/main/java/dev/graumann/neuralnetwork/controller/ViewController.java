package dev.graumann.neuralnetwork.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import dev.graumann.neuralnetwork.model.snake.Game;

import java.util.*;

public class ViewController implements Observer {

    private Game game;
    private double gridFieldWidth = 40;

    private Thread gameThread;

    private int fieldColumns = 44;
    private int fieldRows = 25;

    private boolean firstInit = true;

    private Rectangle[][] rectangles;

    private List<Tuple<Integer, Integer>> drawsBefore;

    @FXML
    AnchorPane anchorPane;

    @FXML
    AnchorPane anchorPane2;

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
    Slider runsSlider;

    @FXML
    Button start;

    @FXML
    Button reset;

    @FXML
    Label labelRunCounter;

    @FXML
    Label labelColumns;

    @FXML
    Label labelRows;

    @FXML
    ChoiceBox<String> choiceBoxPlayerTyps;

    @FXML
    Slider columnSlider;

    @FXML
    Slider rowSlider;


    @FXML
    public void initialize() {

        game = new Game(fieldColumns, fieldRows);
        game.addObserver(this);
        game.stop();

        drawsBefore = new ArrayList<>();
        initBlankGridPane();
        initSpeedSlider();
        initRunsSlider();
        initCoiceBoxPlayerTyps();

        initGridPaneResizer();
        initColumnSlider();
        initRowSlider();



        System.out.println(gridPane.getHeight());
        System.out.println((((gridPane.getHeight() / fieldRows)) * fieldColumns) * 2);


    }

    @Override
    public void update(Observable o, Object arg) {


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (firstInit) {
                    initBlankGridPane();
                    firstInit = false;
                }

                //blankAllCells();
                blankDrawsBeforeSnakeAndPickup(drawsBefore);
                drawsBefore.clear();


                //initBlankGridPane(); // TODO Nicht jedes mal das ganze Feld clearen, nur die Schlange clearen

                for (int i = 0; i < game.getSnake().getTails().size(); i++) {
                    /*
                    Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#000000");
                    rectangle.setStroke(Paint.valueOf("grey"));
                    rectangle.setStrokeWidth(0.5);
                    gridPane.add(rectangle, gameAI.getSnake().getTails().get(i).getX(), gameAI.getSnake().getTails().get(i).getY());
                    */
                    drawsBefore.add(new Tuple(game.getSnake().getTails().get(i).getX(), game.getSnake().getTails().get(i).getY()));
                    rectangles[game.getSnake().getTails().get(i).getX()][game.getSnake().getTails().get(i).getY()].setFill(Color.web("#000000"));
                }

                /*
                Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#5B5B5B");
                rectangle.setStroke(Paint.valueOf("grey"));
                rectangle.setStrokeWidth(0.5);
                gridPane.add(rectangle, gameAI.getSnake().getHead().getX(), gameAI.getSnake().getHead().getY());
                */


                drawsBefore.add(new Tuple(game.getSnake().getHead().getX(), game.getSnake().getHead().getY()));
                rectangles[game.getSnake().getHead().getX()][game.getSnake().getHead().getY()].setFill(Color.web("#5B5B5B"));

                /*
                rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#E8436E");
                rectangle.setStroke(Paint.valueOf("grey"));
                rectangle.setStrokeWidth(0.5);
                gridPane.add(rectangle, gameAI.getField().getPickUp().getX(), gameAI.getField().getPickUp().getY());
                 */

                drawsBefore.add(new Tuple(game.getField().getPickUp().getX(), game.getField().getPickUp().getY()));
                rectangles[game.getField().getPickUp().getX()][game.getField().getPickUp().getY()].setFill(Color.web("#E8436E"));

                drawScroe();

            }
        });


        System.out.println(game.getScore());
    }

    public void input(KeyEvent e) {

        game.getPlayer().inputKey(e.getCode());

    }

    public void drawScroe() {
        score.setText("" + game.getScore().getScore());
        bestScore.setText("" + game.getScore().getBestScore());
        average.setText("" + game.getScore().getAverageScore());
        labelRunCounter.setText(game.getPlayer().getRunCounter() + " / " + game.getRuns());
    }

    private void initBlankGridPane() {
        drawsBefore.clear();
        this.rectangles = new Rectangle[game.getField().getColumns()][game.getField().getRows()];
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();
        for (int a = 0; a < game.getField().getColumns(); a++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints());
        }

        for (int a = 0; a < game.getField().getRows(); a++) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }

        for (int column = 0; column < game.getField().getColumns(); column++) {
            for (int row = 0; row < game.getField().getRows(); row++) {
                Rectangle rectangle = getBlankCell(column, row);
                this.rectangles[column][row] = rectangle;
                gridPane.add(rectangle, column, row);
            }
        }


    }

    public void blankAllCells() {

        for (int columns = 0; columns < rectangles.length; columns++) {

            for (int rows = 0; rows < rectangles[columns].length; rows++) {
                rectangles[columns][rows].setFill(Color.web("#FFFFFF"));
            }

        }

    }

    public void blankDrawsBeforeSnakeAndPickup(List<Tuple<Integer, Integer>> before) {

        for (Tuple<Integer, Integer> tuple : before) {
            rectangles[tuple.x][tuple.y].setFill(Color.web("#FFFFFF"));

        }

    }

    public class Tuple<X, Y> {
        public final X x;
        public final Y y;

        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    private void cleargridSnake() {


    }

    private Rectangle getBlankCell(int column, int row) {

        Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#FFFFFF");
        rectangle.setStroke(Paint.valueOf("#D1D1D1"));
        rectangle.setStrokeWidth(0.5);

        return rectangle;
    }

    private Rectangle drawRectangle(double width, double height, String colorstring) {

        if (((anchorPane2.getHeight() / game.getField().getRows()) - 1.0) * fieldColumns < anchorPane2.getWidth()) {
            width = (gridPane.getHeight() / game.getField().getRows()) - 1.0;
            height = (gridPane.getHeight() / game.getField().getRows()) - 1.0;
        } else {
            width = (gridPane.getWidth() / game.getField().getColumns()) - 1.5;
            height = (gridPane.getWidth() / game.getField().getColumns()) - 1.5;
        }


        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.web(colorstring));
        GridPane.setHalignment(rectangle, HPos.CENTER);

        return rectangle;
    }

    public void reSizeRectangles(double gridPaneWidth, double gridPaneHeight) {
        System.out.println("resize with: " + gridPaneWidth + " " + gridPaneHeight);

        double width, height;

        if (((gridPaneHeight / game.getField().getRows())) * fieldColumns < gridPaneWidth) {
            width = (gridPaneHeight / game.getField().getRows()) - 1.0;
            height = (gridPaneHeight / game.getField().getRows()) - 1.0;
        } else {
            width = (gridPaneWidth / game.getField().getColumns()) - 1.5;
            height = (gridPaneWidth / game.getField().getColumns()) - 1.5;
        }

        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[i].length; j++) {
                rectangles[i][j].setWidth(width);
                rectangles[i][j].setHeight(height);

            }

        }
    }

    @FXML
    public void initSpeedSlider() {
        speedSlider.setValue(speedSlider.getMax() - game.getClock().getTime() + speedSlider.getMin());

        speedSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            game.getClock().setTime((int) (speedSlider.getMax() - new_val.intValue() + speedSlider.getMin()));
        });
    }

    private void initRunsSlider() {
        runsSlider.setValue(game.getRuns());
        labelRunCounter.setText(game.getPlayer().getRunCounter() + " / " + game.getRuns());

        runsSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            game.setRuns(new_val.intValue());
            labelRunCounter.setText(game.getPlayer().getRunCounter() + " / " + game.getRuns());
        });
    }

    private void initColumnSlider() {
        columnSlider.setValue(fieldColumns);
        labelColumns.setText("" + game.getField().getColumns());
        columnSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            game.getField().setColumns(new_val.intValue());
            fieldColumns = new_val.intValue();
            labelColumns.setText("" + new_val.intValue());
            buttonReset();
            initBlankGridPane();

            update(null, null);

        });

    }

    private void initRowSlider() {
        rowSlider.setValue(fieldRows);
        labelRows.setText("" + game.getField().getRows());
        rowSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            game.getField().setRows(new_val.intValue());
            fieldRows = new_val.intValue();
            labelRows.setText("" + new_val.intValue());
            buttonReset();
            initBlankGridPane();

            update(null, null);

        });
    }

    @FXML
    public void buttonStart() {

        if (gameThread != null) {
            this.game.stop();
        }

        this.gameThread = new Thread(() -> {
            game.play();
        });

        this.gameThread.start();


    }

    @FXML
    public void buttonReset() {
        this.game.stop();
    }

    public void initCoiceBoxPlayerTyps() {
        choiceBoxPlayerTyps.setItems(FXCollections.observableArrayList(game.getPlayerTypes().keySet()));
        // remove the humand player
        choiceBoxPlayerTyps.getItems().remove(0);
        choiceBoxPlayerTyps.setValue(choiceBoxPlayerTyps.getItems().get(0));

    }

    @FXML
    public void choiceBoxPlayerTypsOnAction() {
        buttonReset();
        game.setPlayer(choiceBoxPlayerTyps.getValue().toString());

    }

    private void initGridPaneResizer() {

        gridPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            reSizeRectangles(newVal.doubleValue(), gridPane.getHeight());
            update(null, null);
        });

        gridPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            reSizeRectangles(gridPane.getWidth(), newVal.doubleValue());
            update(null, null);
        });


    }

}
