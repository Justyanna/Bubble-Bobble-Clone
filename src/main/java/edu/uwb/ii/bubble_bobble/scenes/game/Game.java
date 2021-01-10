package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Player;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Bubble;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;

public class Game {

    public final static double FRAME_RATE = 60.0;
    public final static double GRAVITY = 10.0 / Game.FRAME_RATE;

    private String _level_name;
    private Map _level;

    private int _score;

    private Player _player;
    private ArrayList<Enemy> _enemies;
    private ArrayList<Projectile> _bubbles;
    private ArrayList<Projectile> _projectiles;

    private boolean _started;
    private boolean _paused;
    private boolean _quit;

    public Game() {

        _started = false;
        _paused = false;
        _quit = false;

    }

    public boolean quit() {
        return _quit;
    }

    public void start(String level) {

        try {
            _level_name = level;
            _level = new Map(_level_name, ResourceManager.get().placeholder);

            _enemies = new ArrayList<>();
            _bubbles = new ArrayList<>();
            _projectiles = new ArrayList<>();

            String[] player_data = _level.get_spawn_points().get(0).split(" ");

            int x = Integer.parseInt(player_data[0]);
            int y = Integer.parseInt(player_data[1]);
            int direction = Integer.parseInt(player_data[2]);

            _player = new Player(x, y, direction, _bubbles);

            for (String enemy : _level.get_enemies()) {

                String[] enemy_data = enemy.split(" ");

                x = Integer.parseInt(enemy_data[1]);
                y = Integer.parseInt(enemy_data[2]);
                direction = Integer.parseInt(enemy_data[3]);

                try {
                    Class enemy_type = Class.forName("edu.uwb.ii.bubble_bobble.game.entity.enemy." + enemy_data[0]);
                    _enemies.add((Enemy) enemy_type.getDeclaredConstructor(int.class, int.class, int.class, Map.class)
                            .newInstance(x, y, direction, _level));
                } catch (Exception e) {
                    System.out.println("Enemy type not found");
                    continue;
                }

            }

            _started = true;

        } catch (Exception e) {
            _quit = true;
            return;
        }

    }

    public void start() {

        _score = 0;
        this.start("StoryMode#1");

    }

    public void update(GraphicsContext gc, int scale) {

        if (!_started || _quit) return;

        if (!_paused) {

//            -- collisions

            for (Enemy e : _enemies)
                if (e.collide(_player)) {
                    _quit = true;
                    return;
                }

            for (Iterator<Projectile> it = _bubbles.iterator(); it.hasNext(); ) {

                Bubble b = (Bubble) it.next();
                if (b.isActive()) {

                    int i = 0;
                    for (Enemy e : _enemies) {
                        if (b.collide(e)) {
                            b.capture(_enemies.remove(i));
                            break;
                        }
                        i++;
                    }

                } else if (b.collide(_player)) {
                    _score += b.isEmpty() ? 10 : 150;
                    it.remove();
                }
            }

//            -- moving

            for (Enemy e : _enemies)
                e.move(_level);
            _player.move(_level);

            for (Projectile b : _bubbles)
                b.move(_level);
            for (Projectile p : _projectiles)
                p.move(_level);

        }

//        -- drawing

        _level.draw(gc, scale);

        for (Enemy e : _enemies)
            e.draw(gc, scale);
        _player.draw(gc, scale);

        for (Projectile b : _bubbles)
            b.draw(gc, scale);
        for (Projectile p : _projectiles)
            p.draw(gc, scale);

//        -- win condition

        int captured_enemies = 0;

        for (Projectile b : _bubbles)
            captured_enemies += ((Bubble) b).isEmpty() ? 0 : 1;

        if (_enemies.size() < 1 && captured_enemies < 1) {
            nextLevel();
        }

    }

    public void togglePause() {

        _paused = !_paused;

    }

    private void nextLevel() {

        String[] level_data = _level_name.split("#");

        if (level_data.length < 1) {
            _quit = true;
            return;
        }

        int id = Integer.parseInt(level_data[1]) + 1;
        start(level_data[0] + "#" + id);

    }

}
