package edu.school21.game;

import com.diogonunes.jcolor.Attribute;
import com.diogonunes.jcolor.Ansi;

import java.io.IOException;
import java.util.List;

public class Field {
    private final int width;
    private final List<GameObject> gameObjects;
    private final PropertiesLoader loader;

    public Field(int width, List<GameObject> gameObjects,
                 PropertiesLoader loader) {
        this.width = width;
        this.gameObjects = gameObjects;
        this.loader = loader;
    }

    public int getWidth() {
        return width;
    }

    public Player getPlayer() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Player) {
                return (Player) gameObject;
            }
        }
        return null;
    }

    public char getEmptyChar() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Empty) {
                return gameObject.getObjectChar();
            }
        }
        return loader.getEmptyChar();
    }

    public void generate() {
        if (loader.getMode().equals("production")) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                String charStr = String.valueOf(getGameObjectChar(x, y));
                Attribute backColor = getGameObjectColor(x, y);
                Attribute charColor = Attribute.BLACK_TEXT();
                System.out.print(Ansi.colorize(charStr, backColor, charColor));
            }
            System.out.println();
        }
    }

    public char getGameObjectChar(int x, int y) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getX() == x && gameObject.getY() == y) {
                return gameObject.getObjectChar();
            }
        }
        return getEmptyChar();
    }

    public Attribute getGameObjectColor(int x, int y) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getX() == x && gameObject.getY() == y) {
                return toColor(String.valueOf(gameObject.getObjectColor()));
            }
        }
        return toColor(loader.getEmptyColor());
    }

    private Attribute toColor(String color) {
        switch (color) {
            case "BLACK":
                return Attribute.BLACK_BACK();
            case "BLUE":
                return Attribute.BLUE_BACK();
            case "GREEN":
                return Attribute.GREEN_BACK();
            case "CYAN":
                return Attribute.CYAN_BACK();
            case "MAGENTA":
                return Attribute.MAGENTA_BACK();
            case "RED":
                return Attribute.RED_BACK();
            case "WHITE":
                return Attribute.WHITE_BACK();
            case "YELLOW":
                return Attribute.YELLOW_BACK();
            default:
                System.out.println("Available colors: BLACK, BLUE, GREEN," +
                        " CYAN, MAGENTA, RED, WHITE, YELLOW");
                System.exit(-1);
        }
        return null;
    }
}
