package model.snake.entity;

import java.util.Random;

public class PickUp {

    int x;
    int y;


    public PickUp(int fieldColumns, int fieldRows) {
        Random random = new Random();
        this.y = random.nextInt(fieldRows);
        this.x = random.nextInt(fieldColumns);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
