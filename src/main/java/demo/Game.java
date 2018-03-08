package demo;

import helper.Mode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import model.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Mode mode;
    private static final int BLOCK_SIZE = 40;
    public static final int WIDTH = 17 * BLOCK_SIZE;
    public static final int HEIGHT = 17 * BLOCK_SIZE;
    private Shape hostPlayer;
    private Shape clientPlayer;
    private Player player;

    private List<Rectangle> walls;

    public Game(){
        walls = new ArrayList<>();
        hostPlayer = new Rectangle(30, 30);
        clientPlayer = new Rectangle(30, 30);
        player = new Player(hostPlayer.getTranslateX(), hostPlayer.getTranslateY());
    }

    public void setPosition(Pane pane){

        if(mode.equals(Mode.SERVER)) {
            hostPlayer.setFill(Color.YELLOW);
            clientPlayer.setFill(Color.GREEN);
            hostPlayer.setTranslateX(5); hostPlayer.setTranslateY(5);
            clientPlayer.setTranslateX(205); clientPlayer.setTranslateY(205);

        } else {
            clientPlayer.setFill(Color.YELLOW);
            hostPlayer.setFill(Color.GREEN);
            hostPlayer.setTranslateX(205); hostPlayer.setTranslateY(205);
            clientPlayer.setTranslateX(5); clientPlayer.setTranslateY(5);
        }
    }

    public void createWalls(Pane pane){

        for (int i = 0; i < pane.getChildren().size(); i++){
            if(pane.getChildren().get(i) instanceof Rectangle
                    && pane.getChildren().get(i) != hostPlayer
                    && pane.getChildren().get(i) != clientPlayer) {

                walls.add((Rectangle) pane.getChildren().get(i));
            }
        }
    }

    public Shape getHostPlayer() {
        return hostPlayer;
    }

    public Shape getClientPlayer() {
        return clientPlayer;
    }

    public List<Rectangle> getWalls() {
        return walls;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Player getPlayer() {
        return player;
    }
}
