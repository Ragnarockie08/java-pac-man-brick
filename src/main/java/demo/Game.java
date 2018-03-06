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

    private Rectangle square = new Rectangle(20, 20);
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
        scene.setOnKeyPressed(event -> {

            try {

                switch (event.getCode()) {

                    case W:
                        if (!(square.getTranslateY() <= 0)) {
                            square.setTranslateY(square.getTranslateY() - 40);
                            player.setyCoordinate(square.getTranslateY());
                        }
                        break;
                    case S:
                        if (!(square.getTranslateY() >= WIDTH)) {
                            square.setTranslateY(square.getTranslateY() + 40);
                            player.setyCoordinate(square.getTranslateY());
                        }
                        break;
                    case A:
                        if (!(square.getTranslateX() <= 0)) {
                            square.setTranslateX(square.getTranslateX() - 40);
                            player.setxCoordinate(square.getTranslateX());

                        }
                        break;
                    case D:
                        if (!(square.getTranslateX() >= HEIGHT)) {
                            square.setTranslateX(square.getTranslateX() + 40);
                            player.setxCoordinate(square.getTranslateX());
                        }
                        break;
                }
                networkConnection.send(player);
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

    public Rectangle getSquare() {
        return square;
    }
}
