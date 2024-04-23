package dev.graumann.neuralnetwork.model.snake.event;

import dev.graumann.neuralnetwork.model.snake.entity.MoveDirection;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public MoveDirection getMovement() {
        return MoveDirection.LEFT;
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
