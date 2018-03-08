package model;

import helper.Direction;

import java.io.Serializable;

public class Player implements Serializable {

    Direction direction;
    private double xCoordinate;
    private double yCoordinate;

    public Player(Player player) {
        this.xCoordinate = player.getxCoordinate();
        this.yCoordinate = player.getyCoordinate();
        this.direction = Direction.UP;
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

}
