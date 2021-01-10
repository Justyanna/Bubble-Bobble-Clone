package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;

abstract public class Enemy extends Entity {

    public Enemy(SpriteSheet gfx, int w, int h, double speed, int x, int y, int direction) {

        super(gfx, Animations.WALK, w, h);
        _speed = speed / 60.0;
        spawn(x + (_direction - 1) / 2.0, y);

    }

}
