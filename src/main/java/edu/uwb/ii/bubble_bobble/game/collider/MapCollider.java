package edu.uwb.ii.bubble_bobble.game.collider;

import edu.uwb.ii.bubble_bobble.game.Collider;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class MapCollider implements Collider {

    private Map _host;

    public MapCollider(Map host) {

        _host = host;

    }

    @Override
    public boolean test(Collider c) {

        if(c instanceof GroupCollider) {
            return c.test(this);
        }

        if(c instanceof EntityCollider) {

            EntityCollider ec = (EntityCollider) c;

            int w = ec.getWidth(), h = ec.getHeight();
            double x = ec.getX(), y = ec.getY();

            for(int i = 0; i <= h; i++) {
                for(int j = 0; j <= w; j++) {
                    if(_host.check(x + j, y)) {

                        ec.bottom = _host.check(x, y) || _host.check(x + w, y);
                        ec.left = _host.check(x, y) && (_host.check(x, y - 1) || _host.check(x, y - 1));
                        ec.right = _host.check(x + w, y) && (_host.check(x + w, y - 1) || _host.check(x + w, y - 1));

                        return true;

                    }
                }
            }

        }

        return false;

    }
}
