package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Player;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.entity.enemy.Walker;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;

public class Game {

    public final static double GRAVITY = 10.0 / 60.0;

    private Map _level;

    private Player _player;
    private ArrayList<Enemy> _enemies;
    private ArrayList<Projectile> _bubbles;
    private ArrayList<Projectile> _projectiles;

    public Game() {

        _enemies = new ArrayList<>();
        _bubbles = new ArrayList<>();
        _projectiles = new ArrayList<>();

        _player = new Player(10, 10, App.getInputs(), _bubbles);

        start();

    }

    public void start() {

        _level = new Map("map_01", ResourceManager.get().placeholder);

    }

    public void update(GraphicsContext gc, int scale) {

//        -- moving

        for(Enemy e : _enemies)
            e.move(_level);
        _player.move(_level);

        for(Projectile b : _bubbles)
            b.move(_level);
        for(Projectile p : _projectiles)
            p.move(_level);

//        -- drawing

        _level.draw(gc, scale);
        for(Enemy e : _enemies)
            e.draw(gc, scale);
        _player.draw(gc, scale);

        for(Projectile b : _bubbles)
            b.draw(gc, scale);
        for(Projectile p : _projectiles)
            p.draw(gc, scale);

        if(_enemies.size() < 1) {
            _enemies.add(new Walker(15, 17, _level));
        }

//        -- collisions

        for(Iterator<Enemy> it = _enemies.iterator(); it.hasNext();) {
            Enemy e = it.next();
            if(e.collide(_player))
                it.remove();
        }

    }

    public void pause() {

    }

    private void nextLevel() {

    }

}
