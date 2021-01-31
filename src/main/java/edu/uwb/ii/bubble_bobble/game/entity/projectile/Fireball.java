package edu.uwb.ii.bubble_bobble.game.entity.projectile;

import edu.uwb.ii.bubble_bobble.game.collider.CollisionMode;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.game.utils.Vec2;
import edu.uwb.ii.bubble_bobble.scenes.game.Game;

public class Fireball extends Projectile
{
    public Fireball(double x, double y, Vec2 target, double speed)
    {
        super(ResourceManager.get().placeholder, x, y, 1, CollisionMode.NONE, 40.0);
        _velocity = target.substract(x, y).normalize().scale(speed / Game.FRAME_RATE);
        setAnimation(Animations.TMP_PROJECTILE);
        _map_locked = false;
    }

    @Override
    public void movementRules()
    {
        _max_distance -= _velocity.length();
        if(_max_distance < 0.0)
        {
            _wasted = true;
        }
    }
}
