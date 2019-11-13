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

    private static final int RUNS = 100;

    private Clock clock;
    private Field  field;
    private Snake snake;
    private SnakeDirection inputDirection;
    private List<Collision> collisions;
    private Score score;

    private AIPlayer aiPlayer;

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

        this.aiPlayer = new AIPlayer(this.field, this.snake);
    }

    public void run(){
        int counter = 0;

        while(counter < RUNS){
            clock.tick();

            snake.moveAI(aiPlayer.getMove());

            for(Collision collision: collisions){
                if(collision.isCollision()){

                    if(collision.getClass().equals(BodyCollision.class) || collision.getClass().equals(WallColision.class)){
                        counter++;
                        inputs.add(aiPlayer.getLastInput().clone());
                        outputs.add(aiPlayer.getLastOutput().clone());
                        scores.add(score.getScore());
                    }
                    collision.action();

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



    public void input(KeyCode code){

        if(code.equals(KeyCode.UP)) inputDirection = SnakeDirection.UP;
        else if(code.equals(KeyCode.DOWN)) inputDirection = SnakeDirection.DOWN;
        else if(code.equals(KeyCode.LEFT)) inputDirection = SnakeDirection.LEFT;
        else if(code.equals(KeyCode.RIGHT)) inputDirection = SnakeDirection.RIGHT;

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


}
