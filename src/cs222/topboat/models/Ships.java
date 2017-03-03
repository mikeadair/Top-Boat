package cs222.topboat.models;

public enum Ships {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    SUBMARINE(3),
    DESTROYER(2);

    Ships(int l) {
        this.length = l;
    }

    public int length;
}
