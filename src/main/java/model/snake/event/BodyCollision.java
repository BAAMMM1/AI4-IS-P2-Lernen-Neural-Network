package model.snake.event;

import model.snake.entity.Field;
import model.snake.entity.Player;
import model.snake.entity.Score;
import model.snake.entity.Snake;

public class BodyCollision extends Collision {
    public BodyCollision(Player player, Field field, Snake snake, Score score) {
        super(player, field, snake, score);
    }

    @Override
    public boolean isCollision() {

        if (snake.getTails().size() > 2) {
            for (int i = 1; i < snake.getTails().size(); i++) {

                if (snake.getHead().getX() == snake.getTails().get(i).getX() && snake.getHead().getY() == snake.getTails().get(i).getY()) {
                    return true;
                }
            }
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
