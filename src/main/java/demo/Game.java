package demo;

import controler.MovementController;
import helper.Mode;
import javafx.application.Application;
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
    public static final int WIDTH = 15 * BLOCK_SIZE;
    public static final int HEIGHT = 15 * BLOCK_SIZE;

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

        MovementController.movement(scene, hostSquare, clientSquare, networkConnection);

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
