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
    private Game game;

    public MovementController(Game game){
        this.game = game;
    }

    public void movement(Scene scene, Shape hostSquare, Shape clientSquare, NetworkConnection networkConnection) {

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

                handleSend(networkConnection);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void checkMoveUp(Shape player, Shape wall) {
        if (player.getTranslateY() > STEP
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() < player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() - STEP);
        }
    }

    private void checkMoveDown(Shape player, Shape wall) {
        if (player.getTranslateY() < Game.HEIGHT - 40
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() > player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() + STEP);
        }
    }

    private void checkMoveLeft(Shape player, Shape wall) {
        if (player.getTranslateX() > STEP
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() < player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() - STEP);
        }
    }

    private void checkMoveRight(Shape player, Shape wall) {
        if (player.getTranslateX() < Game.WIDTH - 40
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() > player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() + STEP);
        }
    }

    private void fillTable() {
        for (int i=0; i<680; i++) {
            for (int j=0; j<680; j++) {
                walkableBoard[i][j] = 'O';
            }
        }
    }

    private void showWalkableBoard() {
        for (int i=0; i<680; i+=10) {
            for (int j=0; j<680; j+=10) {
                System.out.print(walkableBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void fillWithWalkableFields() {


        for (Rectangle shape : game.getWalls()) {
            for (int i = (int) shape.getLayoutX(); i < shape.getLayoutX() + shape.getWidth(); i++) {
                for (int j = (int) shape.getLayoutY(); j < shape.getLayoutY() + shape.getHeight(); j++) {
                    walkableBoard[i][j] = ' ';
                }
            }
        }
    }

    private void handleSend(NetworkConnection networkConnection) throws Exception {

        Player player = new Player(game.getHostPlayer().getTranslateX(), game.getHostPlayer().getTranslateY());

        networkConnection.send(player);
    }

}
