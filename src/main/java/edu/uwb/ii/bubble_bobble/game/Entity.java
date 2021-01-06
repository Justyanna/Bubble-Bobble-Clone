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
    protected boolean _grounded;

    protected double _x;
    protected double _y;

    private boolean _spawned;

    public Entity(SpriteSheet gfx, Animation animation, int w, int h) {

        _gfx = gfx;
        _animation = animation.copy();
        _width = w;
        _height = h;

        _direction = 1;
        _speed = 0;
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
        int y = (int) (scale * _y);
        int w = _direction * scale * _width;
        int h = scale * _height;

        _gfx.draw(gc, _animation.next(), x, y, w, h);

    }

    public final void move(boolean [][] map) {

        movementRules(map);

        if(this._x < -1) this._x = map.length;
        if(this._x > map.length) this._x = -1;
        if(this._y < -1) this._y = map[0].length;
        if(this._y > map[0].length) this._y = -1;

    }

    abstract public void movementRules(boolean [][] map);

}
