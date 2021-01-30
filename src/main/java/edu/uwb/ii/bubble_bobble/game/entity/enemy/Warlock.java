package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Warlock extends Enemy
{

    private boolean _jumped;
    private Map _level;

    public Warlock(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().placeholder, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);
        _level = level;
        _jumped = false;
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

        _velocity.x = _grounded ? _direction * _speed : 0.0;
        _velocity.y = Game.GRAVITY;
    }
}
