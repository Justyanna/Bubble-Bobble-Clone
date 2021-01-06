package edu.uwb.ii.bubble_bobble.game.collider;

import edu.uwb.ii.bubble_bobble.game.Collider;

import java.util.ArrayList;

public class ColliderGroup implements Collider {

    private ArrayList<Collider> _colliders;

    public ColliderGroup() {
        _colliders = new ArrayList<>();
    }

    public void add(Collider c) {
        _colliders.add(c);
    }

    @Override
    public boolean test(Collider trigger) {
        for (Collider collider : _colliders) {
            if (collider.test(trigger))
                return true;
        }
        return false;
    }
}
