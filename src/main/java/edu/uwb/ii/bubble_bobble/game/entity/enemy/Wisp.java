package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Wisp extends Enemy
{

    private Map _level;

    public Wisp(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().wisp, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);
        _level = level;
        setAnimation(Animations.DESCEND);
    }

    @Override
    public void movementRules()
    {
        if(_direction == 1 && _collider.right || _direction == -1 && _collider.left)
        {
            _direction *= -1;
        }

        _velocity.x = _direction * _speed;
        _velocity.y = Game.GRAVITY;

        if(_jump > 0)
        {
            double jh = 8.0 / 60.0;
            _velocity.y = _jump * _jump_height < jh ? -_jump * _jump_height : -jh;
            _jump -= jh / _jump_height;
            if(_jump <= 0)
            {
                setAnimation(_angry ? Animations.DIVE : Animations.DESCEND);
            }
        }

        if(_grounded && _jump <= 0.0)
        {
            _jump = 1.0;
            setAnimation(_angry ? Animations.ANGRY : Animations.ASCEND);
        }

        _velocity.x = _direction * _speed;
    }

    @Override
    public void getAngry()
    {
        super.getAngry();
        setAnimation(Animations.DIVE);
    }
}
