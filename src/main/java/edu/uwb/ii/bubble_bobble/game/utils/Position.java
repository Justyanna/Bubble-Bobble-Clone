package edu.uwb.ii.bubble_bobble.game.utils;

public class Position {

    public double x, y;
    public int direction;

    public Position(double x, double y, int direction) {

        this.x = x;
        this.y = y;
        this.direction = direction;

    }

    public Position(double x, double y) {

        this(x, y, 1);

    }

}
