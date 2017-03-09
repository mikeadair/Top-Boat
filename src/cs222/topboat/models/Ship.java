package cs222.topboat.models;

public class Ship {
    public Type type;

    public Ship(Type type) {
        this.type = type;
    }

    public enum Type {
        CARRIER(5),
        BATTLESHIP(4),
        CRUISER(3),
        SUBMARINE(3),
        DESTROYER(2);

        Type(int l) {
            this.length = l;
        }

        public int length;
    }
}
