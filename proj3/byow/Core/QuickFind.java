package byow.Core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuickFind implements DisjointSets{
    private HashMap<Room, Room> parent;
    private HashMap<Room, Integer> size;
    public QuickFind(List<Room> allroom) {
        parent = new HashMap<>();
        size = new HashMap<>();
        for (Room r : allroom) {
            parent.put(r, null);
            size.put(r, 1);
        }
    }

    private Room find(Room p) {
        Room r = p;
        while (parent.get(r) != null) {
            r = parent.get(r);
        }
        return r;
    }

    @Override
    public void connect(Room a, Room b) {
        Room a1 = find(a);
        Room b1 = find(b);
        if (size.get(a1) >= size.get(b1)) {
            parent.put(b1, a1);
        } else {
            parent.put(a1, b1);
        }
    }

    @Override
    public boolean isConnected(Room a, Room b) {
        return find(a) == find(b);
    }

    public HashMap<Room, Room> getParent() {
        return parent;
    }

    public HashMap<Room, Integer> getSize() {
        return size;
    }
}
