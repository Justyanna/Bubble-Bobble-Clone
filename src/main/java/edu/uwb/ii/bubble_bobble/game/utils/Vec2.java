package edu.uwb.ii.bubble_bobble.game.utils;

public class Vec2
{
    public double x, y;

    public Vec2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 v)
    {
        this(v.x, v.y);
    }

    public Vec2()
    {
        this(0.0, 0.0);
    }

    public void add(double x, double y)
    {
        this.x += x;
        this.y += y;
    }

    public void add(Vec2 v)
    {
        add(v.x, v.y);
    }

    public double length()
    {
        return Math.sqrt(x * x + y * y);
    }
}
