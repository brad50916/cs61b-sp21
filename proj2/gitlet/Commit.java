package gitlet;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
// TODO: any imports you need here

// TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String timestamp;
    private String firstParent;
    private String secondParent;
    private HashMap<String,String> bolbs;


    Commit (String message, String firstParent, HashMap<String,String> bolbs) {
        this.message = message;
        this.firstParent = firstParent;
        this.bolbs = bolbs;
    }

    Commit (String message, String firstParent, String secondParent, HashMap<String,String> bolbs) {
        this.message = message;
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.bolbs = bolbs;
    }
}
