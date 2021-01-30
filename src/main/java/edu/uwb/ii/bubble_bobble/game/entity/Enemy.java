package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;

abstract public class Enemy extends Entity
{
    protected boolean _angry;

    public Enemy(SpriteSheet gfx, int w, int h, double speed, int x, int y, int direction, CollisionMode mode)
    {
        super(gfx, Animations.WALK, w, h, mode);

        _angry = false;
        _speed = speed / 60.0;
        _direction = direction;
        spawn(x + (_direction - 1) / 2.0, y);
    }

    public void getAngry()
    {
        _angry = true;
        setAnimation(Animations.ANGRY);

        _speed *= 1.5;
        _fire_rate *= 2.0;
    }
}
