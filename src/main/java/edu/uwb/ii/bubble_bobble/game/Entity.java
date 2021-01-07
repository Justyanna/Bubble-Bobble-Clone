package edu.uwb.ii.bubble_bobble.game;

import edu.uwb.ii.bubble_bobble.game.rendering.Animation;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;

abstract public class Entity {

    private SpriteSheet _gfx;
    private Animation _animation;
    protected Collider _collider;

    protected int _width;
    protected int _height;

    protected int _direction;
    protected double _speed;
    protected double _jump_height;
    protected boolean _grounded;

    protected double _x;
    protected double _y;
    protected double _jump;

    private boolean _spawned;

    public Entity(SpriteSheet gfx, Animation animation, int w, int h) {

        _gfx = gfx;
        _animation = animation.copy();
        _width = w;
        _height = h;

        _direction = 1;
        _speed = 0;
        _jump_height = 5.5;
        _jump = 0;
        _grounded = false;

        _spawned = false;

    }

    public Entity spawn(int x, int y) {

        _x = x;
        _y = y;
        _spawned = true;
        return this;

    }

    public void set_direction(int direction) {

        _direction = direction < 0 ? -1 : 1;

    }

    public void draw(GraphicsContext gc, int scale) {

        if (!_spawned) return;

        int x = (int) (scale * _x) + (_direction < 0 ? scale * _width : 0);
        int y = (int) (scale * (_y + 1 - _height));
        int w = _direction * scale * _width;
        int h = scale * _height;

        _gfx.draw(gc, _animation.next(), x, y, w, h);

    }

    public final void move(boolean [][] map) {

        double [] delta = movementRules(map);

        int w = map.length;
        int h = map[0].length;
        double dx = delta[0];
        double dy = delta[1];

        _x += dx;
        _y += dy;

        int x = ((int) _x + w) % w;
        int y = ((int) _y + h) % h;

        int wx = (x + (1 + _direction) / 2 * _width) % w;

        boolean wall_ahead = map[wx][y] && !map[(wx  - _direction + w) % w][y]
                || _y % 1 >= dy && map[wx][(y + 1) % h] && !map[(wx  - _direction + w) % w][(y + 1) % h];

        if(wall_ahead)
            _x = Math.floor(_x + (1 - _direction) / 2);

        x = ((int) _x + w) % w;

        boolean wall_below = map[x][(y + 1) % h] || _x % 1 > 0.0 && map[(x + _width) % w][(y + 1) % h];

        if(wall_below && _y % 1 <= dy * 1.1) {
            _y = Math.floor(_y);
            _grounded = true;
        }
        else {
            _grounded = false;
        }

        if(this._x < -1) this._x = map.length;
        if(this._x > map.length) this._x = -1;
        if(this._y < -1) this._y = map[0].length;
        if(this._y > map[0].length) this._y = -1;

    }

    abstract public double [] movementRules(boolean [][] map);

}
