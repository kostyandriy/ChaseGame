package edu.school21.logic;

import java.util.*;

public class ChaseLogic {

    private Integer[][] fieldFull;

    public ChaseLogic(Integer[][] fieldFull) {
        this.fieldFull = fieldFull;
    }

    public Integer[] getNextStep(int startX, int startY,
                                 int goalX, int goalY, String finder) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        HashSet<Node> closedList = new HashSet<>();

        Node startNode = new Node(startX, startY, null,
                0, heuristic(startX, startY, goalX, goalY));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.x == goalX && current.y == goalY) {
                return reconstructNextStep(current);
            }

            closedList.add(current);

            for (Node neighbor : getNeighbors(current, finder)) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                double tentativeG = current.g + 1;
                boolean betterG = false;

                if (!openList.contains(neighbor)) {
                    neighbor.h = heuristic(neighbor.x,
                            neighbor.y, goalX, goalY);
                    neighbor.g = tentativeG;
                    openList.add(neighbor);
                    betterG = true;
                } else if (tentativeG < neighbor.g) {
                    openList.remove(neighbor);
                    neighbor.g = tentativeG;
                    openList.add(neighbor);
                    betterG = true;
                }

                if (betterG) {
                    neighbor.parent = current;
                }
            }
        }

        return null;
    }

    private int heuristic(int x, int y, int goalX, int goalY) {
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }

    private Integer[] reconstructNextStep(Node goalNode) {
        Node step = goalNode;
        while (step.parent != null && step.parent.parent != null) {
            step = step.parent;
        }
        return new Integer[]{step.x, step.y};
    }

    private List<Node> getNeighbors(Node node, String finder) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newX = node.x + dir[0];
            int newY = node.y + dir[1];
            if (finder.equals("enemy")) {
                if (newX >= 0 && newX < fieldFull.length && newY >= 0
                        && newY < fieldFull[0].length
                        && fieldFull[newX][newY] != 4
                        && fieldFull[newX][newY] != 3
                        && fieldFull[newX][newY] != 2) {
                    neighbors.add(new Node(newX, newY, node, node.g + 1, 0));
                }
            }
            if (finder.equals("player")) {
                if (newX >= 0 && newX < fieldFull.length && newY >= 0
                        && newY < fieldFull[0].length
                        && fieldFull[newX][newY] != 3
                        && fieldFull[newX][newY] != 2) {
                    neighbors.add(new Node(newX, newY, node, node.g + 1, 0));
                }
            }
        }

        return neighbors;
    }

}
