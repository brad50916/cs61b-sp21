package gitlet;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    /** Print log history for head commit */
    public static void log() {
        /** Get tree */
        Tree temp = readObject(TREE_PATH, Tree.class);
        /** Get Head commit */
        String sha = temp.getHead().getCommitSHA();
        File inFile = Utils.join(COMMIT_DIR, sha);
        /** Get head's parent commit if it has parent */
        String curSHA = sha;
        while(true) {
            Commit c = readObject(inFile, Commit.class);
            System.out.println("===");
            System.out.println("commit " + curSHA);
            System.out.println("Date: " + c.getTimestamp());
            System.out.println(c.getMessage());
            System.out.println();
            if (c.getFirstParent() != null) {
                inFile = Utils.join(COMMIT_DIR, c.getFirstParent());
                curSHA = c.getFirstParent();
            } else {
                break;
            }
        }
    }
    private static String getSHAfromfile(String fileName) {
        /** Find file's path */
        File f = Utils.join(CWD, fileName);

        /** If the file does not exist, print error */
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        /** Get file's SHA */
        String path = f.getPath();
        String s = "";
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(path));
            s = sha1(fileContent);

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return s;
    }
    private static Commit getHeadCommit() {
        /** Get tree */
        Tree temp = readObject(TREE_PATH, Tree.class);
        /** Get tree Head commit SHA */
        String sha = temp.getHead().getCommitSHA();
        /** Using SHA to get the commit file */
        File inFile = Utils.join(COMMIT_DIR, sha);
        Commit c = readObject(inFile, Commit.class);
        return c;
    }
    /** Add file to staging area */
    public static void addFile(String fileName) {
        String fileSHA = getSHAfromfile(fileName);

        /** Load tree */
        Tree temp = readObject(TREE_PATH, Tree.class);

        /** If main not equal to root, find whether the file has been changed */
        if (temp.getSize() != 1) {
            /** Get head commit */
            Commit c = getHeadCommit();

            /** Get the commits blobs */
            HashMap<String,String> h = c.getBlobs();

            /** Check the corresponding SHA is equal or not compared to previous commit
             *  If is equal, remove the file from the stage.
             */
            if (h.containsKey(fileName) && fileSHA.equals(h.get(fileName))) {
                System.out.println("there is no change to the file compared to previous commit");
                StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
                if (stage.getBlobs().containsKey(fileName)) {
                    System.out.println("Remove the file from commit");
                    stage.removeBlob(fileName);
                    writeObject(STAGE_PATH, stage);
                    return;
                }
            }
        }
        /** Find whether the file has been changed to previously add version */
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        HashMap<String,String> blobs = stage.getBlobs();

        if (blobs.containsKey(fileName) && fileSHA.equals(blobs.get(fileName))) {
            System.out.println("there is no change to the file compared to previous add version");
            return;
        }

        /** Add the file to staging area */
        stage.putBlob(fileName, fileSHA);
        writeObject(STAGE_PATH, stage);
    }
    /** Creating commit */
    public static void Commit(String message) {
        /** Get staging area */
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        /** Get staging area Blobs HashMap */
        HashMap<String,String> stageBlobs = stage.getBlobs();
        HashSet<String> rmBlobs = stage.getRmBolbsBlobs();
        /** If there is no file in stage area, return */
        if (stageBlobs.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }

        /** Get tree */
        Tree temp = readObject(TREE_PATH, Tree.class);
        /** Get tree Head commit SHA */
        String sha = temp.getHead().getCommitSHA();
        /** Using SHA to get the commit file */
        File inFile = Utils.join(COMMIT_DIR, sha);
        Commit c = readObject(inFile, Commit.class);
        /** Get Head commit Blobs HashMap */
        HashMap<String,String> previousBolbs = c.getBlobs();

        /** Creating new Blobs HashMap */
        HashMap<String,String> curBolbs = new HashMap<>();

        /** Iterate through the Head commit Blobs HashMap,
         *  add files to new Blobs HashMap except for those in remove Blobs HashSet
         */
        for (String Key: previousBolbs.keySet()){
            if (!rmBlobs.contains(Key)){
                curBolbs.put(Key,previousBolbs.get(Key));
            }
        }
        /** Iterate through the staging area Blobs HashMap,
         *  add files to new Blobs HashMap except for those in remove Blobs HashSet
         */
        for (String Key: stageBlobs.keySet()) {
            if (!rmBlobs.contains(Key)) {
                curBolbs.put(Key, stageBlobs.get(Key));
            }
        }
        /** Creating commit */
        Commit newCommit = new Commit(message, sha, curBolbs);
        String classtoString = newCommit.toString();
        String s = sha1(classtoString);

        /** Write commit */
        File outFile = Utils.join(COMMIT_DIR, s);
        writeObject(outFile, newCommit);

        /** Write tree and stage */
        temp.put(s);
        writeObject(TREE_PATH, temp);
        stage = new StagingArea();
        writeObject(STAGE_PATH, stage);
    }
    public static void Rm(String fileName) {
        boolean changed = false;

        /** Get staging area */
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        /** Get staging area Blobs HashMap */
        HashMap<String,String> stageBlobs = stage.getBlobs();
        /** If file exist in staging area, remove it */
        if (stageBlobs.containsKey(fileName)) {
            System.out.println("Remove file from staging area");
            stage.removeBlob(fileName);
            writeObject(STAGE_PATH, stage);
            changed = true;
        }

        /** Get Head commit Blobs HashMap */
        Commit c = getHeadCommit();
        HashMap<String,String> headBlobs = c.getBlobs();
        /** If file exist in current commit, remove it */
        if (headBlobs.containsKey(fileName)) {
            System.out.println("Remove file from the working directory");
            /** Stage it for removal */
            stage.addtormBlob(fileName);
            writeObject(STAGE_PATH, stage);
            /** Remove the file from the working directory  */
            File f = Utils.join(CWD, fileName);
            if (restrictedDelete(f) == false) {
                System.out.println("Fail to delete the file");
            }
            changed = true;
        }

        if (changed == false) {
            System.out.println("No reason to remove the file.");
        }
    }
    /** Initialize gitlet */
    public static void initialCommit() {
        /** Setup Persistence, if have been set up, just return */
        if (setupPersistence() == false) return;

        /** Create instance variable of tree and stage*/
        Tree root = new Tree();
        StagingArea stage = new StagingArea();

        /** Add initial commit */
        Commit firstCommit = new Commit("initial commit");
        String classtoString = firstCommit.toString();
        /** Get first commit SHA */
        String s = sha1(classtoString);

        /** Write commit */
        File outFile = Utils.join(COMMIT_DIR, s);
        writeObject(outFile, firstCommit);

        /** Write tree and stage */
        root.put(s);
        writeObject(TREE_PATH, root);
        writeObject(STAGE_PATH, stage);
    }
}
