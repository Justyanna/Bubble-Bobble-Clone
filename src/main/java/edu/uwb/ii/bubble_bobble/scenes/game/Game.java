package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Player;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.entity.enemy.Errant;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Bubble;
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

            x = Integer.parseInt(enemy_data[1]);
            y = Integer.parseInt(enemy_data[2]);
            direction = Integer.parseInt(enemy_data[3]);

            try {
                Class enemy_type = Class.forName("edu.uwb.ii.bubble_bobble.game.entity.enemy." + enemy_data[0]);
                _enemies.add((Enemy) enemy_type.getDeclaredConstructor(int.class, int.class, int.class, Map.class)
                        .newInstance(x, y, direction, _level));
            }
            catch(Exception e) {
                System.out.println("Enemy type not found");
                continue;
            }

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

        for(Iterator<Projectile> it = _bubbles.iterator(); it.hasNext();) {

            Bubble b = (Bubble) it.next();
            if(b.isActive()) {

                int i = 0;
                for(Enemy e : _enemies) {
                    if(b.collide(e)) {
                        b.capture(_enemies.remove(i));
                        break;
                    }
                    i++;
                }

            }
            else if(b.collide(_player)) {
                it.remove();
            }


        }

    }

    public void pause() {

    }

    private void nextLevel() {

    }

}
