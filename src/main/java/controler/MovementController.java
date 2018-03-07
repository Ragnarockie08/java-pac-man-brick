package controler;

import demo.Game;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import model.Player;
import modes.NetworkConnection;

public class MovementController {

    private static final int STEP = 10;
    private static char[][] walkableBoard = new char[680][680];

    public static void movement(Scene scene, Shape hostSquare, Shape clientSquare, NetworkConnection networkConnection) {

        fillTable();
        showWalkableBoard();
        fillWithWalkableFields();
        System.out.println();
        showWalkableBoard();
        scene.setOnKeyPressed(event -> {

            try {
                switch (event.getCode()) {

                    case W:
                        checkMoveUp(hostSquare, clientSquare);
                        break;
                    case S:
                        checkMoveDown(hostSquare, clientSquare);
                        break;
                    case A:
                        checkMoveLeft(hostSquare, clientSquare);
                        break;
                    case D:
                        checkMoveRight(hostSquare, clientSquare);
                        break;
                }

                System.out.println( (int) hostSquare.getTranslateX() + " : " + (int) hostSquare.getTranslateY() + "\n");

                networkConnection.send(new Player(hostSquare.getTranslateX(), hostSquare.getTranslateY()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void checkMoveUp(Shape player, Shape wall) {
        if (player.getTranslateY() > STEP
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() < player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() - STEP);
        }
    }

    private static void checkMoveDown(Shape player, Shape wall) {
        if (player.getTranslateY() < Game.HEIGHT - 40
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() > player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() + STEP);
        }
    }

    private static void checkMoveLeft(Shape player, Shape wall) {
        if (player.getTranslateX() > STEP
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() < player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() - STEP);
        }
    }

    private static void checkMoveRight(Shape player, Shape wall) {
        if (player.getTranslateX() < Game.WIDTH - 40
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() > player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() + STEP);
        }
    }

    private static void fillTable() {
        for (int i=0; i<680; i++) {
            for (int j=0; j<680; j++) {
                walkableBoard[i][j] = 'O';
            }
        }
    }

    private static void showWalkableBoard() {
        for (int i=0; i<680; i+=10) {
            for (int j=0; j<680; j+=10) {
                System.out.print(walkableBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void fillWithWalkableFields() {


        for (Rectangle shape : Game.getWalls()) {
            for (int i=(int)shape.getLayoutX(); i<shape.getLayoutX() + shape.getWidth(); i++) {
                for (int j=(int)shape.getLayoutY(); j<shape.getLayoutY() + shape.getHeight(); j++) {
                    walkableBoard[i][j] = ' ';
                }
            }
        }
    }

}
