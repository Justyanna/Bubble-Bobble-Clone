package edu.uwb.ii.bubble_bobble.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteSheet {

    private static final String imgPath = "file:src/main/resources/img/";

    private Image _source;
    private int _rows;
    private int _columns;
    private int _width;
    private int _height;
    private int _n;

    public SpriteSheet (String source, int rows, int columns) {

        _source = new Image(imgPath + source + ".png");
        _rows = rows;
        _columns = columns;
        _width = (int) (_source.getWidth() / columns);
        _height = (int) (_source.getHeight() / rows);

    }

    public void draw(GraphicsContext gc, int sprite, int x, int y, int w, int h) {

        int sx = sprite % _columns * _width;
        int sy = sprite / _columns * _height;
        gc.drawImage(_source, sx, sy, _width, _height, x, y, w, h);

    }

}
