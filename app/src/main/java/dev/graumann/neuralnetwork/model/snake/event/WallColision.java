package dev.graumann.neuralnetwork.model.snake.event;

import dev.graumann.neuralnetwork.model.snake.entity.Field;
import dev.graumann.neuralnetwork.model.snake.entity.Player;
import dev.graumann.neuralnetwork.model.snake.entity.Score;
import dev.graumann.neuralnetwork.model.snake.entity.Snake;

public class WallColision extends Collision{

    public WallColision(Player player, Field field, Snake snake, Score score) {
        super(player, field, snake, score);
    }

    @Override
    public boolean isCollision() {
        if(snake.getHead().getX() < 0 || snake.getHead().getY() < 0){
            return true;
        } else if(snake.getHead().getX() >= field.getColumns() || snake.getHead().getY() >= field.getRows()){
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        snake.restart();
        score.restart();
        player.nextRun();
    }
}
