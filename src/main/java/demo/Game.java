package demo;

import controler.MovementController;
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

        setPosition(pane);

        MovementController.movement(scene, hostSquare, networkConnection);

        primaryStage.setTitle("Tanks");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private void setPosition(Pane pane){
        hostSquare = (Rectangle) pane.getChildren().get(0);
        clientSquare = (Rectangle) pane.getChildren().get(1);
        if(mode.equals(Mode.SERVER)) {
            hostSquare.setTranslateX(50); hostSquare.setTranslateY(50);
            clientSquare.setTranslateX(250); clientSquare.setTranslateY(250);
        } else {
            hostSquare.setTranslateX(250); hostSquare.setTranslateY(250);
            clientSquare.setTranslateX(50); clientSquare.setTranslateY(50);
        }
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
