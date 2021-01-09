package edu.uwb.ii.bubble_bobble.scenes.editor;

public class Cell {

    private int x;
    private int y;
    private int facing;
    private String id;

    public Cell(int x, int y, int facing, String id) {
        this.x = x;
        this.y = y;
        this.facing = facing;
        this.id = id;
    }

    String toggle(String input) {
        if (!id.equals(input)) {
            id = input;
            facing = 1;
        } else if (id.equals("Wall") || facing == -1) {
            id = "empty";
        } else {
            facing = -1;
        }
        return id.equals("Wall") || id.equals("empty")
                ? id.toLowerCase()
                : id.toLowerCase() + "-" + (facing > 0 ? "A" : "B");
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
