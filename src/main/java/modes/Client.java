package modes;

import demo.Game;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import model.Player;

import java.awt.*;

public class Client extends NetworkConnection {

    private String ip;
    private int port;

    public Client(String ip, int port, Game game) {
        super(game);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}