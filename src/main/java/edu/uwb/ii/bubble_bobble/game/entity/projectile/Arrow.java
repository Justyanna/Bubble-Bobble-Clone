package edu.uwb.ii.bubble_bobble.game.entity.projectile;

import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;

public class Arrow extends Projectile {

    public Arrow(double x, double y, int direction) {

        super(ResourceManager.get().placeholder, x, y, direction, 10.0);

        setAnimation(Animations.TMP_PROJECTILE);
        _dx = _direction * 25.0 / 60.0;

    }

    @Override
    public void movementRules() {

        if (_dx == 0.0) _wasted = true;

    }

}
