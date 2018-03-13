package helper;

import demo.Game;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class WalkableBoard {

    private char[][] board;
    private final int BOARD_WIDTH = Game.WIDTH;
    private final int BOARD_HEIGHT = Game.HEIGHT;

    public WalkableBoard() {
        this.board = new char[BOARD_WIDTH][BOARD_HEIGHT];
    }

    public char[][] prepareTable(List<Rectangle> rectangles) {

        fillTable();
        fillWithWalkableFields(rectangles);
        return board;
    }

    private void fillTable() {

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = 'O';
            }
        }
    }

    private void fillWithWalkableFields(List<Rectangle> rectangles) {

        for (Rectangle shape : rectangles) {
            for (int i = (int) shape.getLayoutX(); i < shape.getLayoutX() + shape.getWidth(); i++) {
                for (int j = (int) shape.getLayoutY(); j < shape.getLayoutY() + shape.getHeight(); j++) {
                    board[i][j] = ' ';
                }
            }
        }
    }

}
