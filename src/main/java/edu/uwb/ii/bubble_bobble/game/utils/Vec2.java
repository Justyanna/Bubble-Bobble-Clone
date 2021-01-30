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

    public int ix() { return (int) x; }

    public int iy() { return (int) y; }

    public Vec2 add(double x, double y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2 add(Vec2 v)
    {
        return add(v.x, v.y);
    }

    public Vec2 sum(Vec2 v)
    {
        return new Vec2(this.x + v.x, this.y + v.y);
    }

    public Vec2 difference(Vec2 v)
    {
        return new Vec2(this.x - v.x, this.y - v.y);
    }

    public double length()
    {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
