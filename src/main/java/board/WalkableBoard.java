package board;

import demo.Game;
import javafx.scene.shape.Rectangle;

public class WalkableBoard {

    private char[][] walkableBoard;
    private Game game;

    public WalkableBoard(Game game) {
        this.game = game;
        prepareBoard();
    }

    public char[][] getWalkableBoard() {
        return walkableBoard;
    }

    private void prepareBoard() {

        walkableBoard = new char[Game.WIDTH][Game.HEIGHT];
        fillTable();
        fillWithWalkableFields(game);
    }

    private void fillTable() {

        for (int i = 0; i < 680; i++) {
            for (int j = 0; j < 680; j++) {
                walkableBoard[i][j] = 'O';
            }
        }
    }

    private void fillWithWalkableFields(Game game) {

        for (Rectangle shape : game.getWalls()) {
            for (int i = (int) shape.getLayoutX(); i < shape.getLayoutX() + shape.getWidth(); i++) {
                for (int j = (int) shape.getLayoutY(); j < shape.getLayoutY() + shape.getHeight(); j++) {
                    walkableBoard[i][j] = ' ';
                }
            }
        }
    }
}
