package edu.uwb.ii.bubble_bobble.game.entity.projectile;

import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;

public class Bubble extends Projectile {

    private boolean _active;

    public Bubble(double x, double y, int direction) {

        super(ResourceManager.get().placeholder, x, y, direction,10.0);

        _active = true;
        _dx = _direction * 15.0 / 60.0;

    }

    @Override
    public void movementRules() {

        if(_active && (_traveled >= _max_distance || _dx == 0)) {

            _animation = Animations.TMP_BUBBLE_FLY;
            _dx = 0;
            _dy = -Game.GRAVITY / 4.0;

        }

        _traveled += Math.sqrt(_dx * _dx + _dy * _dy);

    }

}
