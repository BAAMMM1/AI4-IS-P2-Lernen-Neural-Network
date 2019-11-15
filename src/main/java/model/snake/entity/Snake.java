package model.snake.entity;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private SnakePart head;
    private List<SnakePart> tails;
    private int columns;
    private int rows;

    private MoveDirection direction = MoveDirection.LEFT;


    public Snake(int fieldColumns, int fieldRows) {
        this.columns = fieldColumns;
        this.rows = fieldRows;
            tails = new ArrayList<>();
        head = new SnakePart(fieldRows / 2, fieldColumns / 2);
        tails.add(head);

    }



    public void move(MoveDirection direction) {

        if (direction.equals(MoveDirection.UP) && !this.direction.equals(MoveDirection.DOWN)) {
            this.direction = direction;
            moveUp();
        } else if (direction.equals(MoveDirection.DOWN) && !this.direction.equals(MoveDirection.UP)) {
            this.direction = direction;
            moveDown();
        } else if (direction.equals(MoveDirection.LEFT) && !this.direction.equals(MoveDirection.RIGHT)) {
            this.direction = direction;
            moveLeft();
        } else if (direction.equals(MoveDirection.RIGHT) && !this.direction.equals(MoveDirection.LEFT)) {
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

    public void moveTails() {
        for (int i = tails.size() - 1; i >= 1; i--) {
            tails.get(i).setX(tails.get(i - 1).getX());
            tails.get(i).setY(tails.get(i - 1).getY());
        }
    }

    public SnakePart getHead() {
        return head;
    }

    public List<SnakePart> getTails() {
        return tails;
    }

    public void addTail() {

        tails.add(new SnakePart(tails.get(tails.size() - 1).getY(), tails.get(tails.size() - 1).getX()));

    }

    public void restart() {
        tails.clear();
        head.setX(columns / 2);
        head.setY(rows / 2);
        tails.add(head);
    }

    public MoveDirection getDirection() {
        return direction;
    }
}
