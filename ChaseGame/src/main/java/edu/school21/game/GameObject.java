package edu.school21.game;

public abstract class GameObject {
    private int x;
    private int y;
    private char objectChar;
    private String objectColor;


    public GameObject(char objectChar, String objectColor) {
        this.objectChar = objectChar;
        this.objectColor = objectColor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getObjectChar() {
        return objectChar;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getObjectColor() {
        return objectColor;
    }
}
