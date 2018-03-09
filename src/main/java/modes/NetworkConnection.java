package modes;

import demo.Game;
import helper.Mode;
import javafx.scene.shape.Circle;
import helper.Direction;
import model.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;


public abstract class NetworkConnection extends Thread {

    private Socket socket;
    private ServerSocket server;
    private ObjectOutputStream outputStream;
    private Game game;
    private Player player;
    private boolean connected;
    private boolean isRunning;

    public NetworkConnection(Game game){

        this.isRunning = true;
        this.game = game;
        this.connected = false;
        this.isRunning = true;
    }

    public void startConnection() throws Exception {

        start();
    }

    public void send(Serializable data) throws Exception {

        outputStream.writeObject(data);
        outputStream.flush();
    }

    public void closeConnection() throws Exception {

        socket.close();
        isRunning = false;
    }

    protected abstract boolean isServer();

    protected abstract String getIP();

    protected abstract int getPort();

    @Override
    public void run() {

        try {
            setServerAndSocket();

            connected = true;

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            this.outputStream = out;

            while (isRunning) {
                player = (Player) in.readObject();
                moveOponent(player);
                if (game.getMode().equals(Mode.CLIENT)){
                    removeCoin(player);
                }
            }
        } catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setServerAndSocket() throws IOException {
        if (isServer()) {
            server = new ServerSocket(getPort());
            socket = server.accept();
        } else {
            socket = new Socket(getIP(), getPort());
        }
    }

    private void moveOponent(Player player){

        game.getClientPlayer().setTranslateY(player.getyCoordinate());
        game.getClientPlayer().setTranslateX(player.getxCoordinate());
        roundDirection(player);
    }

    private void roundDirection(Player player) {

        if (player.getDirection() == Direction.UP) {

            if(!game.isPacman()) {

                game.getClientPlayer().setRotate(270);
                game.getClientPlayer().setScaleY(1);
            }
        } else if (player.getDirection() == Direction.RIGHT) {

            game.getClientPlayer().setRotate(0);
            game.getClientPlayer().setScaleY(1);

        } else if (player.getDirection() == Direction.DOWN) {

            if(!game.isPacman()) {
                game.getClientPlayer().setRotate(90);
                game.getClientPlayer().setScaleY(1);
            }

        } else if (player.getDirection() == Direction.LEFT) {

            game.getClientPlayer().setRotate(180);
            game.getClientPlayer().setScaleY(-1);
        }
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

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

}