package model.snake.event;

import model.snake.entity.Field;
import model.snake.entity.Player;
import model.snake.entity.Score;
import model.snake.entity.Snake;

public abstract class Collision {

    Player player;
    Field field;
    Snake snake;
    Score score;

    public Collision(Player player, Field field, Snake snake, Score score) {
        this.player = player;
        this.field = field;
        this.snake = snake;
        this.score = score;
    }

    public abstract boolean isCollision();

    public abstract void action();

}
