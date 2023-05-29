package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class StagingArea implements Serializable {
    /**
     * Using Hashmap to store bolbs
     * Key is relative file path, value is its SHA
     */
    private HashMap<String,String> blobs;
    public StagingArea() {
        blobs = new HashMap<>();
    }
    public HashMap<String,String> getBlobs() {
        return blobs;
    }
    public void putBlob(String path, String sha) {
        blobs.put(path, sha);
    }
    public void removeBlob(String path) {
        blobs.remove(path);
    }
}
