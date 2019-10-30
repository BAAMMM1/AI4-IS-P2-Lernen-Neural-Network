package model.snake.entity;

public class Score {

    private int score;
    private int bestScore;

    public Score() {
    }

    public void increase(){
        score += 1;
    }

    public void restart(){
        if(score > bestScore) bestScore = score;
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public int getBestScore() {
        return bestScore;
    }

    @Override
    public String toString() {
        return "Score{" +
                "score=" + score +
                ", bestScore=" + bestScore +
                '}';
    }
}
