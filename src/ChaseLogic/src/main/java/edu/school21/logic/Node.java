package edu.school21.logic;

import java.util.Objects;

class Node implements Comparable<Node> {
    int x, y;
    Node parent;
    double g, h;

    public Node(int x, int y, Node parent, double g, double h) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.g = g;
        this.h = h;
    }

    public double f() {
        return g + h;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.f(), other.f());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}