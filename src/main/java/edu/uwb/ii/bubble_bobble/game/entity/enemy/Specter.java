package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Specter extends Enemy
{

    private int _fly_direction;
    private Map _level;

    public Specter(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().specter, 2, 2, 6.2, x, y, direction, CollisionMode.FLYING);
        _level = level;
        _fly_direction = -1;
    }

    @Override
    public void movementRules()
    {
        if(_collider.top || _collider.bottom)
        {
            _fly_direction *= -1;

            setAnimation(_fly_direction == -1 ? Animations.ASCEND : Animations.DESCEND);
        }

        if(_collider.left || _collider.right)
        {
            _direction *= -1;
        }

        _velocity.x = _direction * _speed;
        _velocity.y = Game.GRAVITY * _fly_direction;
    }
}
