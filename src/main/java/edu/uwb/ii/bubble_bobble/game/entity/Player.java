package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.Inputs;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Bubble;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;

import java.util.ArrayList;

public class Player extends Entity {

    private Inputs _controls;

    public Player(double x, double y, int direction, ArrayList<Projectile> projectile_output) {

        super(ResourceManager.get().placeholder, Animations.TMP_PLAYER, 2, 2);

        _controls = App.getInputs();
        _projectiles = projectile_output;

        _speed = 7.0 / 60.0;
        _fire_rate = 1.5;

        _direction = direction;

        spawn(x + (_direction - 1) / 2.0, y);

    }

    @Override
    public void movementRules() {

        _dx = 0;
        _dy = Game.GRAVITY;

        if (_jump > 0) {
            double jh = 8.0 / 60.0;
            _dy = _jump * _jump_height < jh ? -_jump * _jump_height * 1.1 : -jh;
            _jump -= jh / _jump_height;
            if (!_controls.jump)
                _jump *= 0.1;
        }

        if (_controls.left) {
            _direction = -1;
            _dx -= _speed;
        }

        if (_controls.right) {
            _direction = 1;
            _dx += _speed;
        }

        if (_grounded && _controls.jump) {
            _jump = 1.0;
        }

        if(_controls.action && _cooldown <= 0.0) {
            _projectiles.add(new Bubble(front(), _y, _direction));
            _cooldown = 1.0;
        }

    }
}
