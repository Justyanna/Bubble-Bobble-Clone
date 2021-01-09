package edu.uwb.ii.bubble_bobble.game.collider;

import edu.uwb.ii.bubble_bobble.game.Collider;

public class BoxCollider implements Collider {

    public double x, y, w, h, ox, oy;

    public BoxCollider(double x, double y, double w, double h) {

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        ox = x + w / 2.0;
        oy = y + h / 2.0;

    }

    public BoxCollider(EntityCollider ec) {

        this(ec.getX(), ec.getY(), ec.getWidth(), ec.getHeight());

    }

    @Override
    public boolean test(Collider c) {

        if(c instanceof BoxCollider) {
            BoxCollider bc = (BoxCollider) c;
            return Math.abs(ox - bc.ox) < (w + bc.w) / 2.5
                    && Math.abs(oy - bc.oy) < (h + bc.h) / 2.5;
        }

        if(c instanceof EntityCollider) {
            return test(new BoxCollider((EntityCollider) c));
        }

        if(c instanceof GroupCollider) {
            return c.test(this);
        }

        return false;

    }
}
