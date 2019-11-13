package model.snake.entity.ai;

import model.snake.entity.SnakeDirection;
import model.snake.entity.ai.network.Network;
import model.snake.entity.ai.network.trainset.TrainSet;

import java.util.Arrays;

public class AIPlayer {

    private Network brain;

    public AIPlayer() {
        this.brain = new Network(6, 4, 4, 1);

        // TODO richtig trainieren
        TrainSet set = new TrainSet(6, 1);
        // [left?, straight?, right?, aleft?, astraight?, aright?] -> [0 ->left, 0.5 -> straight, 1 -> right]
        set.addData(new double[]{0, 0, 0, 0, 0, 0}, new double[]{0.5});

        set.addData(new double[]{1, 0, 0, 0, 0, 0}, new double[]{0.5});
        set.addData(new double[]{0, 1, 0, 0, 0, 0}, new double[]{1});
        set.addData(new double[]{0, 0, 1, 0, 0, 0}, new double[]{0.5});

        set.addData(new double[]{1, 1, 0, 0, 0, 0}, new double[]{1});

        set.addData(new double[]{0, 0, 0, 0, 0, 0}, new double[]{0.5});
        set.addData(new double[]{0, 0, 0, 1, 0, 0}, new double[]{0});
        set.addData(new double[]{0, 0, 0, 0, 1, 0}, new double[]{0.5});
        set.addData(new double[]{0, 0, 0, 0, 0, 1}, new double[]{1});
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

    // TODO richtige Richtung zurückgeben
    public AIDirection getMove(double[] input) {

        // Bei out = 0.0 -> left
        // Bei out = 0.5 -> straight
        // Bei out = 1.0 -> right

        System.out.println("input: " + Arrays.toString(input));

        double[] out = brain.calculate(input);

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

    public Network getBrain() {
        return brain;
    }
}
