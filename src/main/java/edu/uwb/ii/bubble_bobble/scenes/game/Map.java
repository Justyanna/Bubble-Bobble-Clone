package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.game.Collider;
import edu.uwb.ii.bubble_bobble.game.collider.MapCollider;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;
import edu.uwb.ii.bubble_bobble.game.utils.Position;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Map {

    public static final int ROWS = 26;
    public static final int COLUMNS = 32;

    private SpriteSheet _skin;

    private boolean [][] _body;
    private MapCollider _collider;

    private ArrayList<Position> _spawn_points;
    private ArrayList<String> _enemy_names;
    private ArrayList<Position> _enemy_spawns;

    public Map(String source, SpriteSheet skin) {

        _skin = skin;
        _body = new boolean[COLUMNS][ROWS];
        _collider = new MapCollider(this);
        _spawn_points = new ArrayList<>();

        load(source);

    }

    public Collider get_collider() { return _collider; }
    public Position getSpawn() { return _spawn_points.get(0); }
    public ArrayList<String> get_enemy_names() { return _enemy_names; }
    public ArrayList<Position> get_enemy_spawns() { return _enemy_spawns; }

    public boolean check(double x, double y) {

        return _body[((int) x + COLUMNS) % COLUMNS][((int) y + ROWS) % ROWS];

    }

    private void load(String name) {

        try {
            InputStream in = GameSceneController.class.getClassLoader().getResourceAsStream("maps/" + name + ".xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            doc.getDocumentElement().normalize();

            NodeList walls = doc.getElementsByTagName("Walls").item(0).getChildNodes();

            for(Node node = walls.item(0); node != null; node = node.getNextSibling()) {

                if(node.getNodeType() != Node.ELEMENT_NODE) continue;

                NamedNodeMap attr = node.getAttributes();
                Node x = attr.getNamedItem("x");
                Node y = attr.getNamedItem("y");

                if(x == null || y == null) continue;

                _body[Integer.parseInt(x.getNodeValue())][Integer.parseInt(y.getNodeValue())] = true;

            }

            NodeList players = doc.getElementsByTagName("Players").item(0).getChildNodes();

            for(Node player = players.item(0); player != null; player = player.getNextSibling()) {

                if(player.getNodeType() != Node.ELEMENT_NODE) continue;

                NamedNodeMap player_data = player.getAttributes();
                Node x = player_data.getNamedItem("x");
                Node y = player_data.getNamedItem("y");
                Node direction = player_data.getNamedItem("facing");

                if(x != null && y != null && direction != null) {
                    _spawn_points.add(new Position(
                            Integer.parseInt(x.getNodeValue()),
                            Integer.parseInt(y.getNodeValue()),
                            Integer.parseInt(direction.getNodeValue())));
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void draw(GraphicsContext gc, int scale) {

        for(int x = 0; x < COLUMNS; x++) {
            for(int y = 0; y < ROWS; y++) {
                if(_body[x][y]) {
                    _skin.draw(gc, 0, x * scale, y * scale, scale, scale);
                }
            }
        }

    }

}
