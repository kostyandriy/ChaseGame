package edu.school21.game;

import edu.school21.logic.ChaseLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InitialGenerator {
    private final int width;
    private final List<GameObject> gameObjects;
    private final PropertiesLoader loader;

    public InitialGenerator(int width, PropertiesLoader loader) {
        this.width = width;
        this.loader = loader;
        this.gameObjects = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void generate(int wallCount, int enemyCount) {
        loader.load();
        Integer[] step = null;
        while (step == null) {
            gameObjects.clear();
            for (int i = 0; i < wallCount; i++) {
                addObject(new Wall(loader.getWallChar(), loader.getWallColor()));
            }
            for (int i = 0; i < enemyCount; i++) {
                addObject(new Enemy(loader.getEnemyChar(),
                        loader.getEnemyColor()));
            }

            Player player = new Player(loader.getPlayerChar(),
                    loader.getPlayerColor());
            addObject(player);
            int emptyCount = width * width - (enemyCount + wallCount + 2);
            for (int i = 0; i < emptyCount; i++) {
                addObject(new Empty(loader.getEmptyChar(),
                        loader.getEmptyColor()));
            }

            Goal goal = new Goal(loader.getGoalChar(), loader.getGoalColor());
            addObject(goal);

            ChaseLogic logic = new ChaseLogic(
                    GameProcess.gameObjectsToFullField(gameObjects, width));
            step = logic.getNextStep(player.getX(), player.getY(),
                    goal.getX(), goal.getY(), "player");
        }

    }


    private void addObject(GameObject gameObject) {
        while (true) {
            int x = new Random().nextInt(width);
            int y = new Random().nextInt(width);
            if (isFreePosition(x, y)) {
                gameObject.setX(x);
                gameObject.setY(y);
                gameObjects.add(gameObject);
                break;
            }
        }
    }

    private boolean isFreePosition(int x, int y) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getX() == x && gameObject.getY() == y) {
                return false;
            }
        }
        return true;
    }
}
