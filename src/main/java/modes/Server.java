package modes;


import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import model.Player;


public class Server extends NetworkConnection {

    private int port;

    public Server(int port, Player player) {
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
