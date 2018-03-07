package controler;

import demo.Game;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import model.Player;
import modes.NetworkConnection;

public class MovementController {

    public static void movement(Scene scene, Rectangle hostSquare, NetworkConnection networkConnection) {


        Player player = new Player(hostSquare.getTranslateX(), hostSquare.getTranslateY());

        scene.setOnKeyPressed(event -> {

            try {
                switch (event.getCode()) {

                    case W:
                        if (!(hostSquare.getTranslateY() <= 0)) {
                            hostSquare.setTranslateY(hostSquare.getTranslateY() - 20);
                        }
                        break;
                    case S:
                        if (!(hostSquare.getTranslateY() >= Game.WIDTH)) {
                            hostSquare.setTranslateY(hostSquare.getTranslateY() + 20);
                        }
                        break;
                    case A:
                        if (!(hostSquare.getTranslateX() <= 0)) {
                            hostSquare.setTranslateX(hostSquare.getTranslateX() - 20);
                        }
                        break;
                    case D:
                        if (!(hostSquare.getTranslateX() >= Game.HEIGHT)) {
                            hostSquare.setTranslateX(hostSquare.getTranslateX() + 20);
                        }
                        break;
                }
                networkConnection.send(new Player(hostSquare.getTranslateX(), hostSquare.getTranslateY()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
