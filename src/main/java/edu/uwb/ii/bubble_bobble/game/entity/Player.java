package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.Inputs;
import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Bubble;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;

import java.util.ArrayList;

public class Player extends Entity
{

    private final Inputs _controls;
    private boolean _moved;

    public Player(double x, double y, int direction, ArrayList<Projectile> projectile_output)
    {
        super(ResourceManager.get().player, Animations.IDLE, 2, 2, CollisionMode.REGULAR);

        _controls = App.get_inputs();
        _projectiles = projectile_output;

        _speed = 7.0 / 60.0;
        _fire_rate = 1.5;

        _animation.set_speed(6);

        _direction = direction;
        _moved = false;

        spawn(x + (_direction - 1) / 2.0, y);
    }

    public Inputs get_controls() { return _controls; }

    public void jump()
    {
        _jump = 1.0;
    }

    @Override
    public void movementRules()
    {
        _velocity.x = 0;
        _velocity.y = Game.GRAVITY;

        if(_jump > 0)
        {
            double jh = 8.0 / 60.0;
            _velocity.y = _jump * _jump_height < jh ? -_jump * _jump_height * 1.1 : -jh;
            _jump -= jh / _jump_height;
            if(!_controls.jump)
            { _jump *= 0.1; }
        }

        if(_controls.left)
        {
            _direction = -1;
            _velocity.x -= _speed;
        }

        if(_controls.right)
        {
            _direction = 1;
            _velocity.x += _speed;
        }

        if(_moved && !_controls.left && !_controls.right)
        {
            setAnimation(Animations.IDLE);
        }
        else if(!_moved && (_controls.left || _controls.right))
        {
            setAnimation(Animations.WALK);
        }

        _moved = _controls.left || _controls.right;

        if(_grounded && _controls.jump)
        {
            jump();
        }

        if(_controls.action && _cooldown <= 0.0)
        {
            _projectiles.add(new Bubble(getFront() - _direction * 2.0, _position.y, _direction));
            _cooldown = 1.0;
        }
    }
}
