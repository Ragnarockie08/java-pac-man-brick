package modes;

import demo.Game;


public class Server extends NetworkConnection {

    private int port;

    public Server(int port, Game game) {
        super(game);
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
