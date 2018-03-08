package model;

import java.io.Serializable;

public class Player implements Serializable {


    private double xCoordinate;
    private double yCoordinate;
    private String direction;

    public Player(Player player) {
        this.xCoordinate = player.getxCoordinate();
        this.yCoordinate = player.getyCoordinate();
        this.direction = player.getDirection();
    }

    public Player(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
