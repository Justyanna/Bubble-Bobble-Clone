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
    private Map _level;
    private double _fall_chance;
    private Random _rng;
    private double _fallen;
    private int _steps;
    private double _sleep;

    public Errant(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().errant, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);
        _level = level;
        _fall_chance = 0.8;
        _rng = Game.get_rng();
        _fallen = 0;
        _sleep = 60.0;
        _steps = 60 * (_rng.nextInt(12) + 3);
    }

    @Override
    public void movementRules()
    {
        if(_direction == 1 && _collider.right || _direction == -1 && _collider.left)
        {
            _direction *= -1;
        }

        _velocity.x = _direction * _speed;

        if(_steps <= 0.0)
        {
            if(_rng.nextDouble() < _fall_chance)
            {
                _fallen = 1.0;
                setAnimation(_angry ? Animations.DIVE : Animations.FALL);
            }
            _steps = 60 * (_rng.nextInt(15) + 5);
        }

        if(_fallen > 0.0 && _fallen < 1.0 / _sleep)
        {
            setAnimation(_angry ? Animations.ANGRY : Animations.WALK);
        }

        _fallen -= 1.0 / _sleep;

        _velocity.x = _fallen > 0 ? 0.0 : _grounded ? _direction * _speed : 0.0;
        _velocity.y = Game.GRAVITY;
        _steps--;
    }
}
