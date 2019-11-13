package model.snake.clock;

public class Clock {


    public void tick(){

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
