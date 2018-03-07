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

    public NetworkConnection(){
    }

    public void startConnection() throws Exception {
        start();
    }

    public void send(Serializable data) throws Exception {
        outputStream.writeObject(data);
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

            ServerSocket server = null;
            Socket socket;
            if (isServer()) {
                server = new ServerSocket(getPort());
                socket = server.accept();
            } else {
                socket = new Socket(getIP(), getPort());
            }

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            this.socket = socket;
            this.outputStream = out;

            while (true) {
                Player player = (Player) in.readObject();
                Game.getClientPlayer().setTranslateY(player.getyCoordinate());
                Game.getClientPlayer().setTranslateX(player.getxCoordinate());
            }

        } catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}












































