package dev.graumann.neuralnetwork.model.io;

import java.util.List;
import java.util.Set;

/**
 * Diese Klasse stellt ein DTO zum laden und speichern der Felder als json da.
 *
 * @author Christian Graumann
 * @created 10.2019
 */
public class GameRecord {

    List<double[]> inputs;
    List<double[]> outputs;

    public GameRecord() {

    }

    public GameRecord(List<double[]> inputs, List<double[]> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public List<double[]> getInputs() {
        return inputs;
    }

    public void setInputs(List<double[]> inputs) {
        this.inputs = inputs;
    }

    public List<double[]> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<double[]> outputs) {
        this.outputs = outputs;
    }
}
