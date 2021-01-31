package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Bubble;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

import java.util.Random;

public class Imp extends Enemy
{
    private Game _game;
    private Map _level;
    private Random _rng;

    private int _fly_direction;
    private int _swap;

    public Imp(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().imp, 2, 2, 3.5 / Math.sqrt(2.0), x, y, direction, CollisionMode.NONE);

        _game = Game.getInstance();
        _level = level;
        _rng = Game.get_rng();

        _fly_direction = 1;
        _swap = (int) Game.FRAME_RATE * (_rng.nextInt(2) + 1);
    }

    @Override
    public void movementRules()
    {
        boolean swap_time = --_swap < 1;

        if(swap_time)
        {
            _swap = (int) Game.FRAME_RATE * (_rng.nextInt(2) + 1);
        }

        if(swap_time && _rng.nextDouble() < 0.4)
        {
            _fly_direction *= -1;
        }

        if(swap_time && _rng.nextDouble() < 0.4)
        {
            _direction *= -1;
        }

        _velocity.x = _direction * _speed;
        _velocity.y = _fly_direction * _speed;

        for(Projectile projectile : _game.get_bubbles())
        {
            Bubble bubble = (Bubble) projectile;
            if(!bubble.isActive() && _collider.test(bubble.get_collider()))
            {
                bubble.pop();
            }
        }
    }
}
