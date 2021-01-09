package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;

public class Walker extends Enemy {

    private boolean _jumped;
    private Map _level;

    public Walker (int x, int y, Map level) {

        super(ResourceManager.get().placeholder, 2, 2, 6.0, x, y);
        _level = level;
        _jumped = false;

    }

    @Override
    public void movementRules() {

        _dx = _direction * _speed;

        boolean wall_ahead = _grounded && _level.check(_x + (1 + _direction) / 2.0 * _width + _dx, _y);

        if(wall_ahead) {
            _direction *= -1;
        }

        _dx = _grounded ? _direction * _speed : 0.0;
        _dy = Game.GRAVITY;


//        int w = map.length;
//        int h = map[0].length;
//        int x = ((int) _x + w) % w;
//        int y = ((int) _y + h) % h;
//
//        boolean wall_below = map[x][(y + 1) % h] || _x % 1 > 0.0 && map[(x + _width) % w][(y + 1) % h];
//
//        if(_jumped && wall_below) {
//            _jumped = false;
//        }
//
//        _dy = 10.0 / 60.0;
//
//        if (_jump > 0) {
//            double jh = 8.0 / 60.0;
//            _dy = _jump * _jump_height < jh ? -_jump * _jump_height * 1.1 : -jh;
//            _jump -= jh / _jump_height;
//        }
//
//        _dx = _jumped || wall_below ? _direction * _speed : 0;
//
//        if(_dx == 0) {
//            _x = _direction > 0 ? Math.floor(_x) : Math.ceil(_x);
//        }
//
//        int looking_at = ((int)(_x + _dx) + ((1 + _direction) * _width) / 2 + w) % w;
//
//        if(Math.abs(_dx) > 0 && !_jumped && map[looking_at][y] && !map[(looking_at - _direction + w) % w][y]) {
//            _direction *= -1;
//        }
//        else if(y < 24 && wall_below && !map[looking_at][(y + 1) % h]) {
//            _jump = 1.0;
//            _jumped = true;
//        }

    }
}
