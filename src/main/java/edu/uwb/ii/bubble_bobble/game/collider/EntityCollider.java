package edu.uwb.ii.bubble_bobble.game.collider;

import edu.uwb.ii.bubble_bobble.game.Collider;
import edu.uwb.ii.bubble_bobble.game.Entity;

public class EntityCollider implements Collider
{
    private Entity _host;
    private CollisionMode _mode;

    public boolean top, right, bottom, left;

    public EntityCollider(Entity host, CollisionMode mode)
    {
        _host = host;
        _mode = mode;
    }

    public double getX() { return _host.getX(); }

    public double getY() { return _host.getY() - 1.0 + _host.get_height(); }

    public int getWidth() { return _host.get_width(); }

    public int getHeight() { return _host.get_height(); }

    public void clearContactData()
    {

        top = right = bottom = left = false;
    }

    @Override
    public boolean test(Collider c)
    {
        if(c instanceof BoxCollider || c instanceof GroupCollider || c instanceof MapCollider)
        {
            return c.test(this);
        }

        else if(c instanceof EntityCollider)
        {
            EntityCollider ec = (EntityCollider) c;
            return new BoxCollider(this).test(new BoxCollider(ec));
        }

        return false;
    }
}
