package edu.uwb.ii.bubble_bobble.game;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.collider.EntityCollider;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.rendering.Animation;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;
import edu.uwb.ii.bubble_bobble.game.utils.Vec2;
import edu.uwb.ii.bubble_bobble.scenes.game.Map;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

abstract public class Entity
{
    //    -- dependencies
    protected SpriteSheet _gfx;
    protected Animation _animation;
    protected EntityCollider _collider;

    //    -- constants
    protected boolean _map_locked;
    protected int _width;
    protected int _height;
    protected ArrayList<Projectile> _projectiles;

    //    -- stats
    protected double _speed;
    protected double _jump_height;
    protected double _fire_rate;

    //    -- state
    protected Vec2 _position;
    protected int _direction;
    protected Vec2 _velocity;
    protected double _jump;
    protected double _cooldown;

    //    -- flags
    private boolean _spawned;
    protected boolean _grounded;

    public Entity(SpriteSheet gfx, Animation animation, int w, int h, CollisionMode mode)
    {
        _gfx = gfx;
        _animation = animation.copy();
        _map_locked = true;
        _width = w;
        _height = h;

        _collider = new EntityCollider(this, mode);

        _speed = 0.0;
        _jump_height = 5.0;
        _fire_rate = 0.0;

        _direction = 1;
        _velocity = new Vec2();
        _jump = 0.0;
        _cooldown = 0.0;

        _spawned = false;
        _grounded = false;
    }

    public Vec2 get_position() { return new Vec2(_position); }

    public Vec2 get_velocity() { return new Vec2(_velocity); }

    public int get_direction() { return _direction; }

    public double getX() { return _position.x; }

    public double getY() { return _position.y; }

    public int get_width() { return _width; }

    public int get_height() { return _height; }

    public Collider get_collider() { return _collider; }

    public double getTop() { return _position.y + 1.0 - _height; }

    public double getBottom() { return _position.y + 1.0; }

    public double getLeft() { return _position.x; }

    public double getRight() { return _position.x + _width; }

    public double getFront() { return _direction > 0 ? getRight() : getLeft(); }

    public double getBack() { return _direction > 0 ? getLeft() : getRight(); }

    public void set_position(double x, double y) { _position = new Vec2(x, y); }

    public void set_position(Vec2 v) { _position = new Vec2(v); }

    public void setAnimation(Animation animation)
    {
        _animation.set_frames(animation.getFrames());
        _animation.reset();
    }

    public void setAnimation(Animation animation, int speed)
    {
        _animation.set_frames(animation.getFrames());
        _animation.set_speed(speed);
        _animation.reset();
    }

    public Entity spawn(double x, double y)
    {
        _position = new Vec2(x, y);
        _spawned = true;
        return this;
    }

    public void set_direction(int direction)
    {
        _direction = direction < 0 ? -1 : 1;
    }

    public void draw(GraphicsContext gc, int scale)
    {
        if(!_spawned) { return; }

        int x = (int) (scale * _position.x) + (_direction < 0 ? scale * _width : 0);
        int y = (int) (scale * (_position.y + 1 - _height));
        int w = _direction * scale * _width;
        int h = scale * _height;

        _gfx.draw(gc, _animation.next(), x, y, w, h);
    }

    public boolean collide(Entity e)
    {
        return _collider.test(e.get_collider());
    }

    public final void move(Map map)
    {
        movementRules();
        _collider.clearContactData();

        if(_collider.test(map.get_collider()))
        {
            if(_collider.bottom)
            {
                _position.y = Math.floor(_position.y);
                _velocity.y = 0.0;
                _grounded = true;
            }
            else
            {
                _grounded = false;
            }

            if(_collider.top)
            {
                _position.y = Math.ceil(_position.y);
                _velocity.y = 0.0;
            }

            if(_collider.left || _collider.right)
            {
                _position.x = Math.round(_position.x);
                _velocity.x = 0.0;
            }
        }

        _position.add(_velocity);

        if(_map_locked)
        {
            if(_position.x < -1.0) { _position.x = Map.COLUMNS; }
            if(_position.x > Map.COLUMNS - 1.0 + _width) { _position.x = 1.0 - _width; }
            if(_position.y < -1.0) { _position.y = Map.ROWS; }
            if(_position.y > Map.ROWS - 1.0 + _height) { _position.y = 1.0 - _height; }
        }

        if(_fire_rate > 0.0 && _cooldown > 0.0)
        {
            _cooldown -= _fire_rate / 60.0;
        }
    }

    abstract public void movementRules();
}
