package model.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Diese Klasse stellt Methoden zum speichern und laden von Feldern zur Verfügung.
 *
 * @author Christian Graumann
 * @created 10.2019
 */
public class IO {


    public static GameRecord load(URI file) {

        try {
            return new ObjectMapper().readValue(new File(file), GameRecord.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static void save(URI file, List<double[]> inputs, List<double[]> outputs) {

        System.out.println("--> " + file.toString());
        File fil2 = new File(file);
        System.out.println(fil2.toString());
        try {
            new ObjectMapper().writeValue(new File(file), new GameRecord(inputs, outputs));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {

        GameRecord game01 = IO.load(new File("" + System.getProperty("user.dir") + "\\db\\game1210").toURI());
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


    }

}
