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
    private int gridColumns;
    private int girdRows;
    private double gridFieldWidth = 40;

    private Thread gameThread;

    private int fieldColumns = 24;
    private int fieldRows = 14;
    private double lastWidth = 0.0;

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
    Button start;

    @FXML
    Button reset;

    @FXML
    Label labelRunCounter;

    @FXML
    TextField textFieldRuns;

    @FXML
    ChoiceBox<String> choiceBoxPlayerTyps;


    @FXML
    public void initialize() {

        gameAI = new GameAI(fieldColumns, fieldRows);
        gameAI.addObserver(this);


        gridColumns = gameAI.getField().getColumns();
        girdRows = gameAI.getField().getRows();
        cleargrid();

        gameAI.stop();

        initSpeedSlider();
        initTextFieldRuns();
        initCoiceBoxPlayerTyps();
        initGridPaneResizer();

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
            gridPane.getColumnConstraints().add(new ColumnConstraints());
        }

        for (int a = 0; a < girdRows; a++) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }

        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < girdRows; j++) {
                blankCell(i, j);
            }
        }


    }

    private void cleargridSnake() {



    }

    private void blankCell(int column, int row) {
        Rectangle rectangle = drawRectangle(gridFieldWidth, gridFieldWidth, "#FFFFFF");
        rectangle.setStroke(Paint.valueOf("grey"));
        rectangle.setStrokeWidth(0.5);
        gridPane.add(rectangle, column, row);
    }

    private Rectangle drawRectangle(double width, double height, String colorstring) {

        if(((anchorPane2.getHeight() / girdRows) - 1.0) * fieldColumns < anchorPane2.getWidth()){
            width = (gridPane.getHeight() / girdRows) - 1.0;
            height = (gridPane.getHeight() / girdRows) - 1.0 ;
        } else {
            width = (gridPane.getWidth() / gridColumns) - 1.5;
            height = (gridPane.getWidth() / gridColumns) - 1.5;
        }


        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.web(colorstring));
        GridPane.setHalignment(rectangle, HPos.CENTER);

        lastWidth = width * fieldColumns;

        return rectangle;
    }

    @FXML
    public void initSpeedSlider() {
        speedSlider.valueProperty().addListener((ov, old_val, new_val) -> gameAI.getClock().setTime(new_val.intValue()));
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

    public void initTextFieldRuns() {
        textFieldRuns.setText(gameAI.getRuns() + "");

        textFieldRuns.setOnKeyReleased(event -> {
            try {
                int value = Integer.parseInt(textFieldRuns.getText());
                gameAI.setRuns(value);
            } catch (NumberFormatException e) {
                System.out.println(e.getStackTrace());
            }

        });
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
