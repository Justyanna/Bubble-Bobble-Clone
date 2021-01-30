package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Arrow;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.game.utils.Vec2;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Nimrod extends Enemy
{
    private Map _level;
    private Game _game;

    public Nimrod(int x, int y, int direction, Map level)
    {
        super(ResourceManager.get().nimrod, 2, 2, 6.0, x, y, direction, CollisionMode.REGULAR);

        _game = Game.getInstance();
        _projectiles = _game.get_hostile_projectiles();
        _level = level;

        _fire_rate = 0.25;
    }

    @Override
    public void movementRules()
    {
        _velocity.x = _direction * _speed;

        boolean wall_ahead = _grounded && _level.check(getFront() + _velocity.x, _position.y);

        if(wall_ahead)
        {
            _direction *= -1;
        }

        _velocity.x = _grounded ? _direction * _speed : 0.0;
        _velocity.y = Game.GRAVITY;

        Vec2 player = _game.getPlayerPosition();
        boolean can_see_player =
                Math.abs(_position.y - player.y) < 2 && Math.signum(player.x - _position.x) == _direction;

        if(can_see_player && _cooldown <= 0.0)
        {
            _projectiles.add(new Arrow(getFront() - _direction, _position.y, _direction));
            _cooldown = 1.0;
        }
    }
}
