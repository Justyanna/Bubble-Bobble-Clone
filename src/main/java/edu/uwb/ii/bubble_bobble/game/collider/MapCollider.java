package edu.uwb.ii.bubble_bobble.game.collider;

import edu.uwb.ii.bubble_bobble.game.Collider;
import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.utils.Vec2;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class MapCollider implements Collider
{
    private Map _host;

    public MapCollider(Map host)
    {
        _host = host;
    }

    @Override
    public boolean test(Collider c)
    {
        if(c instanceof GroupCollider)
        {
            return c.test(this);
        }

        if(c instanceof EntityCollider)
        {
            EntityCollider ec = (EntityCollider) c;

            Entity e = ec.get_host();
            Vec2 vel = e.get_velocity();

            switch(ec.get_mode())
            {
                case NONE:
                case INVERTED:
                case FLYING:
                    return false;

                case REGULAR:
                    for(int i = 0; i < e.get_width() + 1; i++)
                    {
                        boolean space_above = !_host.check(e.getX() + i, e.getBottom() - vel.y);
                        boolean floor_below = _host.check(e.getX() + i, e.getBottom());
                        if(vel.y > 0 && floor_below && space_above)
                        {
                            ec.bottom = true;
                            break;
                        }
                    }

                    boolean wall_over = _host.check(e.getFront(), e.getY() - 1.0) &&
                                        !_host.check(e.getFront() - vel.x, e.getY() - 1.0);
                    boolean wall_ahead = _host.check(e.getFront(), e.getY()) &&
                                         !_host.check(e.getFront() - vel.x, e.getY());
                    boolean wall_below = _host.check(e.getFront(), e.getY() + 1.0) &&
                                         !_host.check(e.getFront() - vel.x, e.getY() + 1.0);

                    if(vel.x != 0 && wall_ahead && (wall_over || wall_below))
                    {
                        ec.right = e.get_direction() == 1;
                        ec.left = e.get_direction() == -1;
                    }
                    return true;
            }
        }

        return false;
    }
}
