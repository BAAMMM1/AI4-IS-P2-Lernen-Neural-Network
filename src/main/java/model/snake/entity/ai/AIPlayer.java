package model.snake.entity.ai;

import model.snake.entity.Field;
import model.snake.entity.Snake;
import model.snake.entity.SnakeDirection;
import model.snake.entity.ai.network.Network;
import model.snake.entity.ai.network.trainset.TrainSet;

import java.util.Arrays;

public class AIPlayer {

    private Network brain;
    private Field field;
    private Snake snake;

    private double[] lastInput;
    private double[] lastOutput;

    public AIPlayer(Field field, Snake snake) {
        this.brain = new Network(6, 4, 4, 1);
        this.field = field;
        this.snake = snake;
        this.initTrainingData();
    }

    // [left?, straight?, right?, aleft?, astraight?, aright?] -> [0 ->left, 0.5 -> straight, 1 -> right]
    public void initTrainingData(){

        TrainSet set = new TrainSet(6, 1);

        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0}, new double[]{1});
        set.addData(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0}, new double[]{1});
        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0}, new double[]{0});
        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0}, new double[]{1});
        set.addData(new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 1.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0}, new double[]{0});
        set.addData(new double[]{1.0, 0.0, 0.0, 1.0, 1.0, 0.0}, new double[]{1});
        set.addData(new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 1.0}, new double[]{0});
        set.addData(new double[]{1.0, 0.0, 1.0, 1.0, 1.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 1.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 1.0, 1.0, 0.0, 1.0, 0.0}, new double[]{0});
        set.addData(new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 1.0}, new double[]{0});
        set.addData(new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 1.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 1.0, 0.0, 1.0, 1.0, 0.0}, new double[]{1});
        set.addData(new double[]{1.0, 0.0, 1.0, 1.0, 0.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0}, new double[]{1});
        set.addData(new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 1.0, 0.0, 1.0, 0.0, 0.0}, new double[]{1});
        set.addData(new double[]{0.0, 1.0, 1.0, 0.0, 1.0, 1.0}, new double[]{0});

        brain.train(set, 1000000, 26);
    }

    /**
     * Diese Methode berechnet anhand der input-Wert für die input-Neuronen die durchzuführende Aktion.
     *
     * input [obstacle_left?, obstacle_straight?, obstacle_right?, apple_left?, apple_straight?, apple_right?] wo bei 1 für Ja, 0 für Nein steht.
     * @return 0.0 -> left, 0.5 -> straight, 1.0 -> right
     */
    public AIDirection getMove() {

        double[] input = calcSituation();

        double[] out = brain.calculate(input);
        this.lastOutput = out.clone();

        System.out.println("input: " + Arrays.toString(input));
        System.out.println("output: " + Arrays.toString(out) + String.format("%.2f", out[0]));


        if (out[0] >= 0.0 && out[0] <= 0.34){
            System.out.println("move left");
            return AIDirection.LEFT;
        }
        else if (out[0] >= 0.35 && out[0] <= 0.67){
            System.out.println("move straight");
            return AIDirection.STRAIGHT;
        }
        else if (out[0] >= 0.68 && out[0] <= 1.0){
            System.out.println("move right");
            return AIDirection.RIGHT;
        } else{
            System.out.println("move else");
            return AIDirection.STRAIGHT;
        }
    }

    private double[] calcSituation() {
        // [left?, straight?, right?]
        double[] result = new double[6];

        System.out.println(snake.getHead().getX());
        System.out.println(snake.getHead().getY());
        System.out.println(snake.getDirection());

        if(snake.getDirection().equals(SnakeDirection.UP)){
            // links prüfen
            System.out.println("up");
            System.out.println(snake.getHead().getX());
            if(snake.getHead().getX() == 0) {
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getY() == 0){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getX() == field.getColumns()-1){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if(snake.getHead().getX() > field.getPickUp().getX()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getY() > field.getPickUp().getY()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getX() < field.getPickUp().getX()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

            System.out.println("result: " + Arrays.toString(result));



        } else if(snake.getDirection().equals(SnakeDirection.DOWN)){

            // links prüfen
            if(snake.getHead().getX() == field.getColumns()-1){
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getY() == field.getColumns()-1){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getX() == 0){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }


            // apfel links?
            if(snake.getHead().getX() < field.getPickUp().getX()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getY() < field.getPickUp().getY()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getX() > field.getPickUp().getX()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        } else if(snake.getDirection().equals(SnakeDirection.LEFT)){

            // links prüfen
            if(snake.getHead().getY() == field.getColumns()-1){
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getX() == 0){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() - 1, snake.getHead().getY())){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getY() == 0){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if(snake.getHead().getY() < field.getPickUp().getY()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getX() > field.getPickUp().getX()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getY() > field.getPickUp().getY()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        } else if (snake.getDirection().equals(SnakeDirection.RIGHT)){

            // links prüfen
            System.out.println(snake.getHead().getX());
            System.out.println(snake.getHead().getY());
            System.out.println(field.getColumns());
            if(snake.getHead().getY() == 0 ){
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() - 1)){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getX() == field.getColumns()-1){
                result[1] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())){
                result[1] = 1.0;
            } else {
                result[1] = 0.0;
            }
            // rechts prüfen
            if(snake.getHead().getY() == field.getColumns()-1){
                result[2] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX(), snake.getHead().getY() + 1)){
                result[2] = 1.0;
            } else {
                result[2] = 0.0;
            }

            // apfel links?
            if(snake.getHead().getY() > field.getPickUp().getY()){
                result[3] = 1.0;
            } else {
                result[3] = 0.0;
            }
            // apfel vorne?
            if(snake.getHead().getX() < field.getPickUp().getX()){
                result[4] = 1.0;
            } else {
                result[4] = 0.0;
            }
            // apfel rechts
            if(snake.getHead().getY() < field.getPickUp().getY()){
                result[5] = 1.0;
            } else {
                result[5] = 0.0;
            }

        }

        this.lastInput = result.clone();

        return result;
    }

    public boolean isBodyCollison(int x, int y) {

        if (snake.getTails().size() > 2) {
            for (int i = 1; i < snake.getTails().size(); i++) {

                if (x == snake.getTails().get(i).getX() && y == snake.getTails().get(i).getY()) {
                    System.out.println("++++++++++++++++++++++ body collision");
                    return true;
                }
            }
        }
        return false;
    }

    public double[] getLastInput() {
        return lastInput;
    }

    public double[] getLastOutput() {
        return lastOutput;
    }

    public Network getBrain() {
        return brain;
    }
}
