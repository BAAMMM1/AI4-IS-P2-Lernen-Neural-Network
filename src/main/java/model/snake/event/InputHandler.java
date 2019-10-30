package model.snake.event;

import model.snake.entity.SnakeDirection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public SnakeDirection getMovement() {
        return SnakeDirection.LEFT;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()){

            case KeyEvent.VK_W:
                System.out.println("w");
                break;
            case KeyEvent.VK_S:
                System.out.println("s");
                break;
            case KeyEvent.VK_A:
                System.out.println("a");
                break;
            case KeyEvent.VK_D:
                System.out.println("d");
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {

        InputHandler inputHandler = new InputHandler();

        while(true){

        }
    }

}
