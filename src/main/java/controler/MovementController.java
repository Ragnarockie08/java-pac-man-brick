package controler;

import demo.Game;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import model.Player;
import modes.NetworkConnection;

public class MovementController {

    private static final int STEP = 10;
    private Game game;

    public MovementController(Game game){
        this.game = game;
    }

    public void movement(Scene scene, Shape hostSquare, Shape clientSquare, NetworkConnection networkConnection) {

        scene.setOnKeyPressed(event -> {
            if (networkConnection.isConnected()) {

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

    private void handleSend(NetworkConnection networkConnection) throws Exception{

        double coordinateX = game.getHostPlayer().getTranslateX();
        double coordinateY = game.getHostPlayer().getTranslateY();

        game.getPlayer().setxCoordinate(coordinateX);
        game.getPlayer().setyCoordinate(coordinateY);


        networkConnection.send(new Player(game.getPlayer()));
    }

}
