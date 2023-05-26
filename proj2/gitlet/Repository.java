package gitlet;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(CWD, ".gitlet", "commits");
    public static final File BOLB_DIR = join(CWD, ".gitlet", "bolbs");
    public static final File TREE_DIR = join(CWD,".gitlet", "tree");

    public static void setupPersistence() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }
        if (!COMMIT_DIR.exists()) {
            COMMIT_DIR.mkdir();
        }
        if (!BOLB_DIR.exists()) {
            BOLB_DIR.mkdir();
        }
        if (!TREE_DIR.exists()) {
            TREE_DIR.mkdir();
        }
    }

    public static void initialCommit() {
        Date currentDate = new Date(0);
        Formatter formatter = new Formatter();
        String timestamp = formatter.format("%tF %tT", currentDate, currentDate).toString();
        formatter.close();
        Commit firstCommit = new Commit("initial commit", timestamp);
        getSHA1(firstCommit);
//        Tree root = new Tree(firatCommit);
    }
    public static String getSHA1(Object instance) {
        try {
            // Convert the instance to a byte array
            byte[] objectBytes = convertToBytes(instance);

            // Calculate the SHA-1 hash from the byte array
            byte[] sha1Hash = calculateSHA1Hash(objectBytes);

            // Convert the hash bytes to a hexadecimal string representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : sha1Hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            String sha1HashString = hexString.toString();
            System.out.println("SHA-1 hash: " + sha1HashString);
            return sha1HashString;

        } catch (IOException e) {
            System.out.println("Error converting object to byte array: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SHA-1 algorithm not available.");
        }
        return null;
    }
    private static byte[] convertToBytes(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(obj);
        objectStream.close();
        return byteStream.toByteArray();
    }

    private static byte[] calculateSHA1Hash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        return sha1.digest(data);
    }
}
