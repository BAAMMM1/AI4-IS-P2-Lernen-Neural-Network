package dev.graumann.neuralnetwork.model.snake.entity;

public class Field {

    private int[] field;
    private int columns;
    private int rows;

    private int xSize;
    private int ySize;

    private PickUp pickUp;

    public Field(int columns) {
        this.field = new int[columns * columns];
        this.columns = columns;
        this.rows = columns;

        this.xSize = columns - 1;
        this.ySize = columns - 1;

        pickUp = new PickUp(columns, rows);
    }

    public Field(int columns, int rows) {
        this.field = new int[columns * rows];
        this.columns = columns;
        this.rows = rows;

        this.xSize = columns - 1;
        this.ySize = rows - 1;

        pickUp = new PickUp(columns, rows);
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
        this.pickUp = new PickUp(columns, rows);
    }

    public boolean isCollision(){

        return false;
    }

    public PickUp getPickUp() {
        return pickUp;
    }

    public int getRows() {
        return rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
        this.field = new int[this.columns * rows];
    }

    public void setRows(int rows) {
        this.rows = rows;
        this.field = new int[columns * this.rows];
    }

    public static void main(String[] args) {
        Field field = new Field(15);
    }

}
