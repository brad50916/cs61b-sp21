package gitlet;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;

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
    public static final File TREE_PATH = join(CWD,".gitlet", "tree");
    public static final File STAGE_PATH = join(CWD,".gitlet", "stage");

    public static boolean setupPersistence() {
        /** if file has been set up before, print error message and return */
        if (GITLET_DIR.exists() && COMMIT_DIR.exists() && BOLB_DIR.exists() && TREE_PATH.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return false;
        }
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }
        if (!COMMIT_DIR.exists()) {
            COMMIT_DIR.mkdir();
        }
        if (!BOLB_DIR.exists()) {
            BOLB_DIR.mkdir();
        }
        return true;
    }
    public static void log() {
        Tree temp = readObject(TREE_PATH, Tree.class);
        String sha = temp.getRoot().getCommitSHA();
        System.out.println(sha);
        File inFile = Utils.join(COMMIT_DIR, sha);
        if (inFile.exists()) {
            Commit c = readObject(inFile, Commit.class);
            System.out.println(c.getMessage());
            System.out.println(c.getTimestamp());
        }
    }
    /** Add file to staging area */
    public static void addFile(String fileName) {
        /** Find file's path */
        File f = Utils.join(CWD, fileName);

        /** If the file does not exist, print error */
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        /** Get file's SHA */
        String path = f.getPath();
        String s = getSHA1frompath(path);

        /** Load tree */
        Tree temp = readObject(TREE_PATH, Tree.class);

        /** If main not equal to root, find whether the file has been changed */
        if (temp.getSize() != 1) {

            /** Get master commit */
            String sha = temp.getHead().getCommitSHA();
            File inFile = Utils.join(COMMIT_DIR, sha);
            Commit c = readObject(inFile, Commit.class);

            /** Get the commits blobs */
            HashMap<String,String> h = c.getBolbs();

            /** Check the corresponding SHA is equal or not compared to previous commit
             *  If is equal, remove the file from the stage.
             */
            if (h.containsKey(fileName) && s.equals(h.get(fileName))) {
                System.out.println("there is no change to the file compared to previous commit");
                StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
                stage.removeBlob(fileName);
                writeObject(STAGE_PATH, stage);
                return;
            }
        }
        /** Find whether the file has been changed to previously add version */
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        HashMap<String,String> blobs = stage.getBlobs();

        if (blobs.containsKey(fileName) && s.equals(blobs.get(fileName))) {
            System.out.println("there is no change to the file compared to previous add version");
            return;
        }

        /** Add the file to staging area */
        stage.putBlob(fileName, s);
        writeObject(STAGE_PATH, stage);
    }
    public static void Commit(String message) {
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        HashMap<String,String> stageBlobs = stage.getBlobs();
        HashSet<String> rmBolbs = stage.getRmBolbsBlobs();

        Tree temp = readObject(TREE_PATH, Tree.class);
        String sha = temp.getHead().getCommitSHA();
        File inFile = Utils.join(COMMIT_DIR, sha);
        Commit c = readObject(inFile, Commit.class);
        HashMap<String,String> previousBolbs = c.getBolbs();

        HashMap<String,String> curBolbs = new HashMap<>();
        for (String Key: previousBolbs.keySet()){
            if (!rmBolbs.contains(Key)) {
                curBolbs.put(Key,previousBolbs.get(Key));
            }
        }
        for (String Key: stageBlobs.keySet()) {
            if (!rmBolbs.contains(Key)) {
                curBolbs.put(Key, stageBlobs.get(Key));
            }
        }

        Commit newCommit = new Commit(message, sha, curBolbs);
        String s = getSHA1fromclass(newCommit);

        /** Write commit */
        File outFile = Utils.join(COMMIT_DIR, s);
        writeObject(outFile, newCommit);

        /** Write tree and stage */
        temp.put(s);
        writeObject(TREE_PATH, temp);
        stage = new StagingArea();
        writeObject(STAGE_PATH, stage);
    }
    /** Initialize gitlet */
    public static void initialCommit() {
        /** Setup Persistence, if have been set up, just return */
        if (setupPersistence() == false) return;

        /** Create instance variable of tree and stage*/
        Tree root = new Tree();
        StagingArea stage = new StagingArea();

        /**  Generate The (Unix) Epoch time for initial commit timestamp */
        Date currentDate = new Date(0);
        Formatter formatter = new Formatter();
        String timestamp = formatter.format("%tF %tT", currentDate, currentDate).toString();
        formatter.close();

        /** Add initial commit */
        Commit firstCommit = new Commit("initial commit", timestamp);

        /** Get first commit SHA */
        String s = getSHA1fromclass(firstCommit);

        /** Write commit */
        File outFile = Utils.join(COMMIT_DIR, s);
        writeObject(outFile, firstCommit);

        /** Write tree and stage */
        root.put(s);
        writeObject(TREE_PATH, root);
        writeObject(STAGE_PATH, stage);
    }
    public static String getSHA1frompath(String filePath) {
        try {
            // Read the file content into a byte array
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

            // Create a MessageDigest instance with SHA-1 algorithm
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

            // Calculate the SHA-1 hash from the file content
            byte[] sha1Hash = sha1.digest(fileContent);

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
            return sha1HashString;

        } catch (NoSuchAlgorithmException e) {
            System.out.println("SHA-1 algorithm not available.");
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return null;
    }
    public static String getSHA1fromclass(Object instance) {
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
