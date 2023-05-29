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
    private HashSet<String> rmBlobs;
    public StagingArea() {
        blobs = new HashMap<>();
        rmBlobs = new HashSet<>();
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
    public HashSet<String> getRmBolbsBlobs() {
        return rmBlobs;
    }
    public void addtormBlob(String path) {
        rmBlobs.add(path);
    }
}
