package model.snake;


import javafx.scene.input.KeyCode;
import model.snake.clock.Clock;
import model.snake.entity.*;
import model.snake.entity.ai.AIPlayer;
import model.snake.event.BodyCollision;
import model.snake.event.Collision;
import model.snake.event.PickUpCollision;
import model.snake.event.WallColision;

import java.util.*;

public class GameAI extends Observable {


    private Clock clock;
    private Field  field;
    private Snake snake;
    private SnakeDirection inputDirection;
    private boolean run;
    private List<Collision> collisions;
    private Score score;

    private AIPlayer aiPlayer;
    private int counter = -1;

    List<double[]> inputs = new ArrayList<>();
    List<double[]> outputs = new ArrayList<>();
    List<Integer> scores = new ArrayList<>();

    public GameAI(int fieldColumns) {
        this.clock = new Clock();
        this.field = new Field(fieldColumns);
        this.snake = new Snake(field.getColumns());
        this.inputDirection = SnakeDirection.LEFT;
        this.score = new Score();
        this.collisions = new ArrayList<>();
        collisions.add(new PickUpCollision(field, snake, score));
        collisions.add(new WallColision(field, snake, score));
        collisions.add(new BodyCollision(field, snake, score));

        this.aiPlayer = new AIPlayer();

        this.run = true;
    }

    public void run(){
        int counter = 0;

        while(run){
            clock.tick();

            double[] input = calcSituation();

            snake.moveAI(aiPlayer.getMove(input));

            for(Collision collision: collisions){
                if(collision.isCollision()){
                    if(collision.getClass().equals(BodyCollision.class) || collision.getClass().equals(WallColision.class)){
                        counter++;
                        inputs.add(input);
                        outputs.add(aiPlayer.getBrain().calculate(input).clone());
                        scores.add(score.getScore());
                    }
                    collision.action();

                    if(counter == 100){
                        run = false;
                    }

                }
            }


            setChanged();
            notifyObservers();
        }



        for (int i = 0; i < inputs.size(); i++) {
            System.out.println(Arrays.toString(inputs.get(i)) + " " + Arrays.toString(outputs.get(i)) + " score: " + scores.get(i));
        }

        int sum = 0;
        for (int i = 0; i < scores.size(); i++) {
                    sum += scores.get(i);
        }
        System.out.println("Scores: " + sum/scores.size());

    }

    // TODO Richtige Inputs für die Neuronen berechnen
    private double[] calcSituation() {
        // [left?, straight?, right?]
        double[] result = new double[6];

        System.out.println(snake.getHead().getX());
        System.out.println(snake.getHead().getY());
        System.out.println(snake.getDirection());

        if(snake.getDirection().equals(SnakeDirection.UP)){
            // links prüfen
            System.out.println("up");
            System.out.println(snake.getHead().getX());
            if(snake.getHead().getX() == 0) {
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getY() == 0){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getX() == field.getColumns()-1){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if(snake.getHead().getX() > field.getPickUp().getX()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getY() > field.getPickUp().getY()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getX() < field.getPickUp().getX()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

            System.out.println("result: " + Arrays.toString(result));



        } else if(snake.getDirection().equals(SnakeDirection.DOWN)){

            // links prüfen
            if(snake.getHead().getX() == field.getColumns()-1){
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getY() == field.getColumns()-1){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getX() == 0){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }


            // apfel links?
            if(snake.getHead().getX() < field.getPickUp().getX()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getY() < field.getPickUp().getY()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getX() > field.getPickUp().getX()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        } else if(snake.getDirection().equals(SnakeDirection.LEFT)){

            // links prüfen
            if(snake.getHead().getY() == field.getColumns()-1){
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getX() == 0){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getY() == 0){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if(snake.getHead().getY() < field.getPickUp().getY()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getX() > field.getPickUp().getX()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getY() > field.getPickUp().getY()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        } else if (snake.getDirection().equals(SnakeDirection.RIGHT)){

            // links prüfen
            System.out.println(snake.getHead().getX());
            System.out.println(snake.getHead().getY());
            System.out.println(field.getColumns());
            if(snake.getHead().getY() == 0 ){
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getX() == field.getColumns()-1){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getY() == field.getColumns()-1){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if(snake.getHead().getY() > field.getPickUp().getY()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getX() < field.getPickUp().getX()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getY() < field.getPickUp().getY()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        }

        return result;


        /*
        counter++;

        if(counter % 3 == 0){
            return new double[]{0.1, 0.2, 0.3, 0.4};
        }
        else if (counter % 3 == 1){
            return new double[]{0.9, 0.8, 0.7, 0.6};
        }
        else {
            return new double[]{0.3, 0.8, 0.1, 0.4};
        }

         */


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

    public void input(KeyCode code){

        if(code.equals(KeyCode.UP)) inputDirection = SnakeDirection.UP;
        else if(code.equals(KeyCode.DOWN)) inputDirection = SnakeDirection.DOWN;
        else if(code.equals(KeyCode.LEFT)) inputDirection = SnakeDirection.LEFT;
        else if(code.equals(KeyCode.RIGHT)) inputDirection = SnakeDirection.RIGHT;

    }

    public String visualization(){ // TODO in den Controller

        int[][] array = new int[field.getColumns()][field.getColumns()];

        array[snake.getHead().getY()][snake.getHead().getX()] = 1;

        for(int i = 1; i < snake.getTails().size(); i++){
            array[snake.getTails().get(i).getY()][snake.getTails().get(i).getX()] = 2;
        }

        array[field.getPickUp().getY()][field.getPickUp().getX()] = 3;

        String result = "";

        for(int y = 0; y < field.getColumns(); y++){

            for(int x = 0; x < field.getColumns(); x++){
                result = result + array[y][x] + "\t";
            }
            result = result + "\n";

        }

        return result;
    }

    public Field getField() {
        return field;
    }

    public Snake getSnake() {
        return snake;
    }

    public Score getScore() {
        return score;
    }

    private boolean isCollision() {
        return false;
    } // TODO Im Field?


}
