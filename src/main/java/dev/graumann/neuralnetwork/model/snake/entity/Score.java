package dev.graumann.neuralnetwork.model.snake.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Score {

    private int score;
    private int bestScore;

    private List<Integer> history;
    private int averageScore;

    public Score() {
        history = new ArrayList<>();
    }

    public void increase(){
        score += 1;
    }

    public void restart(){
        if(score > bestScore) bestScore = score;
        history.add(new Integer(score));
        averageScore = (int) history.stream().mapToInt(Integer::intValue).average().getAsDouble();
        score = 0;
    }

    public void stop(){
        bestScore = 0;
        history = new ArrayList<>();
        averageScore = 0;
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public List<Integer> getHistory() {
        return history;
    }

    public int getAverageScore(){
        return averageScore;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score=" + score +
                ", bestScore=" + bestScore +
                '}';
    }
}
