package edu.school21.game;

import edu.school21.logic.ChaseLogic;

import java.util.*;

public class GameProcess {
    private final String looseMessage = "YOU DIED";
    private final String winMessage = "VICTORY ACHIEVED";
    private final Field field;
    private final PropertiesLoader loader;
    private List<GameObject> gameObjects;
    private ChaseLogic logic;

    public GameProcess(Field field, PropertiesLoader loader,
                       List<GameObject> gameObjects) {
        this.field = field;
        this.loader = loader;
        this.gameObjects = gameObjects;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        Player player = field.getPlayer();
        if (loader.getMode().equals("dev")) {
            field.generate();
        }
        while (true) {
            if (loader.getMode().equals("production")) {
                field.generate();
            }
            if (isGoal(player.getX(), player.getY())) {
                gameOver(winMessage);
            }
            checkPlayerSurrounded();
            logic = new ChaseLogic(gameObjectsToFullField(gameObjects, field.getWidth()));
            String input = scanner.nextLine();
            switch (input) {
                case "w":
                case "W":
                    makeStep(player, player.getX() - 1, player.getY(), 'w');
                    break;
                case "a":
                case "A":
                    makeStep(player, player.getX(), player.getY() - 1, 'a');
                    break;
                case "d":
                case "D":
                    makeStep(player, player.getX(), player.getY() + 1, 'd');
                    break;
                case "s":
                case "S":
                    makeStep(player, player.getX() + 1, player.getY(), 's');
                    break;
                case "9":
                    gameOver(looseMessage);
                    break;
                default:
                    continue;
            }
        }
    }

    private void makeStep(Player player, int nextX, int nextY, char letter) {
        playerStep(player, nextX, nextY, letter);
        if (loader.getMode().equals("dev")) {
            field.generate();
        }
        if (isPassable(nextX, nextY)) {
            enemyStep(player.getX(), player.getY());
        }
    }

    private void playerStep(Player player, int nextX, int nextY, char letter) {
        if (isInRange(nextX, nextY)) {
            if (isPassable(nextX, nextY)) {
                switch (letter) {
                    case 'w':
                    case 's':
                        player.setX(nextX);
                        break;
                    case 'a':
                    case 'd':
                        player.setY(nextY);
                        break;
                }
                if (isGoal(player.getX(), player.getY())) {
                    field.generate();
                    gameOver(winMessage);
                }
            }
        } else {
            gameOver(looseMessage);
        }
    }

    private void enemyStep(int goalX, int goalY) {
        for (GameObject enemy : gameObjects) {
            if (enemy instanceof Enemy) {
                chasePlayer((Enemy) enemy);
                if (loader.getMode().equals("dev")) {
                    String input;
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        System.out.print("Enter 8 -> ");
                        input = scanner.nextLine();
                        if (input.equals("8")) {
                            field.generate();
                            break;
                        }
                    }
                }
                if (enemy.getX() == goalX && enemy.getY() == goalY) {
                    if (loader.getMode().equals("production")) {
                        field.generate();
                    }
                    gameOver(looseMessage);
                }
            }
        }
    }

    private void chasePlayer(Enemy enemy) {
        Integer[] step;
        logic = new ChaseLogic(gameObjectsToFullField(
                gameObjects, field.getWidth()));
        int goalX = field.getPlayer().getX();
        int goalY = field.getPlayer().getY();
        step = logic.getNextStep(enemy.getX(), enemy.getY(),
                goalX, goalY, "enemy");
        if (step != null) {
            enemy.setX(step[0]);
            enemy.setY(step[1]);

        }
    }

    public static Integer[][] gameObjectsToFullField(
            List<GameObject> gameObjects, int width) {
        Integer[][] fieldFull = new Integer[width][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                fieldFull[x][y] = 0;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                for (GameObject object : gameObjects) {
                    if (object.getX() == x && object.getY() == y) {
                        String name = object.getClass().getSimpleName();
                        switch (name) {
                            case "Player":
                                fieldFull[x][y] = 1;
                                break;
                            case "Enemy":
                                fieldFull[x][y] = 2;
                                break;
                            case "Wall":
                                fieldFull[x][y] = 3;
                                break;
                            case "Goal":
                                fieldFull[x][y] = 4;
                                break;
                        }
                    }
                }
            }
        }
        return fieldFull;
    }

    private int getNeighborsCount(Integer[][] grid) {
        int playerX = field.getPlayer().getX();
        int playerY = field.getPlayer().getY();
        int count = 0;
        if (isInRange(playerX, playerY + 1) && (grid[playerX][playerY + 1] == 2
                || grid[playerX][playerY + 1] == 3)) {
            count++;
        }
        if (isInRange(playerX, playerY - 1) && (grid[playerX][playerY - 1] == 2
                || grid[playerX][playerY - 1] == 3)) {
            count++;
        }
        if (isInRange(playerX - 1, playerY) && (grid[playerX - 1][playerY] == 2
                || grid[playerX - 1][playerY] == 3)) {
            count++;
        }
        if (isInRange(playerX + 1, playerY) && (grid[playerX + 1][playerY] == 2
                || grid[playerX + 1][playerY] == 3)) {
            count++;
        }

        if (!isInRange(playerX - 1, playerY)
                || !isInRange(playerX + 1, playerY)) {
            count++;
        }
        if (!isInRange(playerX, playerY - 1)
                || !isInRange(playerX, playerY + 1)) {
            count++;
        }
        return count;
    }

    private void checkPlayerSurrounded() {
        int count = getNeighborsCount(gameObjectsToFullField(
                gameObjects, field.getWidth()));
        if (count >= 4) {
            gameOver(looseMessage);
        }
    }

    private boolean isInRange(int x, int y) {
        return x < field.getWidth() && y < field.getWidth() && x >= 0 && y >= 0;
    }

    private boolean isPassable(int x, int y) {
        char object = field.getGameObjectChar(x, y);
        return object != loader.getEnemyChar()
                && object != loader.getWallChar();
    }

    private boolean isGoal(int x, int y) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Goal) {
                return gameObject.getX() == x && gameObject.getY() == y;
            }
        }
        return false;
    }

    private void gameOver(String message) {
        System.out.println(message);
        System.exit(0);
    }

}
