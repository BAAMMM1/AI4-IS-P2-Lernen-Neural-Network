package model.snake.event;

import model.snake.entity.Field;
import model.snake.entity.Score;
import model.snake.entity.Snake;

public class PickUpCollision extends Collision {


    public PickUpCollision(Field field, Snake snake, Score score) {
        super(field, snake, score);
    }

    @Override
    public boolean isCollision() {
        if(snake.getHead().getY() == field.getPickUp().getY() && snake.getHead().getX() == field.getPickUp().getX()){
            return true;
        }

        return false;
    }

    @Override
    public void action() {
        snake.addTail();
        field.nextPickUp();
        score.increase();
    }
}
