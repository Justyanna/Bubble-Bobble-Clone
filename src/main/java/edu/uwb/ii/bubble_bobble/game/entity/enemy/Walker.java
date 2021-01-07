package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;

public class Walker extends Enemy {

    public Walker (int x, int y) {

        super(ResourceManager.get().placeholder, 2, 2, 6.0, x, y);

    }

    @Override
    public double [] movementRules(boolean [][] map) {

        int w = map.length;
        int h = map[0].length;
        int x = ((int) _x + w) % w;
        int y = ((int) _y + h) % h;

        boolean wall_below = map[x][(y + 1) % h] || _x % 1 > 0.0 && map[(x + _width) % w][(y + 1) % h];

        double dx = wall_below ? _direction * _speed : 0;
        double dy = 6.0 / 60.0;

        if(dx == 0) _x = _direction > 0 ? Math.floor(_x) : Math.ceil(_x);

        int looking_at = ((int)(_x + dx) + ((1 + _direction) * _width) / 2 + w) % w;

        if(Math.abs(dx) > 0 && map[looking_at][y]) {
            _direction *= -1;
        }

        return new double[] {dx, dy};

    }
}
