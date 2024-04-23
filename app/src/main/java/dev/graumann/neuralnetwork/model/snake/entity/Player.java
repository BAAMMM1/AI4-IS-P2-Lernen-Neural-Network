package dev.graumann.neuralnetwork.model.snake.entity;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    protected Field field;
    protected Snake snake;

    protected double[] lastInput;
    protected double[] lastOutput;

    private List<double[]> lastInputHistory;
    private List<double[]> lastOutputHistroy;

    private int runCounter = 0;

    protected KeyCode code;

    public Player(Field field, Snake snake) {
        this.field = field;
        this.snake = snake;
        this.lastInputHistory = new ArrayList<>();
        this.lastOutputHistroy = new ArrayList<>();
    }

    public void nextRun(){
        runCounter++;
        this.lastInputHistory.add(lastInput.clone());
        this.lastOutputHistroy.add(lastOutput.clone());
    }

    public List<double[]> getLastInputHistory() {
        return lastInputHistory;
    }

    public List<double[]> getLastOutputHistroy() {
        return lastOutputHistroy;
    }

    public void restart(){
        runCounter = 0;
        this.lastInputHistory = new ArrayList<>();
        this.lastOutputHistroy = new ArrayList<>();
    }

    public int getRunCounter() {
        return runCounter;
    }

    public abstract MoveDirection getMove();

    public void inputKey(KeyCode code) {
        this.code = code;
    }
}
