package model.network;

import java.util.ArrayList;
import java.util.List;

public class Netz {

    private List<Neuron> inputLayer;
    private List<Neuron> hiddenLayer;
    private List<Neuron> outputLayer;

    public Netz(int inputLayer, int hiddenLayer, int outputLayer) {
        this.inputLayer = new ArrayList<>();
        this.hiddenLayer = new ArrayList<>();
        this.outputLayer = new ArrayList<>();

        for(int i = 0; i < inputLayer; i++){
            this.inputLayer.add(new Neuron(this.hiddenLayer));
        }

        for(int i = 0; i < hiddenLayer; i++){
            this.hiddenLayer.add(new Neuron(this.outputLayer));
        }

        for(int i = 0; i < outputLayer; i++){
            this.outputLayer.add(new Neuron(null));
        }

    }

    public int[] compute(double[] inputVektor){

        int[] inputResult = new int[this.inputLayer.size()];
        int[] hiddenResult = new int[this.hiddenLayer.size()];
        int[] outputResult = new int[this.outputLayer.size()];

        for(int i = 0; i < this.inputLayer.size(); i++){
            inputResult[i] = this.inputLayer.get(i).fire(inputVektor);
        }

        for(int i = 0; i < this.hiddenLayer.size(); i++){
            hiddenResult[i] = this.hiddenLayer.get(i).fire(inputResult);
        }

        for(int i = 0; i < this.outputLayer.size(); i++){
            outputResult[i] = this.outputLayer.get(i).fire(hiddenResult);
        }

        return outputResult;

    }

}
