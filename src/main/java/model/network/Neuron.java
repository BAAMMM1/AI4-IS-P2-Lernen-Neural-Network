package model.network;

import java.util.List;

public class Neuron {

    double[] gewicht;
    double schwellenwert;
    double bias;

    private List<Neuron> sendToList;

    public Neuron(List<Neuron> sendToList) {
        this.sendToList = sendToList;
    }

    public List<Neuron> sendToList() {
        return sendToList;
    }

    public int fire(double[] input){

        double sum = 0;
        double length = Math.min(input.length, gewicht.length);
        for(int i = 0; i < length; i++){
            sum += gewicht[i] * input[i];
        }
        sum += bias;

        if(schwellenwert <= sum){
            return 1;
        } else {
            return 0;
        }
    }

    public int fire(int[] input){

        double sum = 0;
        double length = Math.min(input.length, gewicht.length);
        for(int i = 0; i < length; i++){
            sum += gewicht[i] * input[i];
        }
        sum += bias;

        if(schwellenwert <= sum){
            return 1;
        } else {
            return 0;
        }
    }

}
