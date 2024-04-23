package dev.graumann.neuralnetwork.model.snake.entity;

public class SnakePart {

    private int x;
    private int y;


    private int xBefore;
    private int yBefore;

    public SnakePart(int y, int x) {
        this.y = y;
        this.x = x;

        this.xBefore = x;
        this.yBefore = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.yBefore = this.y;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.xBefore = this.x;
        this.x = x;
    }


    public int getXBefore() {
        return xBefore;
    }

    public int getYBefore() {
        return yBefore;
    }

}
