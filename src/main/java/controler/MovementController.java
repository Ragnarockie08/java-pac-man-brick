package controler;

import demo.Game;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import model.Player;
import modes.NetworkConnection;

public class MovementController {

    private static final int STEP = 20;

    public static void movement(Scene scene, Rectangle hostSquare, Rectangle clientSquare, NetworkConnection networkConnection) {

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

                networkConnection.send(new Player(hostSquare.getTranslateX(), hostSquare.getTranslateY()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void checkMoveUp(Rectangle player, Rectangle wall) {
        if (player.getTranslateY() > 0
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() < player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() - STEP);
        }
    }

    private static void checkMoveDown(Rectangle player, Rectangle wall) {
        if (player.getTranslateY() < Game.HEIGHT - 20
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() > player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() + STEP);
        }
    }

    private static void checkMoveLeft(Rectangle player, Rectangle wall) {
        if (player.getTranslateX() > 0
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() < player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() - STEP);
        }
    }

    private static void checkMoveRight(Rectangle player, Rectangle wall) {
        if (player.getTranslateX() < Game.WIDTH - 20
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() > player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() + STEP);
        }
    }
}
