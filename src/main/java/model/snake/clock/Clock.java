package model.snake.clock;

public class Clock {

    private int time;

    public Clock() {
        this.time = 100;
    }

    public void tick(){

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
