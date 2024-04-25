package dev.graumann.neuralnetwork.model.snake.entity;

public class Field {

    private int columns;
    private int rows;

    private PickUp pickUp;

    public Field(int columns) {
        this.columns = columns;
        this.rows = columns;

        pickUp = new PickUp(columns, rows);
    }

    public Field(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;

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
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

}
