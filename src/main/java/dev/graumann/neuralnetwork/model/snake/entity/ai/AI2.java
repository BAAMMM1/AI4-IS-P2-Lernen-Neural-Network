package model.snake.entity.ai;

import model.io.IO;
import model.snake.entity.Field;
import model.snake.entity.MoveDirection;
import model.snake.entity.Player;
import model.snake.entity.Snake;
import model.snake.entity.ai.network.Network;
import model.snake.entity.ai.network.trainset.TrainSet;

import java.io.File;
import java.util.Arrays;

/**
 * Per Analyse
 */
public class AI2 extends Player {

    private Network brain;

    public AI2(Field field, Snake snake) {
        super(field, snake);
        this.brain = IO.loadNetwork(new File("" + System.getProperty("user.dir") + "\\networks\\ai2").toURI());
        this.field = field;
        this.snake = snake;

        if(brain == null){
            this.brain = new Network(6, 5, 5, 3);
            this.initTrainingData();
        }
    }

    // [left?, straight?, right?, aleft?, astraight?, aright?] -> [0 ->left, 0.5 -> straight, 1 -> right]
    public void initTrainingData(){

        TrainSet set = new TrainSet(6, 3);

        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0}, new double[]{0.0, 1.0, 0.0});

        set.addData(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0}, new double[]{1.0, 0.0, 0.0});
        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0}, new double[]{1.0, 0.0, 0.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 1.0, 1.0, 0.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 1.0}, new double[]{1.0, 0.0, 0.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 1.0, 1.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 1.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 0.0}, new double[]{1.0, 0.0, 0.0});
        set.addData(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0}, new double[]{1.0, 0.0, 0.0});
        //set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{1.0, 1.0, 0.0, 0.0, 1.0, 0.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.0, 0.0, 1.0});
        set.addData(new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 1.0}, new double[]{1.0, 0.0, 0.0});
        set.addData(new double[]{0.0, 0.0, 0.0, 1.0, 1.0, 0.0}, new double[]{0.0, 1.0, 0.0});
        set.addData(new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 0.0}, new double[]{1.0, 0.0, 0.0});
        set.addData(new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0}, new double[]{0.0, 0.0, 1.0});


        brain.train(set, 1000000, set.size());

        IO.saveNetwork(new File("" + System.getProperty("user.dir") + "\\networks\\ai2").toURI(), brain);
    }

    /**
     * Diese Methode berechnet anhand der input-Wert für die input-Neuronen die durchzuführende Aktion.
     *
     * input [obstacle_left?, obstacle_straight?, obstacle_right?, apple_left?, apple_straight?, apple_right?] wo bei 1 für Ja, 0 für Nein steht.
     * @return 0.0 -> left, 0.5 -> straight, 1.0 -> right
     */
    public MoveDirection getMove() {

        double[] input = calcNeuronInputs();

        double[] out = brain.calculate(input);
        this.lastOutput = out.clone();

        System.out.println("input: " + Arrays.toString(input));
        System.out.println("output: " + Arrays.toString(out));

        AIDirection direction;


        if(out[0] > out[1] && out[0] > out[2] ){
            direction = AIDirection.LEFT;

        } else if(out[1] > out[0] && out[1] > out[2]){
            direction = AIDirection.STRAIGHT;

        } else {
            direction = AIDirection.RIGHT;

        }

        System.out.println("calc: " + direction);

        return convertDirection(direction);
    }

    public MoveDirection convertDirection(AIDirection aiDirection){
        MoveDirection direction = null;

        if (aiDirection.equals(AIDirection.STRAIGHT)) {

            if(snake.getDirection().equals(MoveDirection.UP)){
                direction = MoveDirection.UP;
            } else if(this.snake.getDirection().equals(MoveDirection.DOWN)){
                direction = MoveDirection.DOWN;
            } else if(this.snake.getDirection().equals(MoveDirection.LEFT)){
                direction = MoveDirection.LEFT;
            } else if(this.snake.getDirection().equals(MoveDirection.RIGHT)){
                direction = MoveDirection.RIGHT;
            }

        } else if (aiDirection.equals(AIDirection.LEFT)) {

            if(this.snake.getDirection().equals(MoveDirection.UP)){
                direction = MoveDirection.LEFT;
            } else if(this.snake.getDirection().equals(MoveDirection.DOWN)){
                direction = MoveDirection.RIGHT;
            } else if(this.snake.getDirection().equals(MoveDirection.LEFT)){
                direction = MoveDirection.DOWN;
            } else if(this.snake.getDirection().equals(MoveDirection.RIGHT)){
                direction = MoveDirection.UP;
            }

        } else if (aiDirection.equals(AIDirection.RIGHT)) {

            if(this.snake.getDirection().equals(MoveDirection.UP)){
                direction = MoveDirection.RIGHT;
            } else if(this.snake.getDirection().equals(MoveDirection.DOWN)){
                direction = MoveDirection.LEFT;
            } else if(this.snake.getDirection().equals(MoveDirection.LEFT)){
                direction = MoveDirection.UP;
            } else if(this.snake.getDirection().equals(MoveDirection.RIGHT)){
                direction = MoveDirection.DOWN;
            }

        }
        System.out.println("move SnakeDirection: " + direction);
        return direction;
    }

    private double[] calcNeuronInputs() {
        // [obstacle_left?, obstacle_straight?, obstacle_right?, apple_left?, apple_straight?, apple_right?]
        double[] result = new double[6];

        System.out.println(snake.getHead().getX());
        System.out.println(snake.getHead().getY());
        System.out.println(snake.getDirection());

        if(snake.getDirection().equals(MoveDirection.UP)){
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



        } else if(snake.getDirection().equals(MoveDirection.DOWN)){

            // links prüfen
            if(snake.getHead().getX() == field.getColumns()-1){
                result[0] = 1.0;
            } else if(isBodyCollison(snake.getHead().getX() + 1, snake.getHead().getY())){
                result[0] = 1.0;
            } else {
                result[0] = 0.0;
            }
            // vorne Prüfen
            if(snake.getHead().getY() == field.getRows()-1){
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

        } else if(snake.getDirection().equals(MoveDirection.LEFT)){

            // links prüfen
            if(snake.getHead().getY() == field.getRows()-1){
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

        } else if (snake.getDirection().equals(MoveDirection.RIGHT)){

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
            if(snake.getHead().getY() == field.getRows()-1){
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




    public Network getBrain() {
        return brain;
    }
}
