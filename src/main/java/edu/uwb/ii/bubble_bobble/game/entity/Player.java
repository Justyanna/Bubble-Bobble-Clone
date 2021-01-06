package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.Inputs;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;

public class Player extends Entity {

    private Inputs _controls;

    private int _spawn_x;
    private int _spawn_y;

    public Player(int x, int y, Inputs controls) {

        super(ResourceManager.get().placeholder, Animations.TMP_PLAYER, 1, 1);

        _controls = controls;

        _spawn_x = x;
        _spawn_y = y;
        _speed = 7.0;

        spawn(x, y);

    }

    @Override
    public void movementRules(boolean [][] map) {

        int w = map.length;
        int h = map[0].length;
        int x = ((int) _x + w) %  w;
        int y = ((int) _y + 1) % h;

        if(this._controls.left) {
            _direction = -1;
            if(!map[x % w][(y + h - 1) % h])
                this._x -= _speed / 60.0;
        }

        if(this._controls.right) {
            _direction = 1;
            if(!map[(x + 1) % w][(y + h - 1) % h])
                this._x += _speed / 60.0;
        }

        if(!map[x][y] && !map[(x+1) % w][y]) {
            this._y += 4 / 60.0;
        }
        else {
            this._y = y - 1;
        }

    }
}
