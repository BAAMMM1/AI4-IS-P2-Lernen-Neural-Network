package model.snake.entity.ai;

import javafx.scene.input.KeyCode;
import model.snake.entity.Field;
import model.snake.entity.MoveDirection;
import model.snake.entity.Player;
import model.snake.entity.Snake;

public class HumanPlayer extends Player {



    public HumanPlayer(Field field, Snake snake) {
        super(field, snake);
    }

    @Override
    public MoveDirection getMove() {

        if(code.equals(KeyCode.UP)) return MoveDirection.UP;
        else if(code.equals(KeyCode.DOWN)) return MoveDirection.DOWN;
        else if(code.equals(KeyCode.LEFT)) return MoveDirection.LEFT;
        else if(code.equals(KeyCode.RIGHT)) return MoveDirection.RIGHT;

        return MoveDirection.LEFT;
    }


}
