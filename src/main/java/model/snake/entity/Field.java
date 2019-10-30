package model.snake.entity;

public class Field {

    private int[] field;
    private int columns;

    private int xSize;
    private int ySize;

    private PickUp pickUp;

    public Field(int columns) {
        this.field = new int[columns * columns];
        this.columns = columns;

        this.xSize = columns - 1;
        this.ySize = columns - 1;

        pickUp = new PickUp(columns);
    }

    public int getColumns() {
        return columns;
    }

    public int startX(){
        return columns/2;
    }

    public int startY(){
        return columns/2;
    }


    public void nextPickUp(){
        this.pickUp = new PickUp(columns);
    }

    public boolean isCollision(){

        return false;
    }

    public PickUp getPickUp() {
        return pickUp;
    }

    public static void main(String[] args) {
        Field field = new Field(15);
    }

}
