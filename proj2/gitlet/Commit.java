package gitlet;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
// TODO: any imports you need here

// TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Bard
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    /** parent commit SHA */
    private String firstParent;
    private String secondParent;
    /** hashmap key is relative file path, value is its SHA */
    private HashMap<String, String> bolbs;

    /** For initial commit **/
    public Commit(String message) {
        this.message = message;
        /**  Generate The (Unix) Epoch time for initial commit timestamp */
        Date currentDate = new Date(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM d HH:mm:ss yyyy Z");
        String timestamp1 = dateFormat.format(currentDate);
        this.timestamp = timestamp1;
        this.bolbs = new HashMap<>();
    }

    public Commit(String message, String firstParent, HashMap<String, String> bolbs) {
        this.message = message;
        this.firstParent = firstParent;
        this.bolbs = bolbs;
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM d HH:mm:ss yyyy Z");
        String timestamp1 = dateFormat.format(currentDate);
        this.timestamp = timestamp1;
    }

    public Commit(String message, String firstParent, String secondParent, HashMap<String, String> bolbs) {
        this.message = message;
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.bolbs = bolbs;
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM d HH:mm:ss yyyy Z");
        String timestamp1 = dateFormat.format(currentDate);
        this.timestamp = timestamp1;
    }

    public String getMessage() {
        return message;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getFirstParent() {
        return firstParent;
    }
    public String getSecondParent() {
        return secondParent;
    }
    public HashMap<String, String> getBlobs() {
        return bolbs;
    }
}
