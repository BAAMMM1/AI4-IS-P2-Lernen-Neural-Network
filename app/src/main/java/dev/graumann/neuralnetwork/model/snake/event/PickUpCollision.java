package dev.graumann.neuralnetwork.model.snake.event;

import dev.graumann.neuralnetwork.model.snake.entity.Field;
import dev.graumann.neuralnetwork.model.snake.entity.Player;
import dev.graumann.neuralnetwork.model.snake.entity.Score;
import dev.graumann.neuralnetwork.model.snake.entity.Snake;

public class PickUpCollision extends Collision {


    public PickUpCollision(Player player, Field field, Snake snake, Score score) {
        super(player, field, snake, score);
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
