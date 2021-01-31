package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Fireball;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.game.utils.Vec2;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

import java.util.Random;

public class Warlock extends Enemy
{
    private Game _game;
    private Random _rng;
    private Map _level;
    private int _aim;
    private int _cast;

    private int _min_rest;
    private int _max_rest;
    private double _cast_chance;

    public Warlock(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().warlock, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);
        _game = Game.getInstance();
        _rng = Game.get_rng();
        _projectiles = _game.get_hostile_projectiles();
        _level = level;

        _min_rest = 2;
        _max_rest = 5;
        _cast_chance = 0.4;
        _cast = (int) Game.FRAME_RATE * (_rng.nextInt(_max_rest - _min_rest + 1) + _min_rest);
        _aim = 0;
        _fire_rate = 0.2;
    }

    @Override
    public void movementRules()
    {
        if(_direction == 1 && _collider.right || _direction == -1 && _collider.left)
        {
            _direction *= -1;
        }

        Vec2 player = _game.getPlayerPosition();

        if(--_cast < 1 && _aim == 0)
        {
            if(_rng.nextDouble() < _cast_chance)
            {
                setAnimation(_angry ? Animations.BLAST : Animations.SHOOT);
                _aim = (int) (Game.FRAME_RATE * 2.5);
            }
            else
            {
                _cast = (int) (Game.FRAME_RATE * (_rng.nextInt(_max_rest - _min_rest + 1) + _min_rest) / 2.0);
            }
        }

        if(_aim > 0 && --_aim == 0)
        {
            setAnimation(_angry ? Animations.ANGRY : Animations.WALK);
            if(_rng.nextDouble() < 0.7)
            {
                _projectiles.add(new Fireball(_rng.nextInt(36) - 2, -5.0, _game.getPlayerPosition(), 22.0));
            }
            else
            {
                _projectiles.add(new Fireball(_position.x, _position.y, _game.getPlayerPosition(), 12.0));
            }
            _cast = (int) Game.FRAME_RATE * (_rng.nextInt(_max_rest - _min_rest + 1) + _min_rest);
        }

        _velocity.x = _grounded && _aim < 1 ? _direction * _speed : 0.0;
        _velocity.y = Game.GRAVITY;
    }

    @Override
    public void getAngry()
    {
        super.getAngry();
        _cast_chance = Math.min(_cast_chance + 0.2, 1.0);
    }
}
