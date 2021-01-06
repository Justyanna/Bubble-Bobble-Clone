package edu.uwb.ii.bubble_bobble.game.rendering;

public class ResourceManager {

    private static ResourceManager manager;

    public SpriteSheet placeholder;

    private ResourceManager() {

        placeholder = new SpriteSheet("placeholder", 8, 8);

    }

    public static ResourceManager get() {

        if(manager == null) {
            manager = new ResourceManager();
        }

        return manager;

    }

}
