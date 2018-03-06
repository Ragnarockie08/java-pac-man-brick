package modes;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class NetworkConnection {

    private Node player;

    private ConnectionThread connectionThread = new ConnectionThread();

    public NetworkConnection(Node player) {
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
                    Serializable data = (Serializable) in.readObject();


                }

            } catch (IOException e){

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}












































