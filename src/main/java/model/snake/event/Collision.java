package model.snake.event;

import model.snake.entity.Field;
import model.snake.entity.Score;
import model.snake.entity.Snake;

public abstract class Collision {

    Field field;
    Snake snake;
    Score score;

    public Collision(Field field, Snake snake, Score score) {
        this.field = field;
        this.snake = snake;
        this.score = score;
    }

    public abstract boolean isCollision();

    public abstract void action();

}
