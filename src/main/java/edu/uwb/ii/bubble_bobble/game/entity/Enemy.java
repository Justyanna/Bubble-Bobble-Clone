package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;

abstract public class Enemy extends Entity {

    public Enemy(SpriteSheet gfx, int w, int h, double speed, int x, int y) {

        super(gfx, Animations.TMP_ENEMY, w, h);

        _speed = speed;

        spawn(x, y);

    }

}
