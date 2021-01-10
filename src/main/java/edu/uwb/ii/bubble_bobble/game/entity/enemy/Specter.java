package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Specter extends Enemy {

    private boolean _jumped;
    private int _up;
    private Map _level;

    public Specter(int x, int y, int direction, Map level) {

        super(ResourceManager.get().specter, 2, 2, 6.2, x, y, direction);
        _level = level;
        _jumped = false;
        _up = 1;
    }

    @Override
    public void movementRules() {

        _dx = _direction * _speed;

        boolean wall_ahead = _level.check(_x + (1 + _direction) / 2.0 * _width + _dx, _y);
        boolean wall_uphead = _level.check(_x, _y + (1 + _up) / 2.0 * _height + 2 * _dy);

        if (wall_ahead) {
            _direction *= -1;
        }

        if (wall_uphead) {
            _up *= -1;
        }

        _dx = _grounded ? _direction * _speed : _direction * _speed;
        _dy = Game.GRAVITY * _up;
    }
}
