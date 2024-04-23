package dev.graumann.neuralnetwork.model.snake.event;

import dev.graumann.neuralnetwork.model.snake.entity.Field;
import dev.graumann.neuralnetwork.model.snake.entity.Player;
import dev.graumann.neuralnetwork.model.snake.entity.Score;
import dev.graumann.neuralnetwork.model.snake.entity.Snake;

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

    public void setPlayer(Player player) {
        this.player = player;
    }
}
