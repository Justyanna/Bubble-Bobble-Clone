package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Player;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.entity.enemy.Errant;
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

        start();

    }

    public void start() {

        _level = new Map("map_01", ResourceManager.get().placeholder);

        String [] player_data = _level.get_spawn_points().get(0).split(" ");

        int x = Integer.parseInt(player_data[0]);
        int y = Integer.parseInt(player_data[1]);
        int direction = Integer.parseInt(player_data[2]);

        _player = new Player(x, y, direction, _bubbles);

        for(String enemy : _level.get_enemies()) {

            String [] enemy_data = enemy.split(" ");

            System.out.println(enemy_data[0]);
            x = Integer.parseInt(enemy_data[1]);
            y = Integer.parseInt(enemy_data[2]);
            direction = Integer.parseInt(enemy_data[3]);

            _enemies.add(new Errant(x, y, direction, _level));

        }

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
