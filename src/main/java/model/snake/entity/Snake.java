package model.snake.entity;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private SnakePart head;
    private List<SnakePart> tails;
    private int columns;

    private SnakeDirection direction = SnakeDirection.LEFT;


    public Snake(int fieldColumns) {
        this.columns = fieldColumns;
        tails = new ArrayList<>();
        head = new SnakePart(fieldColumns / 2, fieldColumns / 2);
        tails.add(head);

    }

    public void move(SnakeDirection direction) {

        if (direction.equals(SnakeDirection.UP) && !this.direction.equals(SnakeDirection.DOWN)) {
            this.direction = direction;
            moveUp();
        } else if (direction.equals(SnakeDirection.DOWN) && !this.direction.equals(SnakeDirection.UP)) {
            this.direction = direction;
            moveDown();
        } else if (direction.equals(SnakeDirection.LEFT) && !this.direction.equals(SnakeDirection.RIGHT)) {
            this.direction = direction;
            moveLeft();
        } else if (direction.equals(SnakeDirection.RIGHT) && !this.direction.equals(SnakeDirection.LEFT)) {
            this.direction = direction;
            moveRight();
        } else {
            move(this.direction);
        }


    }

    public void moveLeft() {

        moveTails();

        head.setX(tails.get(0).getX() - 1);

    }

    public void moveRight() {

        moveTails();

        head.setX(tails.get(0).getX() + 1);

    }

    public void moveUp() {

        moveTails();

        head.setY(tails.get(0).getY() - 1);
    }

    public void moveDown() {

        moveTails();

        head.setY(tails.get(0).getY() + 1);

    }

    public void moveTails(){
        for (int i = tails.size() - 1; i >= 1; i--) {
            tails.get(i).setX(tails.get(i-1).getX());
            tails.get(i).setY(tails.get(i-1).getY());
        }
    }

    public SnakePart getHead() {
        return head;
    }

    public List<SnakePart> getTails() {
        return tails;
    }

    public void addTail() {

        tails.add(new SnakePart(tails.get(tails.size()-1).getY(), tails.get(tails.size()-1).getX()));

    }

    public void restart(){
        tails.clear();
        head.setX(columns/2);
        head.setY(columns/2);
        tails.add(head);
    }
}
