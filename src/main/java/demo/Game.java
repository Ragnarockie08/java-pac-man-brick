package demo;

import controler.MovementController;
import helper.Mode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import modes.Client;
import modes.NetworkConnection;
import modes.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends Application {

    private static NetworkConnection networkConnection;
    private static Mode mode;
    private static final int BLOCK_SIZE = 40;
    public static final int WIDTH = 17 * BLOCK_SIZE;
    public static final int HEIGHT = 17 * BLOCK_SIZE;
    private static Shape hostPlayer = new Rectangle(30, 30);
    private static Shape clientPlayer = new Rectangle(30, 30);
    private MovementController movementController;
    private List<Rectangle> walls;

    @Override
    public void init() throws Exception {
        networkConnection.startConnection();
        movementController = new MovementController();
        walls = new ArrayList<>();
    }

    public void start(Stage primaryStage) throws IOException {

        Pane root = FXMLLoader.load(getClass().getResource("/GameBoard.fxml"));

        Scene scene = new Scene(root);
        Pane pane = (Pane) root.lookup("#scene");
        pane.getChildren().addAll(hostPlayer, clientPlayer);
        getWalls(pane);
        setPosition(pane);
        movementController.movement(scene, hostPlayer, clientPlayer, networkConnection);
        showPreparedStage(primaryStage, scene);

    }

    private void showPreparedStage(Stage stage, Scene scene) {
        stage.setTitle("Tanks");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void setPosition(Pane pane){

        hostPlayer = (Rectangle) pane.getChildren().get(30);
        clientPlayer = (Rectangle) pane.getChildren().get(31);

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

    private void getWalls(Pane pane){

        for (int i = 0; i < pane.getChildren().size() - 2; i++){
            walls.add((Rectangle) pane.getChildren().get(i));
        }
    }


    public static void main(String[] args) {

        Server server;
        Client client;

        mode = Mode.getInstance(args[0]);

        if (mode.equals(Mode.SERVER)) {
            server = new Server(Integer.parseInt(args[1]));
            networkConnection = server;

        } else if (mode.equals(Mode.CLIENT)) {
            client = new Client(args[1], Integer.parseInt(args[2]));
            networkConnection = client;
        }
        launch(args);
    }

    public static Shape getHostPlayer() {
        return hostPlayer;
    }

    public static Shape getClientPlayer() {
        return clientPlayer;
    }

    public List<Rectangle> getWalls() {
        return walls;
    }
}
