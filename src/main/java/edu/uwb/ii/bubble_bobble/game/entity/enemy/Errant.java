package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

import java.util.Random;

public class Errant extends Enemy
{

    private boolean _jumped;
    private Map _level;
    private double _probability;
    private Random _roll;
    private double _fallen;
    private int _steps;
    private double _sleep;

    public Errant(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().errant, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);
        _level = level;
        _jumped = false;
        _probability = 0.40;
        _roll = new Random();
        _fallen = 0;
        _sleep = 60.0;
        _steps = 60 * 4;
    }

    @Override
    public void movementRules()
    {
        _velocity.x = _direction * _speed;
        boolean wall_ahead =
                _grounded && _level.check(_position.x + (1 + _direction) / 2.0 * _width + _velocity.x, _position.y);

        if(wall_ahead)
        {
            _direction *= -1;
        }

        if(_steps <= 0.0)
        {
            if(_roll.nextDouble() < _probability)
            {
                _fallen = 1.0;
                setAnimation(Animations.FALL);
            }
            _steps = 60 * 10;
        }

        if(_fallen > 0.0 && _fallen < 1.0 / _sleep)
        {
            setAnimation(Animations.WALK);
        }

        _fallen -= 1.0 / _sleep;

        _velocity.x = _fallen > 0 ? 0.0 : _grounded ? _direction * _speed : 0.0;
        _velocity.y = Game.GRAVITY;
        _steps--;
    }
}
