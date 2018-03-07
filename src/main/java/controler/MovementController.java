package controler;

import demo.Game;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import model.Player;
import modes.NetworkConnection;

public class MovementController {

    private static final int STEP = 20;

    public static void movement(Scene scene, Shape hostSquare, Shape clientSquare, NetworkConnection networkConnection) {

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

    private static void checkMoveUp(Shape player, Shape wall) {
        if (player.getTranslateY() > 0
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() < player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() - STEP);
        }
    }

    private static void checkMoveDown(Shape player, Shape wall) {
        if (player.getTranslateY() < Game.HEIGHT - 20
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateX() == player.getTranslateX()
                     && wall.getTranslateY() > player.getTranslateY())) {
            player.setTranslateY(player.getTranslateY() + STEP);
        }
    }

    private static void checkMoveLeft(Shape player, Shape wall) {
        if (player.getTranslateX() > 0
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() < player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() - STEP);
        }
    }

    private static void checkMoveRight(Shape player, Shape wall) {
        if (player.getTranslateX() < Game.WIDTH - 20
                && !(player.getBoundsInParent().intersects(wall.getBoundsInParent())
                     && wall.getTranslateY() == player.getTranslateY()
                     && wall.getTranslateX() > player.getTranslateX())) {
            player.setTranslateX(player.getTranslateX() + STEP);
        }
    }
}
