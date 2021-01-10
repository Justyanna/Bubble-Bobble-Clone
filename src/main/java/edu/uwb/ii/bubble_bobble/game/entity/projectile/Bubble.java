package edu.uwb.ii.bubble_bobble.game.entity.projectile;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import javafx.scene.canvas.GraphicsContext;

public class Bubble extends Projectile {

    private boolean _active;
    private Enemy _captive;

    public Bubble(double x, double y, int direction) {

        super(ResourceManager.get().placeholder, x, y, direction,10.0);

        _active = true;
        _dx = _direction * 15.0 / 60.0;

    }

    public boolean isActive() {
        return _active;
    }

    public boolean isEmpty() {
        return _captive == null;
    }

    public void capture(Enemy enemy) {

        _captive = enemy;
        setPosition(enemy.get_x(), enemy.get_y());
        _captive.setAnimation(Animations.CAPTURED);

    }

    @Override
    public void movementRules() {

        if(_active && (_traveled >= _max_distance || _dx == 0 || _captive != null)) {

            if (_captive == null) setAnimation(Animations.TMP_BUBBLE_FLY);

            _dx = 0;
            _dy = -Game.GRAVITY / 4.0;
            _active = false;

        }

        if (!isEmpty()) {
            _captive.setPosition(_x, _y);
        }

        _traveled += Math.sqrt(_dx * _dx + _dy * _dy);

    }

    @Override
    public void draw(GraphicsContext gc, int scale) {

        if (isEmpty()) super.draw(gc, scale);
        else _captive.draw(gc, scale);

    }

}
