package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;

abstract public class Projectile extends Entity {

    protected double _max_distance;
    protected double _traveled;
    protected boolean _wasted;

    public Projectile(SpriteSheet skin, double x, double y, int direction, double max_distance) {

        super(skin, Animations.TMP_BUBBLE_RUSH, 2, 2);

        _direction = direction;
        _max_distance = max_distance;
        _traveled = 0;
        _wasted = false;

        this.spawn(x + (direction - 1) / 2.0 * _width, y);

    }

    public boolean isWasted() {
        return _wasted;
    }

}
