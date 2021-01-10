package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

import java.util.Random;

public class Errant extends Enemy {

    private boolean _jumped;
    private Map _level;
    private double _probability;
    private Random _roll;
    private boolean _stop;
    private double _fallen;
    private int _steps;
    private double _sleep;

    public Errant(int x, int y, int direction, Map level) {

        super(ResourceManager.get().errant, 2, 2, 5.0, x, y, direction);
        _level = level;
        _jumped = false;
        _probability = 0.40;
        _roll = new Random();
        _fallen = 0;
        _sleep = 60.0;
        _steps = 60 * 4;
    }

    @Override
    public void movementRules() {

        _dx = _direction * _speed;
        boolean wall_ahead = _grounded && _level.check(_x + (1 + _direction) / 2.0 * _width + _dx, _y);

        if (wall_ahead) {
            _direction *= -1;
        }

        if (_steps == 0) {
            _stop = _roll.nextDouble() < _probability;
            _fallen = _stop ? 1 : 0;
            _steps = 60 * 10;
        }
        _fallen -= 1.0 / _sleep;

        _dx = _fallen > 0 ? 0.0 : _grounded ? _direction * _speed : 0.0;
        _dy = Game.GRAVITY;
        _steps--;
    }
}
