package edu.uwb.ii.bubble_bobble.scenes.editor;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Arrays;

public class Map {

    private Cell[][] body;

    Map() {
        this.body = new Cell[32][26];
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 26; j++) {
                body[i][j] = new Cell(i, j, 1, "Empty");
            }
        }
    }

    void toggle(int x, int y, String input) {
        if (Arrays.stream(EditorSceneController.ids).anyMatch(id -> id == input)) {
            body[x][y].toggle(input);
        }
    }

    public Cell[][] getBody() {
        return body;
    }

    public Document generateFxml() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element root = document.createElement("Map");
        document.appendChild(root);

        Element walls = document.createElement("Walls");
        root.appendChild(walls);

        Element players = document.createElement("Players");
        root.appendChild(players);

        Element enemies = document.createElement("Enemies");
        root.appendChild(enemies);

        Element rules = document.createElement("Rules");
        root.appendChild(rules);

        for (Cell[] cellRow : body) {
            for (Cell cell : cellRow) {
                switch (cell.getId()) {
                    case "Empty":
                        continue;
                    case "Wall": {
                        Element wall = document.createElement("Wall");
                        Attr x = document.createAttribute("x");
                        x.setValue(String.valueOf(cell.getX()));
                        wall.setAttributeNode(x);

                        Attr y = document.createAttribute("y");
                        y.setValue(String.valueOf(cell.getY()));
                        wall.setAttributeNode(y);

                        walls.appendChild(wall);
                        break;
                    }
                    case "Player": {
                        Element player = document.createElement("Player");
                        Attr x = document.createAttribute("x");
                        x.setValue(String.valueOf(cell.getX()));
                        player.setAttributeNode(x);

                        Attr y = document.createAttribute("y");
                        y.setValue(String.valueOf(cell.getY()));
                        player.setAttributeNode(y);

                        Attr facing = document.createAttribute("facing");
                        facing.setValue(String.valueOf(cell.getFacing()));
                        player.setAttributeNode(facing);

                        players.appendChild(player);
                        break;
                    }
                    case "Rules": {
                        continue;
                    }
                    default: {
                        Element enemy = document.createElement("Enemy");
                        Attr x = document.createAttribute("x");
                        x.setValue(String.valueOf(cell.getX()));
                        enemy.setAttributeNode(x);

                        Attr y = document.createAttribute("y");
                        y.setValue(String.valueOf(cell.getY()));
                        enemy.setAttributeNode(y);

                        Attr facing = document.createAttribute("facing");
                        facing.setValue(String.valueOf(cell.getFacing()));
                        enemy.setAttributeNode(facing);

                        enemies.appendChild(enemy);
                        break;
                    }
                }
            }
        }

        return document;
    }

    public void importFormDocument(Document document) throws ParserConfigurationException {

        Element root = document.getDocumentElement();
        NodeList walls = root.getElementsByTagName("Wall");
        NodeList players = root.getElementsByTagName("Player");
        NodeList enemies = root.getElementsByTagName("Enemy");

        for (int i = 0; i < walls.getLength(); i++) {
            Node wall = walls.item(i);
            int x = Integer.parseInt(wall.getAttributes().getNamedItem("x").getTextContent());
            int y = Integer.parseInt(wall.getAttributes().getNamedItem("y").getTextContent());
            body[x][y].setId("Wall");
        }

        for (int i = 0; i < players.getLength(); i++) {
            Node player = players.item(i);
            int x = Integer.parseInt(player.getAttributes().getNamedItem("x").getTextContent());
            int y = Integer.parseInt(player.getAttributes().getNamedItem("y").getTextContent());
            int facing = Integer.parseInt(player.getAttributes().getNamedItem("facing").getTextContent());
            body[x][y].setId("Player");
            body[x][y].setFacing(facing);
        }

        for (int i = 0; i < enemies.getLength(); i++) {
            Node enemy = enemies.item(i);
            int x = Integer.parseInt(enemy.getAttributes().getNamedItem("x").getTextContent());
            int y = Integer.parseInt(enemy.getAttributes().getNamedItem("y").getTextContent());
            int facing = Integer.parseInt(enemy.getAttributes().getNamedItem("facing").getTextContent());
            body[x][y].setId("Enemy");
            body[x][y].setFacing(facing);
        }
    }
}
