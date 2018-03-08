package modes;

import demo.Game;
import model.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;


public abstract class NetworkConnection extends Thread {

    private Socket socket;
    private ObjectOutputStream outputStream;
    private Game game;
    private Player player;
    private boolean connected;

    public NetworkConnection(Game game){

        this.game = game;
        this.connected = false;
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
    }

    protected abstract boolean isServer();

    protected abstract String getIP();

    protected abstract int getPort();


    @Override
    public void run() {

        try {

            ServerSocket server;
            Socket socket;
            if (isServer()) {
                server = new ServerSocket(getPort());
                socket = server.accept();
            } else {
                socket = new Socket(getIP(), getPort());
            }
            connected = true;

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            this.socket = socket;
            this.outputStream = out;

            while (true) {
                player = (Player) in.readObject();
                moveOponent(player);
            }

        } catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void moveOponent(Player player){

        game.getClientPlayer().setTranslateY(player.getyCoordinate());
        game.getClientPlayer().setTranslateX(player.getxCoordinate());
        roundDirection(player);
    }

    private void roundDirection(Player player) {

        if (player.getDirection().equals("NORTH")) {
            if(!game.isPacman()) {

                game.getClientPlayer().setRotate(270);
                game.getClientPlayer().setScaleY(1);
            }
        } else if (player.getDirection().equals("EAST")) {
            game.getClientPlayer().setRotate(0);
            game.getClientPlayer().setScaleY(1);
        } else if (player.getDirection().equals("SOUTH")) {
            if(!game.isPacman()) {

                game.getClientPlayer().setRotate(90);
                game.getClientPlayer().setScaleY(1);
            }
        } else if (player.getDirection().equals("WEST")) {
            game.getClientPlayer().setRotate(180);
            game.getClientPlayer().setScaleY(-1);
        }
    }

    public boolean isConnected() {
        return connected;
    }
}












































