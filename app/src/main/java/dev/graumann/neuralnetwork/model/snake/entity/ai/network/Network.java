package dev.graumann.neuralnetwork.model.snake.entity.ai.network;



import dev.graumann.neuralnetwork.model.snake.entity.ai.network.trainset.TrainSet;

import java.util.Arrays;

public class Network {

    private double[][] output; // [layer][neuron] - Output für jedes Neuron
    private double[][][] weights; // [layer][neuron][prevNeuron] - Kantengewicht der Neuronen
    private double[][] bias;    // [layer][neuron] - Jedes Neuron hat einen bias

    private double[][] error_signal; // Berechneter error-Wert für jedes einzelene Neuron (Backpropagation)
    private double[][] output_derivative; // Die Ableitung des Ergebnis der sigmod Funktion für jedes Neuron (Backpropagation)

    public int[] NETWORK_LAYER_SIZES; // Anzahl an Neuron die jeder Layer hat bsp.: [4, 4] Dann hat der Input-Layer 4 Neuronen, der Ouput-Layer 4 Neuronen
    public int INPUT_SIZE;    // Anzahl an Neuronen der input Layer hat
    public int OUTPUT_SIZE;   // Anzahl an Neuronen die der output Layer hat
    public int NETWORK_SIZE;  // Anzahl an Layer des Netzwerks


    // Initialisierung
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

            // Bias-Werte werden mit Zufallswerten initialisiert
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], 0.3, 0.7);

            //weights nicht für den input layer (er hat keinen Vorherigen Layer)
            if(i > 0){
                // weights werden mit Zufallswerten zwischen den bounds initialisiert
                weights[i] = NetworkTools.createRandomArray(
                        NETWORK_LAYER_SIZES[i],
                        NETWORK_LAYER_SIZES[i-1],
                        -0.3,
                        0.5);

            }
        }

    }

    // Part 1
    // Feedforward process
    // Phase 1. Sum
    // Phase 2. Aktivierung
    public double[] calculate(double... input){
        if(input.length != this.INPUT_SIZE) return null; // Die Berechnung kann nicht durchgeführt werden, der input übereinstimmt nicht mit den Anzahlen an Neuronen im Input-Layer
        this.output[0] = input; // Output[0] dient als Puffer

        // Für jeden Layer
        for(int layer = 1; layer < NETWORK_SIZE; layer++){

            // Für jedes Neuron im Layer
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++){

                // Eingangsfunktion: Sigma (Sum) für das jeweilige Neuron - Zusammenrechnen der gewichteten Eingänge (Alle Kanten) der vorherigen Neuron + bias
                double sum = bias[layer][neuron];
                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++){
                    sum += output[layer-1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }

                // Aktivierungsfunktion: Für das jeweilige Neuron - Angewendet auf den addierten Eingang
                output[layer][neuron] = sigmod(sum);

                // Setzen des output_derivate
                output_derivative[layer][neuron] = (output[layer][neuron] * (1 - output[layer][neuron]));

            }
        }

        return output[NETWORK_SIZE-1]; // Wiedergabe des Output-Layers (letzter im Output array)
    }

    private double sigmod(double x){
        return 1d / ( 1 + Math.exp(-x));
    }

    /*
    traning
     */

    // Part 2
    // Backpropagation Algorithm
    private void train(double[] input, double[] target, double eta){ // eta = learning rate

        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) return;

        // 1. Outputs für den Input berechnen
        calculate(input);

        // 2. Berechnen des Backprop Error
        backpropError(target);

        // 3. Weights anpassen
        updateWeights(eta);

    }

    public void backpropError(double[] target){
        // 1. Ausgehend vom OutputLayer, beginne beim ersten Neuron des Output Layers, berechne
        // (Ist-Output-für-dieses-Neuron - Ziel-Output-für-dieses-Neuron) * output_derivate-für-dieses-Neuron
        for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE - 1]; neuron++) {
            error_signal[NETWORK_SIZE - 1][neuron] = (output[NETWORK_SIZE - 1][neuron] - target[neuron])
                    * output_derivative[NETWORK_SIZE - 1][neuron];
        }


        // 2. Von dem Layer hinter dem Output-Layer (Hidden-Layers) gehe durch Vorgänger Layer (außer dem Input-Layer > 0)
        for (int layer = NETWORK_SIZE - 2; layer > 0 ; layer--) {

            // 3. Beginne beim ersten Neuron des Layers -> aktuelles Neuron
            //
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;

                // 4. Für alle Nachfolgene Neuron vom aktuellen Neuron
                // Summiere die Werte: (gewicht-zum-Nachfolgenen-Neuron * errorsignal-zum-Nachfolgenen-Neuron)
                for (int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer + 1]; nextNeuron++) {
                    sum += weights[layer + 1][nextNeuron][neuron] * error_signal[layer + 1][nextNeuron];
                }


                // 5. Setze für aktuelles Neuron das error_signal neu
                // Summe der Berechnung * der berechneten Ableitung der sigmod für das aktuelle Neuron
                this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
            }

        }


    }

    public void updateWeights(double learningRate){

        // 6. Ausgehend vom Layer nach dem Input-Layer bist zum Output-Layer
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {

            // Für jedes Neuron im aktuellen Layer
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {

                // Berechne das delta
                double delta = - learningRate * error_signal[layer][neuron];

                // Passe den Bias mit dem Delta
                bias[layer][neuron] += delta;

                // Passe das Gewicht zu jedem vorheriges Neuron das Gewicht an der Kante an
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

    public Network() {

    }

    public double[][] getOutput() {
        return output;
    }

    public void setOutput(double[][] output) {
        this.output = output;
    }

    public double[][][] getWeights() {
        return weights;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }

    public double[][] getBias() {
        return bias;
    }

    public void setBias(double[][] bias) {
        this.bias = bias;
    }

    public double[][] getError_signal() {
        return error_signal;
    }

    public void setError_signal(double[][] error_signal) {
        this.error_signal = error_signal;
    }

    public double[][] getOutput_derivative() {
        return output_derivative;
    }

    public void setOutput_derivative(double[][] output_derivative) {
        this.output_derivative = output_derivative;
    }

    public int[] getNETWORK_LAYER_SIZES() {
        return NETWORK_LAYER_SIZES;
    }

    public void setNETWORK_LAYER_SIZES(int[] NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
    }

    public int getINPUT_SIZE() {
        return INPUT_SIZE;
    }

    public void setINPUT_SIZE(int INPUT_SIZE) {
        this.INPUT_SIZE = INPUT_SIZE;
    }

    public int getOUTPUT_SIZE() {
        return OUTPUT_SIZE;
    }

    public void setOUTPUT_SIZE(int OUTPUT_SIZE) {
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    public int getNETWORK_SIZE() {
        return NETWORK_SIZE;
    }

    public void setNETWORK_SIZE(int NETWORK_SIZE) {
        this.NETWORK_SIZE = NETWORK_SIZE;
    }

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
