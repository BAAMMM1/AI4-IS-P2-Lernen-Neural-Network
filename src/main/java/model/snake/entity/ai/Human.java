package model.snake.entity.ai;

import javafx.scene.input.KeyCode;
import model.io.IO;
import model.snake.entity.Field;
import model.snake.entity.MoveDirection;
import model.snake.entity.Player;
import model.snake.entity.Snake;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Human extends Player {

    int counter = 0;
    boolean gameRecord = false;

    private List<double[]> inputs = new ArrayList<>();
    private List<double[]> outputs = new ArrayList<>();


    public Human(Field field, Snake snake) {
        super(field, snake);
    }

    @Override
    public MoveDirection getMove() {
        MoveDirection result = MoveDirection.LEFT;

        if (code != null) {
            if (code.equals(KeyCode.UP)) result = MoveDirection.UP;
            else if (code.equals(KeyCode.DOWN)) result = MoveDirection.DOWN;
            else if (code.equals(KeyCode.LEFT)) result = MoveDirection.LEFT;
            else if (code.equals(KeyCode.RIGHT)) result = MoveDirection.RIGHT;

        }

        calcNeuronInputs();
        calcLastOutput(result);


        if(gameRecord) IO.save(new File("" + System.getProperty("user.dir") + "\\db\\game" + counter++).toURI(), inputs, outputs);


        return result;
    }

    public void calcLastOutput(MoveDirection humanDirection) {
        lastOutput = new double[1];

        if (snake.getDirection().equals(MoveDirection.UP)) {

            if (humanDirection.equals(MoveDirection.UP)) {
                lastOutput[0] = 0.5;

            } else if (humanDirection.equals(MoveDirection.LEFT)) {
                lastOutput[0] = 0.0;

            } else if (humanDirection.equals(MoveDirection.RIGHT)) {
                lastOutput[0] = 1.0;
            }


        } else if (snake.getDirection().equals(MoveDirection.DOWN)) {

            if (humanDirection.equals(MoveDirection.DOWN)) {
                lastOutput[0] = 0.5;

            } else if (humanDirection.equals(MoveDirection.LEFT)) {
                lastOutput[0] = 1.0;

            } else if (humanDirection.equals(MoveDirection.RIGHT)) {
                lastOutput[0] = 0.0;

            }

        } else if (snake.getDirection().equals(MoveDirection.LEFT)) {

            if (humanDirection.equals(MoveDirection.UP)) {
                lastOutput[0] = 1.0;

            } else if (humanDirection.equals(MoveDirection.DOWN)) {
                lastOutput[0] = 0.0;

            } else if (humanDirection.equals(MoveDirection.LEFT)) {
                lastOutput[0] = 0.5;

            }

        } else if (snake.getDirection().equals(MoveDirection.RIGHT)) {

            if (humanDirection.equals(MoveDirection.UP)) {
                lastOutput[0] = 0.0;

            } else if (humanDirection.equals(MoveDirection.DOWN)) {
                lastOutput[0] = 1.0;

            } else if (humanDirection.equals(MoveDirection.RIGHT)) {
                lastOutput[0] = 0.5;

            }

        }
        outputs.add(lastOutput.clone());


    }

    private void calcNeuronInputs() {
        // [obstacle_left?, obstacle_straight?, obstacle_right?, apple_left?, apple_straight?, apple_right?]
        double[] result = new double[6];

        System.out.println(snake.getHead().getX());
        System.out.println(snake.getHead().getY());
        System.out.println(snake.getDirection());

        if (snake.getDirection().equals(MoveDirection.UP)) {
            // links prüfen
            System.out.println("up");
            System.out.println(snake.getHead().getX());
            if (snake.getHead().getX() == 0) {
                result[0] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())) {
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if (snake.getHead().getY() == 0) {
                result[1] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)) {
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if (snake.getHead().getX() == field.getColumns() - 1) {
                result[2] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())) {
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if (snake.getHead().getX() > field.getPickUp().getX()) {
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if (snake.getHead().getY() > field.getPickUp().getY()) {
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if (snake.getHead().getX() < field.getPickUp().getX()) {
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

            System.out.println("result: " + Arrays.toString(result));


        } else if (snake.getDirection().equals(MoveDirection.DOWN)) {

            // links prüfen
            if (snake.getHead().getX() == field.getColumns() - 1) {
                result[0] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())) {
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if (snake.getHead().getY() == field.getColumns() - 1) {
                result[1] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)) {
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if (snake.getHead().getX() == 0) {
                result[2] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())) {
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }


            // apfel links?
            if (snake.getHead().getX() < field.getPickUp().getX()) {
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if (snake.getHead().getY() < field.getPickUp().getY()) {
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if (snake.getHead().getX() > field.getPickUp().getX()) {
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        } else if (snake.getDirection().equals(MoveDirection.LEFT)) {

            // links prüfen
            if (snake.getHead().getY() == field.getColumns() - 1) {
                result[0] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)) {
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if (snake.getHead().getX() == 0) {
                result[1] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())) {
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if (snake.getHead().getY() == 0) {
                result[2] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)) {
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if (snake.getHead().getY() < field.getPickUp().getY()) {
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if (snake.getHead().getX() > field.getPickUp().getX()) {
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if (snake.getHead().getY() > field.getPickUp().getY()) {
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        } else if (snake.getDirection().equals(MoveDirection.RIGHT)) {

            // links prüfen
            System.out.println(snake.getHead().getX());
            System.out.println(snake.getHead().getY());
            System.out.println(field.getColumns());
            if (snake.getHead().getY() == 0) {
                result[0] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)) {
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if (snake.getHead().getX() == field.getColumns() - 1) {
                result[1] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())) {
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if (snake.getHead().getY() == field.getColumns() - 1) {
                result[2] = 1.0;
            } else if (isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)) {
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if (snake.getHead().getY() > field.getPickUp().getY()) {
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if (snake.getHead().getX() < field.getPickUp().getX()) {
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if (snake.getHead().getY() < field.getPickUp().getY()) {
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        }

        this.lastInput = result.clone();
        inputs.add(result.clone());

    }

    public boolean isBodyCollison(int x, int y) {

        if (snake.getTails().size() > 2) {
            for (int i = 1; i < snake.getTails().size(); i++) {

                if (x == snake.getTails().get(i).getX() && y == snake.getTails().get(i).getY()) {
                    System.out.println("++++++++++++++++++++++ body collision");
                    return true;
                }
            }
        }
        return false;
    }

}
