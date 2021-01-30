package edu.uwb.ii.bubble_bobble.game.entity.projectile;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;
import javafx.scene.canvas.GraphicsContext;

public class Bubble extends Projectile
{

    private boolean _active;
    private Enemy _captive;
    private int _timer;

    public Bubble(double x, double y, int direction)
    {
        super(ResourceManager.get().placeholder, x, y, direction, CollisionMode.REGULAR, 10.0);

        _active = true;
        _velocity.x = _direction * 15.0 / Game.FRAME_RATE;
    }

    public boolean isActive()
    {
        return _active;
    }

    public boolean isEmpty()
    {
        return _captive == null;
    }

    public Enemy get_captive() { return _captive; }

    public void capture(Enemy enemy)
    {
        _captive = enemy;
        set_position(enemy.getX(), enemy.getY());
        _captive.setAnimation(Animations.CAPTURED);
        _timer = 8 * (int) Game.FRAME_RATE;
    }

    @Override
    public void movementRules()
    {
        if(_active && (_traveled >= _max_distance || _velocity.x == 0 || _captive != null))
        {
            if(_captive == null) { setAnimation(Animations.TMP_BUBBLE_FLY); }

            _velocity.x = 0;
            _velocity.y = -Game.GRAVITY / 4.0;
            _active = false;
        }

        if(!isEmpty())
        {
            _captive.set_position(_position);
            _timer--;
            if(_timer == 3 * Game.FRAME_RATE)
            {
                _captive.setAnimation(Animations.HATCH);
            }
            else if(_timer == 0)
            {
                _wasted = true;
            }
        }

        _traveled += _velocity.length();
    }

    @Override
    public void draw(GraphicsContext gc, int scale)
    {
        if(isEmpty()) { super.draw(gc, scale); }
        else { _captive.draw(gc, scale); }
    }
}
