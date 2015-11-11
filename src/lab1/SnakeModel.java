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
    /** Graphical representation of the snake*/
    private static Deque Snake = new ArrayDeque();
    /** Graphical representation of a blank tile */
    private static final GameTile BLANK_TILE = new GameTile();
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
        Snake.addFirst(new RectangularTile(Color.BLACK));

        //Insert snake
        snakePos = new Position(size.width*2, size.height*2);
        setGameboardState(snakePos, (GameTile) Snake.getFirst());

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
                snakePos.getX() + direction.getXDelta(),
                snakePos.getY() + direction.getYDelta());
    }




    public void gameUpdate(final int lastKey) throws GameOverException {

    }
}
