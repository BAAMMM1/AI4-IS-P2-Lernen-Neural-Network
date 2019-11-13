package model.snake.entity.ai.network;

public class NetworkTools {

    public static double[] createArray(int size, double init_value){

        // Erstt ein Array mit der Größe Size.
        // Jeder Index in diesem Array ist mit dem init_value initialisiert.

        if(size < 1){
            return null;
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = init_value;
        }
        return ar;
    }

    public static double[] createRandomArray(int size, double lower_bound, double upper_bound){

        // Erstt ein Array mit der Größe Size.
        // Jeder Index in diesem Array ist mit einem random zwischen den bounds initialisiert.

        if(size < 1){
            return null;
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = randomValue(lower_bound,upper_bound);
        }
        return ar;
    }

    public static double[][] createRandomArray(int sizeX, int sizeY, double lower_bound, double upper_bound){

        // Erstt ein 2D-Array mit den Größe Size [x][y].
        // Jeder Index in diesem Array ist mit einem random zwischen den bounds initialisiert.


        if(sizeX < 1 || sizeY < 1){
            return null;
        }
        double[][] ar = new double[sizeX][sizeY];
        for(int i = 0; i < sizeX; i++){
            ar[i] = createRandomArray(sizeY, lower_bound, upper_bound);
        }
        return ar;
    }

    public static double randomValue(double lower_bound, double upper_bound){

        // Erstellt ein random zwischen dem lower und upper bound

        return Math.random()*(upper_bound-lower_bound) + lower_bound;
    }

    public static Integer[] randomValues(int lowerBound, int upperBound, int amount) {

        // Erstellt ein Integer Array mit random, jedoch kein Wert kann doppelt in diesem Arry vorkommen

        lowerBound --;

        if(amount > (upperBound-lowerBound)){
            return null;
        }

        Integer[] values = new Integer[amount];
        for(int i = 0; i< amount; i++){
            int n = (int)(Math.random() * (upperBound-lowerBound+1) + lowerBound);
            while(containsValue(values, n)){
                n = (int)(Math.random() * (upperBound-lowerBound+1) + lowerBound);
            }
            values[i] = n;
        }
        return values;
    }

    public static <T extends Comparable<T>> boolean containsValue(T[] ar, T value){

        // Prüft ob value im array ar ist

        for(int i = 0; i < ar.length; i++){
            if(ar[i] != null){
                if(value.compareTo(ar[i]) == 0){
                    return true;
                }
            }

        }
        return false;
    }

    public static int indexOfHighestValue(double[] values){

        // Ermittelt den Index des höchsten Wert in einem double array

        int index = 0;
        for(int i = 1; i < values.length; i++){
            if(values[i] > values[index]){
                index = i;
            }
        }
        return index;
    }



}
