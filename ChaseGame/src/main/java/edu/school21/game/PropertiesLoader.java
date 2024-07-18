package edu.school21.game;

import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.Properties;

public class PropertiesLoader {
    private String mode;
    private String fileName;
    private char playerChar;
    private char enemyChar;
    private char wallChar;
    private char goalChar;
    private char emptyChar;
    private String playerColor;
    private String enemyColor;
    private String wallColor;
    private String goalColor;
    private String emptyColor;

    public PropertiesLoader(String mode) {
        this.mode = mode;
        if (mode.equals("dev")) {
            this.fileName = "application-dev.properties";
        } else {
            this.fileName = "application-production.properties";
        }
    }

    public String getMode() {
        return mode;
    }

    public char getPlayerChar() {
        return playerChar;
    }

    public char getEnemyChar() {
        return enemyChar;
    }

    public char getWallChar() {
        return wallChar;
    }

    public char getGoalChar() {
        return goalChar;
    }

    public char getEmptyChar() {
        return emptyChar;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public String getEnemyColor() {
        return enemyColor;
    }

    public String getWallColor() {
        return wallColor;
    }

    public String getGoalColor() {
        return goalColor;
    }

    public String getEmptyColor() {
        return emptyColor;
    }

    public void load() {
        Properties properties = new Properties();
        try (InputStream inputStream = Main.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (inputStream == null) {
                System.out.println("Unable to find " + fileName);
                return;
            }
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.print(e.getMessage());
            return;
        }
        try {
            playerChar = getCharProperty(properties, "player.char");
            enemyChar = getCharProperty(properties, "enemy.char");
            wallChar = getCharProperty(properties, "wall.char");
            goalChar = getCharProperty(properties, "goal.char");
            emptyChar = getCharProperty(properties, "empty.char");
            playerColor = getProperty(properties, "player.color");
            enemyColor = getProperty(properties, "enemy.color");
            wallColor = getProperty(properties, "wall.color");
            goalColor = getProperty(properties, "goal.color");
            emptyColor = getProperty(properties, "empty.color");
        } catch (MissingPropertyException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }

    private String getProperty(Properties properties, String key)
            throws MissingPropertyException {
        String value = properties.getProperty(key);
        if (value != null) {
            return value.trim();
        } else {
            throw new MissingPropertyException("Missing property: " + key);
        }
    }

    private char getCharProperty(Properties properties, String key)
            throws MissingPropertyException {
        String value = properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return value.charAt(0);
        } else if (value != null) {
            return ' ';
        } else {
            throw new MissingPropertyException("Missing property: " + key);
        }
    }



}
