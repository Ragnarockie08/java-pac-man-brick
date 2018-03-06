package demo;

import helper.Mode;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.scene.shape.Rectangle;
import model.Player;
import modes.Client;
import modes.NetworkConnection;
import modes.Server;

import java.io.IOException;

public class Game extends Application {

    private static NetworkConnection networkConnection;
    private static Mode mode;

    private static final int BLOCK_SIZE = 40;
    private static final int WIDTH = 15 * BLOCK_SIZE;
    private static final int HEIGHT = 15 * BLOCK_SIZE;

    private static Pane root;

    private static Rectangle hostSquare = new Rectangle(20, 20);
    private static Rectangle clientSquare = new Rectangle(20, 20);
    private static Player player = new Player();

    @Override
    public void init() throws Exception {
        networkConnection.startConnection();
    }

    public void start(Stage primaryStage) throws IOException {

        root = FXMLLoader.load(getClass().getResource("/test.fxml"));

        Scene scene = new Scene(root);
        Pane pane = (Pane) root.lookup("#scene");
        pane.getChildren().addAll(hostSquare, clientSquare);

        setSquares(pane);

        final double rectangleSpeed = 100 ; // pixels per second
        final DoubleProperty rectangleVelocityX = new SimpleDoubleProperty();
        final DoubleProperty rectangleVelocityY = new SimpleDoubleProperty();
        final LongProperty lastUpdateTime = new SimpleLongProperty();
        final AnimationTimer rectangleAnimation = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (lastUpdateTime.get() > 0) {
                    final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;

                    final double deltaX = elapsedSeconds * rectangleVelocityX.get();
                    final double oldX = hostSquare.getTranslateX();
                    final double newX = Math.max(0, Math.min(WIDTH, oldX + deltaX));

                    final double deltaY = elapsedSeconds * rectangleVelocityY.get();
                    final double oldY = hostSquare.getTranslateY();
                    final double newY = Math.max(0, Math.min(HEIGHT, oldY + deltaY));

                    hostSquare.setTranslateX(newX);
                    hostSquare.setTranslateY(newY);
                }
                lastUpdateTime.set(timestamp);
            }
        };
        rectangleAnimation.start();

        try {
            scene.setOnKeyPressed((KeyEvent event) ->
            {
                if (event.getCode() == KeyCode.D) {
                    rectangleVelocityX.set(rectangleSpeed);
                } else if (event.getCode() == KeyCode.A) {
                    rectangleVelocityX.set(-rectangleSpeed);
                } else if (event.getCode() == KeyCode.W) {
                    rectangleVelocityY.set(-rectangleSpeed);
                } else if (event.getCode() == KeyCode.S) {
                    rectangleVelocityY.set(rectangleSpeed);
                }

                try {
                    networkConnection.send(new Player(hostSquare.getTranslateX(), hostSquare.getTranslateY()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            scene.setOnKeyReleased((KeyEvent event) -> {

                if (isAnyKeyReleased(event)) {
                    rectangleVelocityX.set(0);
                    rectangleVelocityY.set(0);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        showPreparedStage(primaryStage, scene);
    }

    private void setSquares(Pane pane) {
        hostSquare = (Rectangle) pane.getChildren().get(0);
        clientSquare = (Rectangle) pane.getChildren().get(1);

        int initCoord1 = 50;
        int initCoord2 = 250;

        if(mode.equals(Mode.SERVER)) {
            hostSquare.setTranslateX(initCoord1); hostSquare.setTranslateY(initCoord1);
            clientSquare.setTranslateX(initCoord2); clientSquare.setTranslateY(initCoord2);

        } else {
            hostSquare.setTranslateX(initCoord2); hostSquare.setTranslateY(initCoord2);
            clientSquare.setTranslateX(initCoord1); clientSquare.setTranslateY(initCoord1);
        }
    }

    private boolean isAnyKeyReleased(KeyEvent event) {
        return (event.getCode() == KeyCode.D || event.getCode() == KeyCode.A
                || event.getCode() == KeyCode.W || event.getCode() == KeyCode.S);
    }

    private void showPreparedStage(Stage stage, Scene scene) {
        stage.setTitle("Tanks");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {

        Server server;
        Client client;

        mode = Mode.getInstance(args[0]);

        if (mode.equals(Mode.SERVER)) {
            server = new Server(Integer.parseInt(args[1]), player);
            networkConnection = server;

        } else if (mode.equals(Mode.CLIENT)) {
            client = new Client(args[1], Integer.parseInt(args[2]), player);
            networkConnection = client;
        }

        launch(args);
    }

    public static Rectangle getHostSquare() {
        return hostSquare;
    }

    public static Rectangle getClientSquare() {
        return clientSquare;
    }
}
