package edu.uwb.ii.bubble_bobble.game.entity.enemy;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;

public class Walker extends Enemy {

    public Walker (int x, int y) {

        super(ResourceManager.get().placeholder, 1, 1, 6.0, x, y);

    }

    @Override
    public void movementRules(boolean [][] map) {

        int w = map.length;
        int h = map[0].length;
        int x = ((int) _x + w) %  w;
        int y = ((int) _y + 1) % h;

        this._x += _speed / 60;

        if(!map[x][y] && !map[(x + 1) % w][y]) {
            this._y += 4 / 60.0;
        }

    }
}
