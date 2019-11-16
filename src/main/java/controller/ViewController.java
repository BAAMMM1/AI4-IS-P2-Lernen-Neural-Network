package controller;

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
import javafx.stage.Stage;
import model.snake.GameAI;

import java.util.Observable;
import java.util.Observer;

public class ViewController implements Observer {

    private GameAI gameAI;
    private double gridFieldWidth = 40;

    private Thread gameThread;

    private int fieldColumns = 24;
    private int fieldRows = 14;

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

        gameAI = new GameAI(fieldColumns, fieldRows);
        gameAI.addObserver(this);

        cleargrid();

        gameAI.stop();

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
        score.setText("" + gameAI.getScore().getScore());
        bestScore.setText("" + gameAI.getScore().getBestScore());
        average.setText("" + gameAI.getScore().getAverageScore());
        labelRunCounter.setText(gameAI.getPlayer().getRunCounter() + " / " + gameAI.getRuns());
    }

    private void cleargrid() {
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();
        for (int a = 0; a < gameAI.getField().getColumns(); a++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints());
        }

        for (int a = 0; a < gameAI.getField().getRows(); a++) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }

        for (int i = 0; i < gameAI.getField().getColumns(); i++) {
            for (int j = 0; j < gameAI.getField().getRows(); j++) {
                blankCell(i, j);
            }
        }


    }

    private void cleargridSnake() {


    }

    private void blankCell(int column, int row) {
        Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#FFFFFF");
        rectangle.setStroke(Paint.valueOf("#D1D1D1"));
        rectangle.setStrokeWidth(0.5);
        gridPane.add(rectangle, column, row);
    }

    private Rectangle drawRectangle(double width, double height, String colorstring) {

        if (((anchorPane2.getHeight() / gameAI.getField().getRows()) - 1.0) * fieldColumns < anchorPane2.getWidth()) {
            width = (gridPane.getHeight() / gameAI.getField().getRows()) - 1.0;
            height = (gridPane.getHeight() / gameAI.getField().getRows()) - 1.0;
        } else {
            width = (gridPane.getWidth() / gameAI.getField().getColumns()) - 1.5;
            height = (gridPane.getWidth() / gameAI.getField().getColumns()) - 1.5;
        }


        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.web(colorstring));
        GridPane.setHalignment(rectangle, HPos.CENTER);

        return rectangle;
    }

    @FXML
    public void initSpeedSlider() {
        speedSlider.setValue(speedSlider.getMax() - gameAI.getClock().getTime() + speedSlider.getMin());

        speedSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            gameAI.getClock().setTime((int) (speedSlider.getMax() - new_val.intValue() + speedSlider.getMin()));
        });
    }

    private void initRunsSlider() {
        runsSlider.setValue(gameAI.getRuns());
        labelRunCounter.setText(gameAI.getPlayer().getRunCounter() + " / " + gameAI.getRuns());

        runsSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            gameAI.setRuns(new_val.intValue());
            labelRunCounter.setText(gameAI.getPlayer().getRunCounter() + " / " + gameAI.getRuns());
        });
    }

    private void initColumnSlider() {
        columnSlider.setValue(fieldColumns);
        labelColumns.setText("" + gameAI.getField().getColumns());
        columnSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            gameAI.getField().setColumns(new_val.intValue());
            fieldColumns = new_val.intValue();
            labelColumns.setText("" + new_val.intValue());
            update(null, null);
            buttonStart();
            buttonReset();
        });

    }

    private void initRowSlider() {
        rowSlider.setValue(fieldRows);
        labelRows.setText("" + gameAI.getField().getRows());
        rowSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            gameAI.getField().setRows(new_val.intValue());
            fieldRows = new_val.intValue();
            labelRows.setText("" + new_val.intValue());
            update(null, null);
            buttonStart();
            buttonReset();
        });
    }

    @FXML
    public void buttonStart() {

        if (gameThread != null) {
            this.gameThread.stop();
        }

        this.gameThread = new Thread(() -> {
            gameAI.play();
        });

        this.gameThread.start();
        cleargrid();

    }

    @FXML
    public void buttonReset() {
        if (gameThread != null) {
            this.gameThread.stop();
            this.gameAI.stop();
        }
    }

    public void initCoiceBoxPlayerTyps() {
        choiceBoxPlayerTyps.setItems(FXCollections.observableArrayList(gameAI.getPlayerTypes().keySet()));
        choiceBoxPlayerTyps.setValue(choiceBoxPlayerTyps.getItems().get(0));

    }

    @FXML
    public void choiceBoxPlayerTypsOnAction() {
        buttonReset();
        gameAI.setPlayer(choiceBoxPlayerTyps.getValue().toString());

    }

    private void initGridPaneResizer() {

        anchorPane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        ((Stage) newWindow).heightProperty().addListener((a, b, c) -> {
                            if (c != b) {
                                update(null, null);
                                System.out.println(newWindow.getHeight());
                            }
                        });
                    }
                });
            }
        });

        anchorPane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        ((Stage) newWindow).widthProperty().addListener((a, b, c) -> {
                            if (c != b) {
                                update(null, null);
                                System.out.println(newWindow.getWidth());
                            }
                        });
                    }
                });
            }
        });
    }

}
