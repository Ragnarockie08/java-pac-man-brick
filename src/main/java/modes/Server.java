package modes;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;


public class Server extends NetworkConnection {

    private int port;

    public Server(int port, Node player) {
        super(player);
        this.port = port;
    }


    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
