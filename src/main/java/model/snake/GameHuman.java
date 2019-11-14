package model.snake;


import javafx.scene.input.KeyCode;
import model.snake.clock.Clock;
import model.snake.entity.Field;
import model.snake.entity.Score;
import model.snake.entity.Snake;
import model.snake.entity.MoveDirection;
import model.snake.event.BodyCollision;
import model.snake.event.Collision;
import model.snake.event.PickUpCollision;
import model.snake.event.WallColision;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class GameHuman extends Observable {


    private Clock clock;
    private Field  field;
    private Snake snake;
    private MoveDirection inputDirection;
    private boolean run;
    private List<Collision> collisions;
    private Score score;

    public GameHuman(int fieldColumns) {
        this.clock = new Clock();
        this.field = new Field(fieldColumns);
        this.snake = new Snake(field.getColumns());
        this.inputDirection = MoveDirection.LEFT;
        this.score = new Score();
        this.collisions = new ArrayList<>();
        collisions.add(new PickUpCollision(null, field, snake, score));
        collisions.add(new WallColision(null, field, snake, score));
        collisions.add(new BodyCollision(null, field, snake, score));

        this.run = true;
    }

    public void run(){

        while(run){
            clock.tick();
            snake.move(inputDirection);

            for(Collision collision: collisions){
                if(collision.isCollision()){
                    collision.action();
                }
            }


            setChanged();
            notifyObservers();
        }

    }

    public void input(KeyCode code){


        if(code.equals(KeyCode.UP)) inputDirection = MoveDirection.UP;
        else if(code.equals(KeyCode.DOWN)) inputDirection = MoveDirection.DOWN;
        else if(code.equals(KeyCode.LEFT)) inputDirection = MoveDirection.LEFT;
        else if(code.equals(KeyCode.RIGHT)) inputDirection = MoveDirection.RIGHT;


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


}
