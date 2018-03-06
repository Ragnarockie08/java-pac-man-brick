package demo;

import helper.Mode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

        hostSquare = (Rectangle) pane.getChildren().get(0);
        hostSquare.setX(50); hostSquare.setY(50);

        clientSquare = (Rectangle) pane.getChildren().get(1);
        clientSquare.setX(250); clientSquare.setY(250);

        player = new Player(hostSquare.getTranslateX(), hostSquare.getTranslateY());

        scene.setOnKeyPressed(event -> {

            try {
                switch (event.getCode()) {

                    case W:
                        if (!(hostSquare.getTranslateY() <= 0)) {
                            hostSquare.setTranslateY(hostSquare.getTranslateY() - 40);
                        }
                        break;
                    case S:
                        if (!(hostSquare.getTranslateY() >= WIDTH)) {
                            hostSquare.setTranslateY(hostSquare.getTranslateY() + 40);
                        }
                        break;
                    case A:
                        if (!(hostSquare.getTranslateX() <= 0)) {
                            hostSquare.setTranslateX(hostSquare.getTranslateX() - 40);
                        }
                        break;
                    case D:
                        if (!(hostSquare.getTranslateX() >= HEIGHT)) {
                            hostSquare.setTranslateX(hostSquare.getTranslateX() + 40);
                        }
                        break;
                }
                networkConnection.send(new Player(hostSquare.getTranslateX(), hostSquare.getTranslateY()));

            } catch (Exception e){
                e.printStackTrace();
            }
        });

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

    public static Rectangle getHostSquare() {
        return hostSquare;
    }

    public static Rectangle getClientSquare() {
        return clientSquare;
    }
}
