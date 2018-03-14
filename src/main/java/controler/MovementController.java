package controler;

import demo.Game;
import helper.Direction;
import helper.WalkableBoard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.Player;
import modes.NetworkConnection;


public class MovementController {

    private final int STEP = 8;
    private final int PLAYER_SIZE = 30;

    private Direction direction;
    private boolean moved = false;
    private Timeline timeline = new Timeline();

    private char[][] walkableBoard;
    private Game game;

    public MovementController(Game game) {

        this.game = game;
        direction = Direction.UP;
    }

    public void movement(Scene scene, Pane hostSquare, NetworkConnection networkConnection, Pane pane) {
        handleMovement(scene, hostSquare, networkConnection, pane);
        timeline.play();

    }

    private void handleMovement(Scene scene, Pane hostSquare, NetworkConnection networkConnection, Pane pane) {

        WalkableBoard board = new WalkableBoard();
        walkableBoard = board.prepareTable(game.getWalls());


        scene.setOnKeyPressed(event -> {
            if (moved) {
                int x = (int) hostSquare.getTranslateX();
                int y = (int) hostSquare.getTranslateY();
                switch (event.getCode()) {

                    case W:
                        if (isAbleToMoveUp(hostSquare, x, y)) {
                            direction = Direction.UP;
                            game.getPlayer().setDirection(Direction.UP);
                        }
                        break;
                    case S:
                        if (isAbleToMoveDown(hostSquare, x, y)) {
                            direction = Direction.DOWN;
                            game.getPlayer().setDirection(Direction.DOWN);
                        }
                        break;
                    case A:
                        if (isAbleToMoveLeft(hostSquare, x, y)) {
                            direction = Direction.LEFT;
                            game.getPlayer().setDirection(Direction.LEFT);
                        }
                        break;
                    case D:
                        if (isAbleToMoveRight(hostSquare, x, y)) {
                            direction = Direction.RIGHT;
                            game.getPlayer().setDirection(Direction.RIGHT);
                        }
                        break;
                }
            }
        });
        KeyFrame frame = new KeyFrame(Duration.seconds(0.05), event -> {
            if (networkConnection.isConnected()) {

                try {
                    int x = (int) hostSquare.getTranslateX();
                    int y = (int) hostSquare.getTranslateY();

                    switch (direction) {
                        case UP:
                            checkMoveUp(hostSquare, x, y);
                            break;
                        case DOWN:
                            checkMoveDown(hostSquare, x, y);
                            break;
                        case LEFT:
                            checkMoveLeft(hostSquare, x, y);
                            break;
                        case RIGHT:
                            checkMoveRight(hostSquare, x, y);
                            break;
                    }
                    moved = true;
                    handleSend(networkConnection);

                } catch (Exception e) {
                    System.out.println("impossible to send");
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void checkMoveUp(Pane player, int x, int y) {
        if (isAbleToMoveUp(player, x, y)) {
            player.setTranslateY(player.getTranslateY() - STEP);
            game.getPlayer().setDirection(Direction.UP);
        }
    }

    private boolean isAbleToMoveUp(Pane player, int x, int y) {
        return player.getTranslateY() > STEP
                && walkableBoard[x][y - STEP] == 'O' && walkableBoard[x + PLAYER_SIZE][y - STEP] == 'O';
    }

    private void checkMoveDown(Pane player, int x, int y) {
        if (isAbleToMoveDown(player, x, y)) {
            player.setTranslateY(player.getTranslateY() + STEP);
            game.getPlayer().setDirection(Direction.DOWN);
        }
    }

    private boolean isAbleToMoveDown(Pane player, int x, int y) {
        return player.getTranslateY() < Game.HEIGHT - 40
                && walkableBoard[x][y + STEP + PLAYER_SIZE] == 'O'
                && walkableBoard[x + PLAYER_SIZE][y + STEP + PLAYER_SIZE] == 'O';
    }

    private void checkMoveLeft(Pane player, int x, int y) {
        if (isAbleToMoveLeft(player, x, y)) {
            player.setTranslateX(player.getTranslateX() - STEP);
            game.getPlayer().setDirection(Direction.LEFT);
        }
    }

    private boolean isAbleToMoveLeft(Pane player, int x, int y) {
        return player.getTranslateX() > STEP
                && walkableBoard[x - STEP][y] == 'O' && walkableBoard[x - STEP][y + PLAYER_SIZE] == 'O';
    }

    private void checkMoveRight(Pane player, int x, int y) {
        if (isAbleToMoveRight(player, x, y)) {
            player.setTranslateX(player.getTranslateX() + STEP);
            game.getPlayer().setDirection(Direction.RIGHT);
        }
    }

    private boolean isAbleToMoveRight(Pane player, int x, int y) {
        return player.getTranslateX() < Game.WIDTH - 40
                && walkableBoard[x + STEP + PLAYER_SIZE][y] == 'O'
                && walkableBoard[x + STEP + PLAYER_SIZE][y + PLAYER_SIZE] == 'O';
    }

    private void handleSend(NetworkConnection networkConnection) throws Exception {

        double coordinateX = game.getHostPlayer().getTranslateX();
        double coordinateY = game.getHostPlayer().getTranslateY();

        game.getPlayer().setxCoordinate(coordinateX);
        game.getPlayer().setyCoordinate(coordinateY);

        networkConnection.send(new Player(game.getPlayer()));
    }
}
