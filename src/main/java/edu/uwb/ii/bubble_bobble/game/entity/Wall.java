package edu.uwb.ii.bubble_bobble.game.entity;

import edu.uwb.ii.bubble_bobble.game.Entity;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;

public class Wall extends Entity {

    private Wall(int w, int h) {

        super(ResourceManager.get().placeholder, Animations.IDLE, w, h);

    }

    public static Wall tiny() {
        return new Wall(1, 1);
    }

    public static Wall wide() {
        return new Wall(2, 1);
    }

    public static Wall tall() {
        return new Wall(1, 2);
    }

    public static Wall huge() {
        return new Wall(2, 2);
    }

    @Override
    public double [] movementRules(boolean [][] map) {
        return new double[] {0, 0};
    }
}
