package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.Inputs;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;

public class Player extends Entity {

    private Inputs _controls;

    private int _spawn_x;
    private int _spawn_y;

    public Player(int x, int y, Inputs controls) {

        super(ResourceManager.get().placeholder, Animations.TMP_PLAYER, 2, 2);

        _controls = controls;

        _spawn_x = x;
        _spawn_y = y;

        _speed = 7.0 / 60.0;

        spawn(x, y);

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

    }
}
