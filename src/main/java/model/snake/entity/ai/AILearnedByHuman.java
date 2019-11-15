package model.snake.entity.ai;

import model.io.GameRecord;
import model.io.IO;
import model.snake.entity.Field;
import model.snake.entity.MoveDirection;
import model.snake.entity.Player;
import model.snake.entity.Snake;
import model.snake.entity.ai.network.Network;
import model.snake.entity.ai.network.trainset.TrainSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Per Aufnahme
 */
public class AILearnedByHuman extends Player {

    private Network brain;

    public AILearnedByHuman(Field field, Snake snake) {
        super(field, snake);
        this.brain = new Network(6, 5, 5, 1);
        this.field = field;
        this.snake = snake;
        this.initTrainingData();
    }

    // [left?, straight?, right?, aleft?, astraight?, aright?] -> [0 ->left, 0.5 -> straight, 1 -> right]
    public void initTrainingData(){

        GameRecord game01 = IO.load(new File("" + System.getProperty("user.dir") + "\\db\\game400").toURI());
        List<double[]> list = new ArrayList<>();

        for (int i = 0; i < game01.getInputs().size(); i++) {
            double[] entry = new double[7];
            entry[0] = game01.getInputs().get(i)[0];
            entry[1] = game01.getInputs().get(i)[1];
            entry[2] = game01.getInputs().get(i)[2];
            entry[3] = game01.getInputs().get(i)[3];
            entry[4] = game01.getInputs().get(i)[4];
            entry[5] = game01.getInputs().get(i)[5];
            entry[6] = game01.getOutputs().get(i)[0];

            boolean found = false;

            for (int j = 0; j < list.size(); j++) {
                if( entry[0] == list.get(j)[0]
                        && entry[1] == list.get(j)[1]
                        && entry[2] == list.get(j)[2]
                        && entry[3] == list.get(j)[3]
                        && entry[4] == list.get(j)[4]
                        && entry[5] == list.get(j)[5]
                        && entry[6] == list.get(j)[6]) found = true;
            }


            if(!found) list.add(entry);
        }

        System.out.println(list.size());

        TrainSet set = new TrainSet(6, 1);

        for (int i = 0; i < list.size(); i++) {
            double[] inputs = new double[]{list.get(i)[0], list.get(i)[1], list.get(i)[2],list.get(i)[3],list.get(i)[4],list.get(i)[5]};
            double[] outputs = new double[]{list.get(i)[6]};

            set.addData(inputs, outputs);

        }

        set.addData(new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 1.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 0.0}, new double[]{0.0});
        set.addData(new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 1.0}, new double[]{0.5});
        set.addData(new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 1.0}, new double[]{0.0});
        set.addData(new double[]{1.0, 1.0, 0.0, 1.0, 0.0, 0.0}, new double[]{1.0});
        set.addData(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, new double[]{0.5});
        set.addData(new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 1.0}, new double[]{0.5});

        set.addData(new double[]{0.0, 1.0, 0.0, 1.0, 1.0, 0.0}, new double[]{0.0});
        set.addData(new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0}, new double[]{1.0});

        brain.train(set, 1000000, set.size());
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
        System.out.println("output: " + Arrays.toString(out) + String.format("%.2f", out[0]));

        AIDirection direction;

        if (out[0] >= 0.0 && out[0] <= 0.34){
            System.out.println("move left");
            direction = AIDirection.LEFT;
        }
        else if (out[0] >= 0.35 && out[0] <= 0.67){
            System.out.println("move straight");
            direction = AIDirection.STRAIGHT;
        }
        else if (out[0] >= 0.68 && out[0] <= 1.0){
            System.out.println("move right");
            direction = AIDirection.RIGHT;
        } else{
            System.out.println("move else");
            direction = AIDirection.STRAIGHT;
        }

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

        } else if(snake.getDirection().equals(MoveDirection.LEFT)){

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




    public Network getBrain() {
        return brain;
    }
}
