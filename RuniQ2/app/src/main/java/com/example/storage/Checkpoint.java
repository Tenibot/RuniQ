package com.example.storage;

public class Checkpoint {
    private String hint;
    private Coordinates coordinates;

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getHint() {
        return hint;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
