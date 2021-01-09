package edu.uwb.ii.bubble_bobble.scenes.game;

import edu.uwb.ii.bubble_bobble.game.Collider;
import edu.uwb.ii.bubble_bobble.game.collider.MapCollider;
import edu.uwb.ii.bubble_bobble.game.rendering.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Map {

    public static final int ROWS = 26;
    public static final int COLUMNS = 32;

    private SpriteSheet _skin;

    private boolean [][] _body;
    private MapCollider _collider;

    public Map(String source, SpriteSheet skin) {

        _skin = skin;
        _body = new boolean[COLUMNS][ROWS];
        _collider = new MapCollider(this);

        load(source);

    }

    public Collider get_collider() { return _collider; }

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

            var walls = doc.getElementsByTagName("Walls").item(0).getChildNodes();

            for(var node = walls.item(0); node != null; node = node.getNextSibling()) {

                if(node.getNodeType() == 1) {

                    var attr = node.getAttributes();
                    var x = attr.getNamedItem("x");
                    var y = attr.getNamedItem("y");

                    if(x == null || y == null) continue;

                    _body[Integer.parseInt(x.getNodeValue())][Integer.parseInt(y.getNodeValue())] = true;

                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        for(int x = 0; x < COLUMNS; x++) {
//            for(int y = 0; y < ROWS; y++) {
//                if(y == 0 || y == ROWS - 1 || x == 0 || x == COLUMNS - 1) {
//                    if(x == 14 || x == 15 || x == 16 || x == 17) continue;
//                    _body[x][y] = true;
//                }
//            }
//        }
//
//        _body[14][20] = true;
//        _body[15][20] = true;
//        _body[16][20] = true;
//        _body[17][20] = true;

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
