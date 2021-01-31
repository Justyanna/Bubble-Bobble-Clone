package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

import java.util.Random;

public class Beast extends Enemy
{
    private Map _level;
    private int _floor;
    private int _time_to_jump;
    private Random _rng;

    public Beast(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().beast, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);
        _level = level;
        _animation.set_speed(8);
        _rng = Game.get_rng();
        _floor = 1;
        _time_to_jump = (int) Game.FRAME_RATE * (_rng.nextInt(20) + 5);
    }

    @Override
    public void movementRules()
    {
        if(_direction == 1 && _collider.right || _direction == -1 && _collider.left)
        {
            _direction *= -1;
        }

        _velocity.x = _direction * _speed;
        _velocity.y = _floor * Game.GRAVITY;

        if(--_time_to_jump < 1)
        {
            _floor *= -1;
            _collider.set_mode(_floor > 0 ? CollisionMode.REGULAR : CollisionMode.INVERTED);
            setAnimation(_angry
                         ? _floor > 0 ? Animations.ANGRY : Animations.CLIMB
                         : _floor > 0 ? Animations.WALK : Animations.TURN);
            _time_to_jump = (int) Game.FRAME_RATE * (_rng.nextInt(20) + 5);
        }
    }

    @Override
    public void getAngry()
    {
        super.getAngry();
        setAnimation(_floor > 0 ? Animations.ANGRY : Animations.CLIMB, 8);
    }
}
