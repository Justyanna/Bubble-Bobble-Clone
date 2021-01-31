package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.App;
import edu.uwb.ii.bubble_bobble.game.entity.Enemy;
import edu.uwb.ii.bubble_bobble.game.entity.Player;
import edu.uwb.ii.bubble_bobble.game.entity.Projectile;
import edu.uwb.ii.bubble_bobble.game.entity.enemy.Wisp;
import edu.uwb.ii.bubble_bobble.game.entity.projectile.Bubble;
import edu.uwb.ii.bubble_bobble.game.rendering.Animations;
import edu.uwb.ii.bubble_bobble.game.rendering.ResourceManager;
import edu.uwb.ii.bubble_bobble.game.utils.Vec2;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public class Game
{
    private final static Random _rng = new Random();

    public final static double FRAME_RATE = 60.0;
    public final static double GRAVITY = 10.0 / Game.FRAME_RATE;
    private static Game __instance__;
    private int _countdown;
    private String _level_name;
    private Map _level;
    private int _score;
    private int _lives;
    private Player _player;
    private ArrayList<Enemy> _enemies;
    private ArrayList<Projectile> _bubbles;
    private ArrayList<Projectile> _hostile_projectiles;
    private boolean _started;
    private boolean _paused;
    private boolean _quit;

    public Game()
    {
        _started = false;
        _paused = false;
        _quit = false;
    }

    public static Game getInstance()
    {
        if(__instance__ == null)
        {
            __instance__ = new Game();
        }

        return __instance__;
    }

    public static Random get_rng()
    {
        return _rng;
    }

    public ArrayList<Projectile> get_hostile_projectiles()
    {
        return _hostile_projectiles;
    }

    public boolean quit()
    {
        return _quit;
    }

    public Vec2 getPlayerPosition()
    {
        return new Vec2(_player.getX(), _player.getY());
    }

    public Stream<Vec2> getEnemyPositions()
    {
        return _enemies.stream().map(Enemy::get_position);
    }

    public ArrayList<Projectile> get_bubbles()
    {
        return _bubbles;
    }

    public int get_lives() { return _lives; }

    public void start(String level, boolean isResource)
    {
        try
        {
            _level_name = level;
            _level = new Map(_level_name, ResourceManager.get().placeholder, isResource);

            _enemies = new ArrayList<>();
            _bubbles = new ArrayList<>();
            _hostile_projectiles = new ArrayList<>();

            String[] player_data = _level.get_spawn_points().get(0).split(" ");

            int x = Integer.parseInt(player_data[0]);
            int y = Integer.parseInt(player_data[1]);
            int direction = Integer.parseInt(player_data[2]);

            x = direction < 0 ? x - 1 : x;

            _player = new Player(x, y, direction, _bubbles);

            for(String enemy : _level.get_enemies())
            {
                String[] enemy_data = enemy.split(" ");

                x = Integer.parseInt(enemy_data[1]);
                y = Integer.parseInt(enemy_data[2]);
                direction = Integer.parseInt(enemy_data[3]);

                try
                {
                    Class enemy_type = Class.forName("edu.uwb.ii.bubble_bobble.game.entity.enemy." + enemy_data[0]);
                    _enemies.add((Enemy) enemy_type.getDeclaredConstructor(int.class, int.class, int.class, Map.class)
                                                   .newInstance(x, y, direction, _level));
                }
                catch(Exception e)
                {
                    System.out.println("Enemy type not found");
                    continue;
                }
            }

            _started = true;
        }
        catch(Exception e)
        {
            _quit = true;
            return;
        }
    }

    public void start()
    {
        _score = 0;
        _lives = 3;

        _quit = false;
        _paused = false;
        _started = true;
        _countdown = (int) Game.FRAME_RATE * 5;

        if(App.customMapName.isEmpty())
        {
            this.start("StoryMode#1", true);
        }
        else
        {
            this.start(App.customMapName, false);
        }
    }

    public void update(GraphicsContext gc, int scale)
    {
        if(!_started || _quit)
        {
            return;
        }

        if(!_paused && _countdown < 1)
        {
//            -- collisions

            for(Enemy e : _enemies)
            {
                if(e.collide(_player) && !_player.isInvincible() && !_player.isMourning())
                {
                    if(--_lives > 0)
                    {
                        _player.die();
                    }
                    else
                    {
                        _quit = true;
                        return;
                    }
                }
            }

            for(Iterator<Projectile> it = _bubbles.iterator(); it.hasNext(); )
            {
                Bubble b = (Bubble) it.next();
                if(b.isActive())
                {

                    int i = 0;
                    for(Enemy e : _enemies)
                    {
                        if(b.collide(e))
                        {
                            b.capture(_enemies.remove(i));
                            break;
                        }
                        i++;
                    }
                }
                else if(b.collide(_player))
                {
                    if(_player.get_controls().jump && _player.getBottom() < b.getY())
                    {
                        _player.jump();
                    }
                    else
                    {
                        _score += b.isEmpty() ? 10 : 150;
                        it.remove();
                    }
                }
                else if(b.isWasted())
                {
                    if(b.isWasted())
                    {
                        if(!b.isEmpty())
                        {
                            Enemy e = b.get_captive();
                            e.getAngry();
                            _enemies.add(e);
                        }
                        it.remove();
                    }
                }
            }

            for(Iterator<Projectile> it = _hostile_projectiles.iterator(); it.hasNext(); )
            {
                Projectile p = it.next();

                if(p.collide(_player) && !_player.isInvincible() && !_player.isMourning())
                {
                    if(--_lives > 0)
                    {
                        _player.die();
                    }
                    else
                    {
                        _quit = true;
                        return;
                    }
                }

                if(p.isWasted())
                {
                    it.remove();
                }
            }

//            -- moving

            for(Enemy e : _enemies)
            {
                e.move(_level);
            }
            _player.move(_level);

            for(Projectile b : _bubbles)
            {
                b.move(_level);
            }
            for(Projectile p : _hostile_projectiles)
            {
                p.move(_level);
            }
        }
        else if(_countdown > 0)
        {
            if(--_countdown == 0)
            {
                for(Enemy e : _enemies)
                {
                    e.setAnimation(e instanceof Wisp ? Animations.DESCEND : Animations.WALK);
                }
            }
        }

//        -- drawing

        _level.draw(gc, scale);

        for(Enemy e : _enemies)
        {
            e.draw(gc, scale);
        }
        _player.draw(gc, scale);

        for(Projectile b : _bubbles)
        {
            b.draw(gc, scale);
        }
        for(Projectile p : _hostile_projectiles)
        {
            p.draw(gc, scale);
        }

//        -- win condition

        int captured_enemies = 0;

        for(Projectile b : _bubbles)
        {
            captured_enemies += ((Bubble) b).isEmpty() ? 0 : 1;
        }

        if(_enemies.size() < 1 && captured_enemies < 1)
        {
            if(App.customMapName.isEmpty())
            {
                nextLevel();
            }
            else
            {
                _quit = true;
                quit();
            }
        }
    }

    public void togglePause()
    {
        _paused = !_paused;
    }

    private void nextLevel()
    {
        String[] level_data = _level_name.split("#");

        if(level_data.length < 1)
        {
            _quit = true;
            return;
        }

        int id = Integer.parseInt(level_data[1]) + 1;
        start(level_data[0] + "#" + id, true);
    }

    public int get_score()
    {
        return _score;
    }
}
