package controler;

import demo.Game;
import helper.Mode;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Player;
import modes.NetworkConnection;

public class MovementController {

    private final int STEP = 8;
    private final int PLAYER_SIZE = 30;
    private char[][] walkableBoard;
    private Game game;

    public MovementController(Game game){
        this.game = game;
        this.walkableBoard = new char[680][680];
    }

    public void movement(Scene scene, Pane hostSquare, NetworkConnection networkConnection, Pane pane) {

        prepareTable();
        scene.setOnKeyPressed(event -> {
            if (networkConnection.isConnected()){
                try {
                    int x = (int) hostSquare.getTranslateX();
                    int y = (int) hostSquare.getTranslateY();

                    switch (event.getCode()) {

                        case W:
                            checkMoveUp(hostSquare, x, y);
                            break;
                        case S:
                            checkMoveDown(hostSquare, x, y);
                            break;
                        case A:
                            checkMoveLeft(hostSquare, x, y);
                            break;
                        case D:
                            checkMoveRight(hostSquare, x, y);
                            break;
                    }
                    handleCoins(pane);
                    handleSend(networkConnection);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkMoveUp(Pane player, int x, int y) {
        if (player.getTranslateY() > STEP && isAbleToMoveUp(x, y)) {
            player.setTranslateY(player.getTranslateY() - STEP);
        }
    }

    private boolean isAbleToMoveUp(int x, int y) {
        return walkableBoard[x][y - STEP] == 'O' && walkableBoard[x + PLAYER_SIZE][y - STEP] == 'O';
    }

    private void checkMoveDown(Pane player, int x, int y) {
        if (player.getTranslateY() < Game.HEIGHT - 40 && isAbleToMoveDown(x, y)) {
            player.setTranslateY(player.getTranslateY() + STEP);
        }
    }

    private boolean isAbleToMoveDown(int x, int y) {
        return walkableBoard[x][y + STEP + PLAYER_SIZE] == 'O' && walkableBoard[x + PLAYER_SIZE][y + STEP + PLAYER_SIZE] == 'O';
    }

    private void checkMoveLeft(Pane player, int x, int y) {
        if (player.getTranslateX() > STEP && isAbleToMoveLeft(x, y)) {
            player.setTranslateX(player.getTranslateX() - STEP);
        }
    }

    private boolean isAbleToMoveLeft(int x, int y) {
        return walkableBoard[x - STEP][y] == 'O' && walkableBoard[x -STEP][y + PLAYER_SIZE] == 'O';
    }

    private void checkMoveRight(Pane player, int x, int y) {
        if (player.getTranslateX() < Game.WIDTH  && isAbleToMoveRight(x, y)) {
            player.setTranslateX(player.getTranslateX() + STEP);
        }
    }

    private boolean isAbleToMoveRight(int x, int y) {
        return walkableBoard[x + STEP + PLAYER_SIZE][y] == 'O' && walkableBoard[x + STEP + PLAYER_SIZE][y + PLAYER_SIZE] == 'O';
    }

    private void prepareTable() {
        fillTable();
        fillWithWalkableFields();
    }

    private void fillTable() {
        for (int i=0; i<680; i++) {
            for (int j=0; j<680; j++) {
                walkableBoard[i][j] = 'O';
            }
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

    private void handleCoins(Pane pane){
        if (game.getMode().equals(Mode.SERVER)){
            handleServerCoinPick(pane);
        } else {
            handleClientCoinRemove(pane);
        }
    }

    private void handleSend(NetworkConnection networkConnection) throws Exception {

        double coordinateX = game.getHostPlayer().getTranslateX();
        double coordinateY = game.getHostPlayer().getTranslateY();

        game.getPlayer().setxCoordinate(coordinateX);
        game.getPlayer().setyCoordinate(coordinateY);

        networkConnection.send(new Player(game.getPlayer()));
    }

    private void handleServerCoinPick(Pane pane){

        Circle toRemove = null;
        double coordinateX = game.getHostPlayer().getTranslateX();
        double coordinateY = game.getHostPlayer().getTranslateY();

        for (Circle coin: game.getCoins()){

            double coinCoordinateX = coin.getLayoutX() + coin.getCenterX() - 15;
            double coinCoordinateY = coin.getLayoutY() + coin.getCenterY() - 15;
            if (coordinateX == coinCoordinateX && coordinateY == coinCoordinateY){
                toRemove = coin;
                pane.getChildren().remove(coin);
                break;
            }
        }
        game.getCoins().remove(toRemove);
    }

    private void handleClientCoinRemove(Pane pane) {
        pane.getChildren().removeAll(game.getCoinsToRemove());
        game.getCoins().removeAll(game.getCoinsToRemove());
        game.getCoinsToRemove().clear();
    }


}
