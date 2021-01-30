package edu.uwb.ii.bubble_bobble.game.rendering;

public class ResourceManager {

    public static ResourceManager manager;

    public SpriteSheet placeholder;
    public SpriteSheet player;
    public SpriteSheet errant;
    public SpriteSheet nimrod;
    public SpriteSheet wisp;
    public SpriteSheet specter;

    public ResourceManager() {

        placeholder = new SpriteSheet("placeholder", 8, 8);

        player = new SpriteSheet("player", 4, 6);
        errant = new SpriteSheet("errant", 4, 6);
        nimrod = new SpriteSheet("nimrod", 4, 6);
        wisp = new SpriteSheet("wisp", 4, 6);
        specter = new SpriteSheet("specter", 4, 6);

    }

    public static ResourceManager get() {

        if(manager == null) {
            manager = new ResourceManager();
        }

        return manager;

    }

}
