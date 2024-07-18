package edu.school21.game;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.util.List;

@Parameters(separators = "=")

public class GameManager {
    @Parameter(names = {"--size"}, description = "Width of matrix",
            required = true)
    private int size;

    @Parameter(names = {"--enemiesCount"}, description = "Count of enemies",
            required = true)
    private int enemiesCount;

    @Parameter(names = {"--wallsCount"}, description = "Count of walls",
            required = true)
    private int wallsCount;

    @Parameter(names = {"--profile"}, description = "Mode of execution",
            required = true)
    private String mode;

    public void start(String[] args) {
        parseArgs(args);
        PropertiesLoader loader = new PropertiesLoader(mode);
        InitialGenerator generator = new InitialGenerator(size, loader);
        generator.generate(wallsCount, enemiesCount);
        List<GameObject> gameObjects = generator.getGameObjects();
        Field field = new Field(size, gameObjects, loader);
        GameProcess process = new GameProcess(field, loader, gameObjects);
        process.start();
    }

    private void parseArgs(String[] args) {
        try {
            JCommander.newBuilder().addObject(this).build().parse(args);
            testArgs();
        } catch (IllegalParametersException | ParameterException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private void testArgs() {
        if (size < 2) {
            throw new IllegalParametersException("Error: " +
                    "width can't be less than 2");
        }
        if (size > 50) {
            throw new IllegalParametersException("Error: " +
                    "width can't be more than 50");
        }
        if (enemiesCount < 0) {
            throw new IllegalParametersException("Error: " +
                    "count of enemies can't be less than 0");
        }
        if (wallsCount < 0) {
            throw new IllegalParametersException("Error: " +
                    "count of walls can't be less than 0");
        }
        if (size * size < enemiesCount + wallsCount + 2) {
            throw new IllegalParametersException("Error: " +
                    "field size is not enough");
        }
        if (!(mode.equals("production") || mode.equals("dev"))) {
            throw new IllegalParametersException("Error: " +
                    "wrong profile. Available profiles: production and dev");
        }
    }

}
