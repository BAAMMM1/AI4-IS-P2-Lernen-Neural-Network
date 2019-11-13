package model.snake.entity.ai.network;



import model.snake.entity.ai.network.trainset.TrainSet;

import java.util.Arrays;

public class Network {

    private double[][] output; // [layer][neuron] - Output für jedes Neuron
    private double[][][] weights; // [layer][neuron][prevNeuron] - Kante der Neuronen
    private double[][] bias;    // [layer][neuron] - Jedes Neuron hat einen bias

    private double[][] error_signal;
    private double[][] output_derivative;

    public final int[] NETWORK_LAYER_SIZES; // Anzahl an Neuron die jeder Layer hat bsp.: [4, 4] Dann hat der Input-Layer 4 Neuronen, der Ouput-Layer 4 Neuronen
    public final int INPUT_SIZE;    // Anzahl an Neuronen der input Layer hat
    public final int OUTPUT_SIZE;   // Anzahl an Neuronen die der output Layer hat
    public final int NETWORK_SIZE;  // Anzahl an Layer des  Netzwerks

    public Network(int... network_layer_size) {
        NETWORK_LAYER_SIZES = network_layer_size;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE-1];

        this.output = new double[NETWORK_SIZE][];
        this.weights = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];

        this.error_signal = new double[NETWORK_SIZE][];
        this.output_derivative = new double[NETWORK_SIZE][];

        for(int i = 0; i < NETWORK_SIZE; i++){
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
            //this.bias[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], 0.3, 0.7);

            if(i > 0){
                //weights[i] = new double[NETWORK_LAYER_SIZES[i]][NETWORK_LAYER_SIZES[i-1]];
                weights[i] = NetworkTools.createRandomArray(
                        NETWORK_LAYER_SIZES[i],
                        NETWORK_LAYER_SIZES[i-1],
                        -0.3,
                        0.5);

            }
        }

    }

    public double[] calculate(double... input){
        if(input.length != this.INPUT_SIZE) return null; // Die Berechnung kann nicht durchgeführt werden, der input übereinstimmt nicht mit den Anzahlen an Neuronen im Input-Layer
        this.output[0] = input;

        for(int layer = 1; layer < NETWORK_SIZE; layer++){

            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){

                double sum = bias[layer][neuron];
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++){
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }

                output[layer][neuron] = sigmod(sum);
                output_derivative[layer][neuron] = (output[layer][neuron] * (1 - output[layer][neuron]));

            }
        }
        return output[NETWORK_SIZE-1];
    }

    private double sigmod(double x){
        return 1d / ( 1 + Math.exp(-x));
    }

    /*
    traning
     */

    private void train(double[] input, double[] target, double eta){ // eta = learning rate

        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) return;

        calculate(input);
        backpropError(target);
        updateWeights(eta);

    }

    public void backpropError(double[] target){
        for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE - 1]; neuron++) {
            error_signal[NETWORK_SIZE - 1][neuron] = (output[NETWORK_SIZE - 1][neuron] - target[neuron])
                    * output_derivative[NETWORK_SIZE - 1][neuron];
        }
        for (int layer = NETWORK_SIZE - 2; layer > 0 ; layer--) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;

                for (int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer + 1]; nextNeuron++) {
                    sum += weights[layer + 1][nextNeuron][neuron] * error_signal[layer + 1][nextNeuron];
                }
                this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
            }
        }
    }

    public void updateWeights(double eta){
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {

                double delta = - eta * error_signal[layer][neuron];
                bias[layer][neuron] += delta;

                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer -1 ]; prevNeuron++) {
                    weights[layer][neuron][prevNeuron] += delta * output[layer - 1][prevNeuron];
                }

            }
        }
    }

    /*
    train set
     */
    public void train(TrainSet set, int loops, int batch_size){

        if(set.INPUT_SIZE != INPUT_SIZE || set.OUTPUT_SIZE != OUTPUT_SIZE) return;

        for (int i = 0; i < loops; i++) {
            TrainSet batch = set.extractBatch(batch_size);
            for (int b = 0; b < batch_size; b++) {
                this.train(batch.getInput(b), batch.getOutput(b), 0.3);
            }
            //System.out.println(MSE(batch));
        }
    }

    public double MSE(double[] input, double[] target) {
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) return 0;
        calculate(input);
        double v = 0;
        for(int i = 0; i < target.length; i++) {
            v += (target[i] - output[NETWORK_SIZE-1][i]) * (target[i] - output[NETWORK_SIZE-1][i]);
        }
        return v / (2d * target.length);
    }

    public double MSE(TrainSet set) {
        double v = 0;
        for(int i = 0; i< set.size(); i++) {
            v += MSE(set.getInput(i), set.getOutput(i));
        }
        return v / set.size();
    }

    /*
    public static void main(String[] args) {
        Network network = new Network(4, 3, 3, 2);

        double[] in = new double[]{0.1, 0.2, 0.3, 0.4};
        double[] target = new double[]{0.9, 0.1};

        double[] in2 = new double[]{0.6, 0.1, 0.4, 0.8};
        double[] target2 = new double[]{0.1, 0.9};

        for (int i = 0; i < 10000; i++) {
            network.train(in, target,0.3);
            network.train(in2, target2,0.3);
        }

        //double[] output = network.calculate(0.2, 0.9, 0.3, 0.4); // Input-Layer-Size = 4, dann 4 inputs bei calculate;


        /*
        // Er referienziert hier die Instanzvariable als return, deshalb der gleiche Wert;
        // Man müsste sich also diesen return wert kopieren
        double[] out = network.calculate(in);
        double[] out2 = network.calculate(in2);
        System.out.println(Arrays.toString(out));
        System.out.println(Arrays.toString(out2));
         */
        /*
        System.out.println(Arrays.toString(network.calculate(in)));
        System.out.println(Arrays.toString(network.calculate(in2)));
    }
    */

    public static void main(String[] args) {
        Network net = new Network(4,3,3,2);

        TrainSet set = new TrainSet(4, 2);
        set.addData(new double[]{0.1, 0.2, 0.3, 0.4}, new double[]{0.9, 0.1});
        set.addData(new double[]{0.9, 0.8, 0.7, 0.6}, new double[]{0.1, 0.9});
        set.addData(new double[]{0.3, 0.8, 0.1, 0.4}, new double[]{0.3, 0.7});
        set.addData(new double[]{0.9, 0.8, 0.1, 0.2}, new double[]{0.7, 0.3});

        net.train(set, 100000, 4);

        for (int i = 0; i < 4; i++) {
            System.out.println(Arrays.toString(net.calculate(set.getInput(i))));
        }
    }

}