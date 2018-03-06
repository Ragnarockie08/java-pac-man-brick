package modes;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class NetworkConnection {

    private Player player;

    private ConnectionThread connectionThread = new ConnectionThread();

    public NetworkConnection(Player player) {
        this.player = player;
    }

    public void startConnection() throws Exception {
        connectionThread.start();
    }

    public void send(Serializable data) throws Exception {
        connectionThread.outputStream.writeObject(data);
    }

    public void closeConnection() throws Exception {
        connectionThread.socket.close();
    }

    protected abstract boolean isServer();

    protected abstract String getIP();

    protected abstract int getPort();

    private class ConnectionThread extends Thread {

        private Socket socket;
        private ObjectOutputStream outputStream;



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
                    player = (Player) in.readObject();
                    System.out.println("X :" + player.getxCoordinate());
                    System.out.println("Y :" + player.getyCoordinate());

                }

            } catch (IOException e){

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}












































