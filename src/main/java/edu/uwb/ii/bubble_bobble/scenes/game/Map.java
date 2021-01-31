package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.game.Collider;
import edu.uwb.ii.bubble_bobble.game.collider.MapCollider;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;

public class Map {

    public static final int ROWS = 26;
    public static final int COLUMNS = 32;
    private static final String MAPS_PATH = System.getProperty("user.home") + "/AppData/Local/Bubble Bobble Clone/maps";
    private SpriteSheet _skin;
    private boolean[][] _body;
    private MapCollider _collider;
    private ArrayList<String> _spawn_points;
    private ArrayList<String> _enemies;

    public Map(String source, SpriteSheet skin, boolean isResource) {
        _skin = skin;
        _body = new boolean[COLUMNS][ROWS];
        _collider = new MapCollider(this);
        _spawn_points = new ArrayList<>();
        _enemies = new ArrayList<>();

        load(source, isResource);
    }

    public Collider get_collider() { return _collider; }

    public ArrayList<String> get_spawn_points() { return _spawn_points; }

    public ArrayList<String> get_enemies() { return _enemies; }

    public boolean check(double x, double y)
    {
        return _body[((int) x + COLUMNS) % COLUMNS][((int) y + ROWS) % ROWS];
    }

    public boolean checkTest(double x, double y)
    {
        System.out.println(((int) x + COLUMNS) % COLUMNS + " " + ((int) y + ROWS) % ROWS);
        return _body[((int) x + COLUMNS) % COLUMNS][((int) y + ROWS) % ROWS];
    }

    private void load(String name, boolean isResource)
    {
        try
        {
            InputStream in;
            if(isResource)
            {
                in = GameSceneController.class.getClassLoader().getResourceAsStream("maps/" + name + ".xml");
            }
            else
            {
                in = new FileInputStream(new File(MAPS_PATH + "/" + name));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);
            doc.getDocumentElement().normalize();

            NodeList walls = doc.getElementsByTagName("Walls").item(0).getChildNodes();

            for (Node node = walls.item(0); node != null; node = node.getNextSibling()) {

                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                NamedNodeMap attr = node.getAttributes();
                Node x = attr.getNamedItem("x");
                Node y = attr.getNamedItem("y");

                if (x == null || y == null) {
                    continue;
                }

                _body[Integer.parseInt(x.getNodeValue())][Integer.parseInt(y.getNodeValue())] = true;
            }

            NodeList players = doc.getElementsByTagName("Players").item(0).getChildNodes();

            for (Node player = players.item(0); player != null; player = player.getNextSibling()) {

                if (player.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                NamedNodeMap player_data = player.getAttributes();
                Node x = player_data.getNamedItem("x");
                Node y = player_data.getNamedItem("y");
                Node direction = player_data.getNamedItem("facing");

                if (x != null && y != null && direction != null) {
                    _spawn_points.add(x.getNodeValue() + " " + y.getNodeValue() + " " + direction.getNodeValue());
                }
            }

            NodeList enemies = doc.getElementsByTagName("Enemies").item(0).getChildNodes();

            for (Node enemy = enemies.item(0); enemy != null; enemy = enemy.getNextSibling()) {

                if (enemy.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                NamedNodeMap enemy_data = enemy.getAttributes();
                Node x = enemy_data.getNamedItem("x");
                Node y = enemy_data.getNamedItem("y");
                Node direction = enemy_data.getNamedItem("facing");

                if (x != null && y != null && direction != null) {
                    _enemies.add(enemy.getNodeName() + " " + x.getNodeValue() + " " + y.getNodeValue() + " " +
                                         direction.getNodeValue());
                }
            }
        } catch (IndexOutOfBoundsException | IOException | NullPointerException | ParserConfigurationException | SAXException e) {
        }
    }

    public void draw(GraphicsContext gc, int scale) {
        for (int x = 0; x < COLUMNS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if (_body[x][y]) {
                    _skin.draw(gc, 0, x * scale, y * scale, scale, scale);
                }
            }
        }
    }
}
