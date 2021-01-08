package edu.uwb.ii.bubble_bobble.scenes.editor;

import java.util.List;

public class Map {

    List<Wall> walls;
    Player player;
    List<Enemies> enemies;

    public Map(List<Wall> walls, Player player, List<Enemies> enemies) {
        this.walls = walls;
        this.player = player;
        this.enemies = enemies;
    }
}
