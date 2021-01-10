package edu.uwb.ii.bubble_bobble.game;

import edu.uwb.ii.bubble_bobble.game.collider.EntityCollider;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.rendering.Animation;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

abstract public class Entity {

//    -- dependencies
    protected SpriteSheet _gfx;
    protected Animation _animation;
    protected EntityCollider _collider;

//    -- constants
    protected int _width;
    protected int _height;
    protected ArrayList<Projectile> _projectiles;

//    -- stats
    protected double _speed;
    protected double _jump_height;
    protected double _fire_rate;

//    -- state
    protected double _x;
    protected double _y;
    protected int _direction;
    protected double _dx;
    protected double _dy;
    protected double _jump;
    protected double _cooldown;

//    -- flags
    private boolean _spawned;
    protected boolean _grounded;

    public Entity(SpriteSheet gfx, Animation animation, int w, int h) {

        _gfx = gfx;
        _animation = animation.copy();
        _width = w;
        _height = h;

        _collider = new EntityCollider(this);

        _speed = 0.0;
        _jump_height = 5.5;
        _fire_rate = 0.0;

        _direction = 1;
        _dx = 0.0;
        _dy = 0.0;
        _jump = 0.0;
        _cooldown = 0.0;

        _spawned = false;
        _grounded = false;

    }

    public double get_x() { return _x; }
    public double get_y() { return _y; }
    public int get_width() { return _width; }
    public int get_height() { return _height; }
    public Collider get_collider() { return _collider; }

    public double front() {
        return _x + (1 + _direction) / 2.0 * _width;
    }

    public double back() {
        return _x - (_direction - 1) / 2.0 * _width;
    }

    public double top() {
        return _y + 1 - _height;
    }

    public double bottom() {
        return _y + 1;
    }

    public void setPosition(double x, double y) {
        _x = x;
        _y = y;
    }

    public void setAnimation(Animation animation) {

        _animation.set_frames(animation.getFrames());
        _animation.reset();

    }

    public Entity spawn(double x, double y) {

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

    public boolean collide(Entity e) {

        return _collider.test(e.get_collider());

    }

    public final void move(Map map) {

        movementRules();

        _x += _dx;
        _y += _dy;

        if(_collider.test(map.get_collider())) {

            boolean stopped = false;

            if(_collider.left && _dx < 0) {
                _x = Math.ceil(_x);
                _dx = 0;
                stopped = true;
            }

            if(_collider.right && _dx > 0) {
                _x = Math.floor(_x);
                _dx = 0;
                stopped = true;
            }

            _grounded = _collider.bottom && _dy > 0 && (!stopped || _grounded);

            if(_grounded) {
                _y = Math.floor(_y);
                _dy = 0.0;
            }

            _collider.clearContactData();

        }
        else {
            _grounded = false;
        }

        if(_x < -1.0) _x = Map.COLUMNS;
        if(_x > Map.COLUMNS - 1.0 + _width) _x = 1.0 - _width;
        if(_y < -1.0) _y = Map.ROWS;
        if(_y > Map.ROWS - 1.0 + _height) _y = 1.0 - _height;

        if(_fire_rate > 0.0 && _cooldown > 0.0) {
            _cooldown -= _fire_rate / 60.0;
        }

    }

    abstract public void movementRules();

}
