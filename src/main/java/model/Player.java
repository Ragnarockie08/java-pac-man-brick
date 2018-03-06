package model;

import java.io.Serializable;

public class Player implements Serializable {


    private double xCoordinate;
    private double yCoordinate;

    public Player() {
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

    @Override
    public String toString() {
        return "Player{" +
                "x ='" + xCoordinate + '\'' +
                ", y ='" + yCoordinate +
                '}';
    }
}
