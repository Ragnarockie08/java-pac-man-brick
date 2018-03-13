package controler;

import demo.Game;
import helper.Mode;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class CoinController {


    private Game game;
    private Pane pane;

    public CoinController(Game game, Pane pane) {
        this.game = game;
        this.pane = pane;
    }

    public void check() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (game.getMode() == Mode.SERVER){
                    handleServerCoinPick();
                } else {
                    handleClientCoinRemove(game.getClientPlayer());
                }
            }
        });
    }

    private void handleServerCoinPick(){

        Circle toRemove = null;
        double coordinateX = game.getHostPlayer().getTranslateX();
        double coordinateY = game.getHostPlayer().getTranslateY();

        for (Circle coin: game.getCoins()){

            double coinCoordinateX = coin.getLayoutX() + coin.getCenterX() - 15;
            double coinCoordinateY = coin.getLayoutY() + coin.getCenterY() - 15;
            if (game.getHostPlayer().getBoundsInParent().contains(coin.getBoundsInParent())){
                toRemove = coin;
                pane.getChildren().remove(coin);
                break;
            }
        }
        game.getCoins().remove(toRemove);
    }

    private void handleClientCoinRemove(Pane pacMan) {

        Circle toRemove = null;

        double coordinateX = pacMan.getTranslateX();
        double coordinateY = pacMan.getTranslateY();

        for (Circle coin: game.getCoins()){

            double coinX = coin.getLayoutX() + coin.getCenterX() - 15;
            double coinY = coin.getLayoutY() + coin.getCenterY() - 15;

            if (pacMan.getBoundsInParent().contains(coin.getBoundsInParent())) {
                toRemove = coin;
            }
        }
        game.getCoins().remove(toRemove);
        pane.getChildren().remove(toRemove);
    }
}
