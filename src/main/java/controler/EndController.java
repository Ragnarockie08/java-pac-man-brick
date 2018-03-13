package controler;

import demo.Game;
import helper.Mode;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import modes.NetworkConnection;

import java.io.IOException;

public class EndController {

    private Game game;
    private Pane pane;
    private NetworkConnection networkConnection;

    public EndController(Game game, Pane pane, NetworkConnection networkConnection) {
        this.game = game;
        this.pane = pane;
        this.networkConnection = networkConnection;
    }

    public void checkEnd(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    handleEnd(networkConnection, pane);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void handleEnd(NetworkConnection networkConnection, Pane pane) throws Exception {

        if (game.getCoins().isEmpty()){
            if (game.getMode() == Mode.SERVER){
                handleWin(pane);
            } else {
                handleLose(pane);
            }
            networkConnection.setConnected(false);
        } else if (game.getHostPlayer().getTranslateX() == game.getClientPlayer().getTranslateX()
                && game.getHostPlayer().getTranslateY() == game.getClientPlayer().getTranslateY()){
            if (game.getMode() == Mode.CLIENT){
                handleWin(pane);
            } else {
                handleLose(pane);
            }
            networkConnection.setConnected(false);
        }
        if (!networkConnection.isConnected()){
            endGame(networkConnection);
        }
    }

    private void handleWin(Pane pane) throws IOException {
        StackPane stackPane = (StackPane) pane.getParent();
        Pane victoryPane = FXMLLoader.load(getClass().getResource("/victory.fxml"));
        stackPane.getChildren().add(victoryPane);
    }

    private void handleLose(Pane pane) throws IOException {
        StackPane stackPane = (StackPane) pane.getParent();
        Pane losePane = FXMLLoader.load(getClass().getResource("/lose.fxml"));
        stackPane.getChildren().add(losePane);
    }

    private void endGame(NetworkConnection networkConnection) throws Exception {
        networkConnection.closeConnection();
    }
}
