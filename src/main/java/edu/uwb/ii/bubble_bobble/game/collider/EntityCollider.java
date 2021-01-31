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

    public Entity get_host() { return _host; }

    public CollisionMode get_mode() { return _mode; }

    public void set_mode(CollisionMode mode) { _mode = mode; }

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
