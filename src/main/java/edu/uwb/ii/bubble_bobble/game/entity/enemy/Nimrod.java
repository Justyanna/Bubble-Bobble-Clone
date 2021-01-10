package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Arrow;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.game.utils.Position;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Nimrod extends Enemy {

    private Map _level;
    private Game _game;

    public Nimrod (int x, int y, int direction, Map level) {

        super(ResourceManager.get().nimrod, 2, 2, 6.0, x, y, direction);

        _game = Game.getInstance();
        _projectiles = _game.get_hostile_projectiles();
        _level = level;

        _fire_rate = 0.25;

    }

    @Override
    public void movementRules() {

        _dx = _direction * _speed;

        boolean wall_ahead = _grounded && _level.check(front() + _dx, _y);

        if (wall_ahead) {
            _direction *= -1;
        }

        _dx = _grounded ? _direction * _speed : 0.0;
        _dy = Game.GRAVITY;

        Position player = _game.getPlayerPosition();
        boolean can_see_player = Math.abs(_y - player.y) < 2 && Math.signum(player.x - _x) == _direction;

        if (can_see_player && _cooldown <= 0.0) {
            _projectiles.add(new Arrow(front() - _direction, _y, _direction));
            _cooldown = 1.0;
        }

    }
}
