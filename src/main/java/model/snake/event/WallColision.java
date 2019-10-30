package model.snake.event;

import model.snake.entity.Field;
import model.snake.entity.Score;
import model.snake.entity.Snake;

public class WallColision extends Collision{

    public WallColision(Field field, Snake snake, Score score) {
        super(field, snake, score);
    }

    @Override
    public boolean isCollision() {
        if(snake.getHead().getX() < 0 || snake.getHead().getY() < 0){
            return true;
        } else if(snake.getHead().getX() >= field.getColumns() || snake.getHead().getY() >= field.getColumns()){
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        snake.restart();
        score.restart();
    }
}
