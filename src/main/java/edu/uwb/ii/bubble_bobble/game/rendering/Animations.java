package edu.uwb.ii.bubble_bobble.game.rendering;

public interface Animations {

    Animation IDLE = new Animation(0, 1, 1);
    Animation WALK = new Animation(1, 2, 4);
    Animation RUN = new Animation(1, 2, 8);

    Animation TMP_PLAYER = new Animation(8, 2, 4);
    Animation TMP_ENEMY = new Animation(16, 2, 4);

}
