package demo;

import helper.Mode;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
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
import sun.plugin2.message.Message;

import java.io.IOException;

public class Game extends Application {

    private static NetworkConnection networkConnection;
    private static Mode mode;

    private static final int BLOCK_SIZE = 40;
    private static final int WIDTH = 15 * BLOCK_SIZE;
    private static final int HEIGHT = 15 * BLOCK_SIZE;

    private static Pane root;

    private static Rectangle square = new Rectangle(20, 20);
    private static Player player = new Player();

    @Override
    public void init() throws Exception {
        networkConnection.startConnection();
    }

    public void start(Stage primaryStage) throws IOException {

        root = FXMLLoader.load(getClass().getResource("/test.fxml"));

        Scene scene = new Scene(root);
        Pane pane = (Pane) root.lookup("#scene");
        pane.getChildren().add(square);
        player = new Player(square.getTranslateX(), square.getTranslateY());

        final double rectangleSpeed = 100 ; // pixels per second
        final double minX = 0, minY = 0;
        final double maxX = WIDTH, maxY = HEIGHT ; // whatever the max value should be.. can use a property and bind to scene width if needed...
        final DoubleProperty rectangleVelocityX = new SimpleDoubleProperty();
        final DoubleProperty rectangleVelocityY = new SimpleDoubleProperty();
        final LongProperty lastUpdateTime = new SimpleLongProperty();
        final AnimationTimer rectangleAnimation = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (lastUpdateTime.get() > 0) {
                    final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0 ;
                    final double deltaX = elapsedSeconds * rectangleVelocityX.get();
                    final double oldX = square.getTranslateX();
                    final double newX = Math.max(minX, Math.min(maxX, oldX + deltaX));
                    final double deltaY = elapsedSeconds * rectangleVelocityY.get();
                    final double oldY = square.getTranslateY();
                    final double newY = Math.max(minY, Math.min(maxY, oldY + deltaY));
                    square.setTranslateX(newX);
                    square.setTranslateY(newY);
                }
                lastUpdateTime.set(timestamp);
            }
        };
        rectangleAnimation.start();

        try {

            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode()== KeyCode.D) { // don't use toString here!!!
                        rectangleVelocityX.set(rectangleSpeed);
                    } else if (event.getCode() == KeyCode.A) {
                        rectangleVelocityX.set(-rectangleSpeed);
                    } else if (event.getCode() == KeyCode.W) {
                        rectangleVelocityY.set(-rectangleSpeed);
                    } else if (event.getCode() == KeyCode.S) {
                        rectangleVelocityY.set(rectangleSpeed);
                    }
                    System.out.println(rectangleVelocityX);
                    System.out.println(rectangleVelocityY);
                }
            });

            scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.A
                            || event.getCode() == KeyCode.W || event.getCode() == KeyCode.S) {
                        rectangleVelocityX.set(0);
                        rectangleVelocityY.set(0);
                    }
                }
            });
            networkConnection.send(new Player(square.getTranslateX(), square.getTranslateY()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
/*
        scene.setOnKeyPressed(event -> {

            try {
                switch (event.getCode()) {

                    case W:
                        if (!(square.getTranslateY() <= 0)) {
                            square.setTranslateY(square.getTranslateY() - 40);
                        }
                        break;
                    case S:
                        if (!(square.getTranslateY() >= WIDTH)) {
                            square.setTranslateY(square.getTranslateY() + 40);
                        }
                        break;
                    case A:
                        if (!(square.getTranslateX() <= 0)) {
                            square.setTranslateX(square.getTranslateX() - 40);
                        }
                        break;
                    case D:
                        if (!(square.getTranslateX() >= HEIGHT)) {
                            square.setTranslateX(square.getTranslateX() + 40);
                        }
                        break;
                }
                networkConnection.send(new Player(square.getTranslateX(), square.getTranslateY()));

            } catch (Exception e){
                e.printStackTrace();
            }
        });
*/

        primaryStage.setTitle("Tanks");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

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

    public static Rectangle getSquare() {
        return square;
    }




}
