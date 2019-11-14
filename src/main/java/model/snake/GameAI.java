package model.snake;


import model.snake.clock.Clock;
import model.snake.entity.*;
import model.snake.entity.ai.AIPlayer;
import model.snake.event.BodyCollision;
import model.snake.event.Collision;
import model.snake.event.PickUpCollision;
import model.snake.event.WallColision;

import java.util.*;

public class GameAI extends Observable {

    private int runs = 10;

    private Clock clock;
    private Field field;
    private Snake snake;
    private List<Collision> collisions;
    private Score score;
    private boolean play;

    private Player player;

    public GameAI(int fieldColumns) {
        this.clock = new Clock();
        this.field = new Field(fieldColumns);
        this.snake = new Snake(field.getColumns());
        this.score = new Score();
        this.player = new AIPlayer(this.field, this.snake);

        this.collisions = Arrays.asList(
                new PickUpCollision(player, field, snake, score),
                new WallColision(player, field, snake, score),
                new BodyCollision(player, field, snake, score)
        );

    }

    public void play() {
        this.play = true;


        while (player.getRunCounter() < runs && play) {
            clock.tick();

            snake.move(player.getMove());

            for (Collision collision : collisions) {
                if (collision.isCollision()) {
                    collision.action();
                }
            }

            setChanged();
            notifyObservers();
        }

        printPlayerFailList();

    }

    private void printPlayerFailList() {

        for (int i = 0; i < player.getLastInputHistory().size(); i++) {
            System.out.println(
                    Arrays.toString(player.getLastInputHistory().get(i)) + " " + Arrays.toString(player.getLastOutputHistroy().get(i)) + " score: " + score.getHistory().get(i));
        }

        System.out.println("Average Scores: " + score.getAverageScore());

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

    public Clock getClock() {
        return clock;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public Player getPlayer() {
        return player;
    }

    public void stop() {
        snake.restart();
        player.restart();
        score.stop();
        field.nextPickUp();
        setChanged();
        notifyObservers();
    }
}
