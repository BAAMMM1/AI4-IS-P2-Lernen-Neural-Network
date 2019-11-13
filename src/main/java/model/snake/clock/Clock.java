package model.snake.clock;

public class Clock {


    public void tick(){

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
