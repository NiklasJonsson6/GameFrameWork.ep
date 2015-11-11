package lab1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;

public class SnakeModel extends GameModel {

    //Variables:
    public enum Directions {
        EAST(1, 0),
        WEST(-1, 0),
        NORTH(0, -1),
        SOUTH(0, 1),
        NONE(0, 0);

        private final int xDelta;
        private final int yDelta;

        Directions(final int xDelta, final int yDelta) {
            this.xDelta = xDelta;
            this.yDelta = yDelta;
        }

        public int getXDelta() {
            return this.xDelta;
        }

        public int getYDelta() {
            return this.yDelta;
        }
    }
    /** Graphical representation of the food tile */
    private static final GameTile FOOD_TILE = new RoundTile(Color.RED);
    /** Graphical representation of a blank tile */
    private static final GameTile BLANK_TILE = new GameTile();
    /** Graphical representation of a snake tile */
    private static final GameTile SNAKE_TILE = new RectangularTile(Color.BLACK);
    /** Graphical representation of the snake*/
    private Deque Snake = new ArrayDeque();
    /** Position of the food tile */
    private Position foodPos = new Position(0, 0);
    /** Position of the head of the snake */
    private Position snakePos;
    /** Current direction of the snake */
    private Directions direction = Directions.NORTH;
    /** Food eaten / snake body length */
    private int score;
    /** Size of gameboard */
    private Dimension size = getGameboardSize();

    //Constructors
    public SnakeModel() {
        /*//Size of gameboard
        Dimension size = getGameboardSize();*/

        //Blank out gameboard
        for (int i = 0; i < size.width; i++) {
            for (int j = 0; j < size.height; j++) {
                setGameboardState(i, j, BLANK_TILE);
            }
        }

        //Create snake head
        Snake.add(new Position(size.width/2, size.height/2));

        //Insert snake
        snakePos = new Position(size.width/2, size.height/2);
        setGameboardState((Position)Snake.element(), SNAKE_TILE);

        //Insert food
        addFood();
    }

    //Methods
    private void addFood() {
        //Find blank position
        do {
            foodPos = new Position((int) (Math.random() * size.width), (int) (Math.random() * size.height));
        } while (!isPositionEmpty(foodPos));
        //add new food
        setGameboardState(foodPos, FOOD_TILE);
    }

    private boolean isPositionEmpty(final Position pos) {
        return (getGameboardState(pos) == BLANK_TILE);
    }

    private void updateDirection(final int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                direction = Directions.WEST;
                break;
            case KeyEvent.VK_UP:
                direction = Directions.NORTH;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Directions.EAST;
                break;
            case KeyEvent.VK_DOWN:
                direction = Directions.SOUTH;
                break;
            default:
                // Don't change direction if another key is pressed
                break;
        }
    }

    private Position getNextSnakePos() {
        return new Position(
                ((Position)Snake.getFirst()).getX() + direction.getXDelta(),
                ((Position)Snake.getFirst()).getY() + direction.getYDelta());
    }

    private boolean isOutOfBounds(Position pos) {
        return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
                || pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
    }

    public void gameUpdate(final int lastKey) throws GameOverException {
        updateDirection(lastKey);

        //Move snake..
        Snake.addFirst(getNextSnakePos());

        if(isOutOfBounds((Position) Snake.getFirst()) //if border or snake body is hit
                || (getGameboardState((Position) Snake.getFirst()) instanceof RectangularTile)) {
            throw new GameOverException(score);
        }

        if (!(getGameboardState((Position)Snake.getFirst()) instanceof RoundTile)) { //if food is not eaten, remove tail
            setGameboardState((Position)Snake.getLast(), BLANK_TILE);
            Snake.removeLast();
        } else { //if food is eaten, skip removing tail once and add new food
            score++;
            addFood();
        }
        setGameboardState((Position)Snake.getFirst(), SNAKE_TILE);
    }
}
