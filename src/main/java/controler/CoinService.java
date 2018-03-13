package controler;

import demo.Game;
import helper.Mode;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import model.Player;

public class CoinService {


    private Game game;
    private Pane pane;

    public CoinService(Game game, Pane pane) {
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
                    removeCoin(game.getPlayer());
                    handleClientCoinRemove();
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
            if (coordinateX == coinCoordinateX && coordinateY == coinCoordinateY){
                toRemove = coin;
                pane.getChildren().remove(coin);
                break;
            }
        }
        game.getCoins().remove(toRemove);
    }

    private void handleClientCoinRemove() {
        pane.getChildren().removeAll(game.getCoinsToRemove());
        game.getCoins().removeAll(game.getCoinsToRemove());
        game.getCoinsToRemove().clear();
    }

    private void removeCoin(Player player){

        double coordinateX = player.getxCoordinate();
        double coordinateY = player.getyCoordinate();

        for (Circle coin: game.getCoins()){

            double coinCoordinateX = coin.getLayoutX() + coin.getCenterX() - 15;
            double coinCoordinateY = coin.getLayoutY() + coin.getCenterY() - 15;

            if (coordinateX == coinCoordinateX && coordinateY == coinCoordinateY){
                game.getCoinsToRemove().add(coin);
            }
        }
    }
}
