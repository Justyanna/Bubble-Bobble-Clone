package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Specter extends Enemy
{

    private boolean _jumped;
    private int _up;
    private Map _level;

    public Specter(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().specter, 2, 2, 6.2, x, y, direction, CollisionMode.FLYING);
        _level = level;
        _jumped = false;
        _up = -1;
    }

    @Override
    public void movementRules()
    {
        _velocity.x = _direction * _speed;

        boolean wall_ahead = _level.check(_position.x + (1 + _direction) / 2.0 * _width + _velocity.x, _position.y);
        boolean wall_uphead = _level.check(_position.x, _position.y + (1 + _up) / 2.0 * _height + 2 * _velocity.y);

        if(wall_ahead)
        {
            _direction *= -1;
        }

        if(wall_uphead)
        {
            _up *= -1;
            setAnimation(_up < 0 ? Animations.ASCEND : Animations.DESCEND);
        }

        _velocity.x = _grounded ? _direction * _speed : _direction * _speed;
        _velocity.y = Game.GRAVITY * _up;
    }
}
