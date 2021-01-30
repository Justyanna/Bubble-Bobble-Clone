package edu.uwb.ii.bubble_bobble.game.rendering;

public interface Animations {

    Animation IDLE = new Animation(0, 2, 4);
    Animation WALK = new Animation(2, 2, 4);
    Animation ANGRY = new Animation(4, 2, 6);
    Animation CAPTURED = new Animation(6, 2, 4);
    Animation HATCH = new Animation(8, 2, 6);
    Animation DEAD = new Animation(10, 2, 2);

    Animation SHOOT = new Animation(12, 2, 4);
    Animation FALL = SHOOT.copy();
    Animation ASCEND = WALK.copy();
    Animation DESCEND = SHOOT.copy();
    Animation DIVE = new Animation(14, 2, 6);
    Animation TURN = SHOOT.copy();
    Animation CRY = SHOOT.copy();

    Animation TMP_PLAYER = new Animation(8, 2, 4);
    Animation TMP_BUBBLE_FLY = new Animation(32, 1, 0);
    Animation TMP_BUBBLE_RUSH = new Animation(33, 1, 0);
    Animation TMP_PROJECTILE = new Animation(40, 2, 4);

}
