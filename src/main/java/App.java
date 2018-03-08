import controler.MovementController;
import demo.Game;
import helper.Mode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modes.Client;
import modes.NetworkConnection;
import modes.Server;
import java.io.IOException;


public class App extends Application {

    private static Game game = new Game();
    private MovementController movementController;
    private static NetworkConnection networkConnection;


    @Override
    public void init() throws Exception {
        networkConnection.startConnection();
        movementController = new MovementController(game);
    }

    public void start(Stage primaryStage) throws IOException {

        Pane root = FXMLLoader.load(getClass().getResource("/GameBoard.fxml"));

        Scene scene = new Scene(root);
        Pane pane = (Pane) root.lookup("#scene");
        pane.getChildren().addAll(game.getHostPlayer(), game.getClientPlayer());
        game.createWalls(pane);
        game.setPosition(pane);
        movementController.movement(scene, game.getHostPlayer(), networkConnection);
        showPreparedStage(primaryStage, scene);

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

        Mode mode = Mode.getInstance(args[0]);

        if (mode.equals(Mode.SERVER)) {
            server = new Server(Integer.parseInt(args[1]), game);
            networkConnection = server;

        } else if (mode.equals(Mode.CLIENT)) {
            client = new Client(args[1], Integer.parseInt(args[2]), game);
            networkConnection = client;
        }

        game.setMode(mode);
        launch(args);
    }
}
