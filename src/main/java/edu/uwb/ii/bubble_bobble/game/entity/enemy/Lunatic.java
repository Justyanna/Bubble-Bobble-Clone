package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.game.utils.Vec2;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

import java.util.stream.Stream;

public class Lunatic extends Enemy
{
    private Map _level;
    Game _game;
    int _panic;

    public Lunatic(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().lunatic, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);
        _level = level;
        _game = Game.getInstance();
        _panic = 0;
    }

    @Override
    public void movementRules()
    {
        if(_direction == 1 && _collider.right || _direction == -1 && _collider.left)
        {
            _direction *= -1;
        }

        if(_panic > 0)
        {
            if(--_panic == 0)
            {
                setAnimation(_angry ? Animations.ANGRY : Animations.WALK);
            }
            _velocity.x = 0.0;
        }
        else
        {
            Stream<Vec2> enemies = _game.getEnemyPositions();

            if(enemies.anyMatch(enemy -> Math.abs(_position.y - enemy.y) < 1 &&
                                         _direction * (enemy.x - _position.x) > 0 &&
                                         _direction * (enemy.x - _position.x) < 3))
            {
                if(_level.check(_position.x - _direction, _position.y))
                {
                    _panic = (int) Game.FRAME_RATE * 2;
                    setAnimation(_angry ? Animations.PANIC : Animations.CRY);
                }
                else
                {
                    _direction *= -1;
                }
            }

            _velocity.x = _grounded ? _direction * _speed : 0.0;
            _velocity.y = Game.GRAVITY;
        }
    }
}
