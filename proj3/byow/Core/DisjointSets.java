package byow.Core;

public interface DisjointSets {
    /** Connects two rooms a and b. */
    void connect(Room a, Room b);
    /** Checks to see if two rooms are connected. */
    boolean isConnected(Room a, Room b);
}
