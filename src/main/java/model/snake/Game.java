package model.snake;


import model.snake.clock.Clock;
import model.snake.entity.*;
import model.snake.entity.ai.*;
import model.snake.event.BodyCollision;
import model.snake.event.Collision;
import model.snake.event.PickUpCollision;
import model.snake.event.WallColision;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Modifier;
import java.util.*;

public class Game extends Observable {

    private int runs = 10;

    private Clock clock;
    private Field field;
    private Snake snake;
    private List<Collision> collisions;
    private Score score;
    private boolean play;

    private Player player;

    public Game(int fieldColumns) {
        this.clock = new Clock();
        this.field = new Field(fieldColumns);
        this.snake = new Snake(field);
        this.score = new Score();
        this.player = new Human(this.field, this.snake);

        this.collisions = Arrays.asList(
                new PickUpCollision(player, field, snake, score),
                new WallColision(player, field, snake, score),
                new BodyCollision(player, field, snake, score)
        );

    }

    public Game(int fieldColumns, int fieldRows) {
        this.clock = new Clock();
        this.field = new Field(fieldColumns, fieldRows);
        this.snake = new Snake(field);
        this.score = new Score();
        this.player = new Human(this.field, this.snake);

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

    public Map<String, String> getPlayerTypes() {

        Set<Class<? extends Player>> searchAlgortihms = new Reflections("model.snake",
                new SubTypesScanner(false))
                .getSubTypesOf(Player.class);

        Map<String, String> result = new HashMap<>();

        for (Class object : searchAlgortihms) {
            if (!Modifier.isAbstract(object.getModifiers())) {
                result.put(object.getSimpleName(), object.getName());
            }
        }

        return result;
    }

    public void setPlayer(String player) {

        if (player.equals("Human")) {
            this.player = new Human(field, snake);
        } else if (player.equals("AI")) {
            this.player = new AI(field, snake);
        } else if (player.equals("AI2")) {
            this.player = new AI2(field, snake);
        } else if (player.equals("AILearnedByHuman2")) {
                this.player = new AILearnedByHuman2(field, snake);
        } else {
            this.player = new AILearnedByHuman(field, snake);
        }

        for (Collision collision : collisions) {
            collision.setPlayer(this.player);
        }


    }
}
