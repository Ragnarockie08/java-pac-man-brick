package controler;

import demo.Game;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import model.Player;
import modes.NetworkConnection;

public class MovementController {


    private static final int SIZE = 20;

    public void movement(Scene scene, Circle hostPlayer, NetworkConnection networkConnection) {

        scene.setOnKeyPressed(event -> {

            try {
                switch (event.getCode()) {

                    case W:
                        if (!(hostPlayer.getTranslateY() <= SIZE)) {
                            hostPlayer.setTranslateY(hostPlayer.getTranslateY() - SIZE);
                        }
                        break;
                    case S:
                        if (!(hostPlayer.getTranslateY() >= Game.WIDTH - SIZE)) {
                            hostPlayer.setTranslateY(hostPlayer.getTranslateY() + SIZE);
                        }
                        break;
                    case A:
                        if (!(hostPlayer.getTranslateX() <= SIZE)) {
                            hostPlayer.setTranslateX(hostPlayer.getTranslateX() - SIZE);
                        }
                        break;
                    case D:
                        if (!(hostPlayer.getTranslateX() >= Game.HEIGHT - SIZE)) {
                            hostPlayer.setTranslateX(hostPlayer.getTranslateX() + SIZE);
                        }
                        break;
                }
                networkConnection.send(new Player(hostPlayer.getTranslateX(), hostPlayer.getTranslateY()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
