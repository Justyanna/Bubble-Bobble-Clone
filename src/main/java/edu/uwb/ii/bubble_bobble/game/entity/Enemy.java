package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;

abstract public class Enemy extends Entity {

    public Enemy(SpriteSheet gfx, int w, int h, double speed, int x, int y, int direction, CollisionMode mode)
    {

        super(gfx, Animations.WALK, w, h, mode);

        _speed = speed / 60.0;
        _direction = direction;
        spawn(x + (_direction - 1) / 2.0, y);
    }

}
