package gitlet;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Bard
 */
public class Repository {
    /**
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
    public static final File TREE_PATH = join(CWD, ".gitlet", "tree");
    public static final File STAGE_PATH = join(CWD, ".gitlet", "stage");
    private static String[] excludedPrefixDir = new String[]{".", "gitlet", "testing"};
    private static String[] excludedPrefixFile = new String[]{".DS_Store", "Makefile",
            "pom.xml", "gitlet-design.md"};
    private static List<String> recordFile;


    public static boolean setupPersistence() {
        /** if file has been set up before, print error message and return */
        if (GITLET_DIR.exists() && COMMIT_DIR.exists() && BOLB_DIR.exists() && TREE_PATH.exists()) {
            System.out.println("A Gitlet version-control system " +
                    "already exists in the current directory.");
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
        /* Get tree */
        Tree temp = readObject(TREE_PATH, Tree.class);
        /* Get Head commit */
        String sha = temp.getHead();
        String firstTwo = sha.substring(0, 2);
        String last = sha.substring(2, sha.length());
        File inFile = Utils.join(COMMIT_DIR, firstTwo, last);
        /* Get head's parent commit if it has parent */
        String curSHA = sha;
        while (true) {
            Commit c = readObject(inFile, Commit.class);
            System.out.println("===");
            System.out.println("commit " + curSHA);
            System.out.println("Date: " + c.getTimestamp());
            System.out.println(c.getMessage());
            System.out.println();
            if (c.getFirstParent() != null) {
                String nextParentsha = c.getFirstParent();
                firstTwo = nextParentsha.substring(0, 2);
                last = nextParentsha.substring(2, sha.length());
                inFile = Utils.join(COMMIT_DIR, firstTwo, last);
                curSHA = c.getFirstParent();
            } else {
                break;
            }
        }
    }
    public static void globalLog() {
        String[] files = COMMIT_DIR.list();
        if (files != null) {
            for (String file : files) {
                File inFile = Utils.join(COMMIT_DIR, file);
                String[] subFiles = inFile.list();
                for (String subFile : subFiles) {
                    File inFile1 = Utils.join(COMMIT_DIR, file, subFile);
                    Commit c = readObject(inFile1, Commit.class);
                    System.out.println("===");
                    System.out.println("commit " + file + subFile);
                    System.out.println("Date: " + c.getTimestamp());
                    System.out.println(c.getMessage());
                    System.out.println();
                }
            }
        }
    }
    public static void find(String commitMessage) {
        boolean find = false;
        String[] files = COMMIT_DIR.list();
        if (files != null) {
            for (String file : files) {
                File inFile = Utils.join(COMMIT_DIR, file);
                String[] subFiles = inFile.list();
                for (String subFile : subFiles) {
                    File inFile1 = Utils.join(COMMIT_DIR, file, subFile);
                    Commit c = readObject(inFile1, Commit.class);
                    if (c.getMessage().equals(commitMessage)) {
                        find = true;
                        System.out.println(file + subFile);
                    }
                }
            }
        }
        if (!find) {
            System.out.println("Found no commit with that message.");
        }
    }
    public static void status() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        System.out.println("=== Branches ===");
        Tree temp = readObject(TREE_PATH, Tree.class);
        String curBranch = temp.getCurBranch();
        HashMap<String, String> branch = temp.getBranch();
        SortedSet<String> sortSet = new TreeSet<>();
        for (String key: branch.keySet()) {
            sortSet.add(key);
        }
        if (curBranch.equals("master")) {
            System.out.print("*");
        }
        System.out.println("master");
        for (String key: sortSet) {
            if (key.equals("master")) {
                continue;
            }
            if (key.equals(curBranch)) {
                System.out.println("*" + key);
            } else {
                System.out.println(key);
            }
        }
        System.out.println("");
        System.out.println("=== Staged Files ===");
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        HashMap<String, String> blobs = stage.getBlobs();
        sortSet = new TreeSet<>();
        for (String key: blobs.keySet()) {
            sortSet.add(key);
        }
        for (String key: sortSet) {
            System.out.println(key);
        }
        System.out.println("");
        System.out.println("=== Removed Files ===");
        HashSet<String> rmBlobs = stage.getRmBolbsBlobs();
        sortSet = new TreeSet<>();
        for (String key: rmBlobs) {
            sortSet.add(key);
        }
        for (String key : sortSet) {
            System.out.println(key);
        }
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("");
        System.out.println("=== Untracked Files ===");
        List<String> untrackfile = getUntrackFile();
        for (String s : untrackfile) {
            System.out.println(s);
        }
        System.out.println("");
    }
    private static List<String> getUntrackFile() {
        List<String> untrackfile = new ArrayList<>();
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        HashMap<String, String> blobs = stage.getBlobs();
        recordFile = new ArrayList<>();
        traverseDirectory(CWD);
        Commit c = getHeadCommit();
        for (String s : recordFile) {
            if (!c.getBlobs().containsKey(s) && !blobs.containsKey(s)) {
                untrackfile.add(s);
            }
        }
        return untrackfile;
    }
    private static void traverseDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && !isFileExcluded(file)) {
                    Path pathAbsolute = file.toPath();
                    Path pathBase = CWD.toPath();
                    Path pathRelative = pathBase.relativize(pathAbsolute);
                    recordFile.add(pathRelative.toString());
                } else if (file.isDirectory() && !isDicExcluded(file)) {
                    traverseDirectory(file);
                }
            }
        }
    }
    private static boolean isDicExcluded(File file) {
        String fileName = file.getName();
        boolean flag = false;
        for (String s : excludedPrefixDir) {
            if (fileName.startsWith(s)) {
                flag = true;
            }
        }
        return flag;
    }
    private static boolean isFileExcluded(File file) {
        String fileName = file.getName();
        boolean flag = false;
        for (String s : excludedPrefixFile) {
            if (fileName.startsWith(s)) {
                flag = true;
            }
        }
        return flag;
    }

    private static String getSHAfromfile(String fileName) {
        /* Find file's path */
        File f = Utils.join(CWD, fileName);

        /* If the file does not exist, print error */
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        /* Get file's SHA */
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
        /* Get tree */
        Tree temp = readObject(TREE_PATH, Tree.class);
        /* Get tree Head commit SHA */
        String sha = temp.getHead();
        /* Using SHA to get the commit file */
        Commit c = getCommitfromSHA(sha);
        return c;
    }
    private static String getSplitCommitsha(String head, String branch) {
        /* key is commit SHA, value is depth*/
        HashMap<String, Integer> headLog = new HashMap<>();
        HashMap<String, Integer> branchLog = new HashMap<>();
        Queue<String> headQ = new LinkedList<>();
        headQ.add(head);
        int depth = 0;
        while (!headQ.isEmpty()) {
            int size = headQ.size();
            while (size-- > 0) {
                String temp = headQ.remove();
                headLog.put(temp, depth);
                Commit tempc = getCommitfromSHA(temp);
                if (tempc.getFirstParent() != null) {
                    headQ.add(tempc.getFirstParent());
                }
                if (tempc.getSecondParent() != null) {
                    headQ.add(tempc.getSecondParent());
                }
            }
            depth++;
        }
        Queue<String> branchQ = new LinkedList<>();
        branchQ.add(branch);
        depth = 0;
        while (!branchQ.isEmpty()) {
            int size = branchQ.size();
            while (size-- > 0) {
                String temp = branchQ.remove();
                branchLog.put(temp, depth);
                Commit tempc = getCommitfromSHA(temp);
                if (tempc.getFirstParent() != null) {
                    branchQ.add(tempc.getFirstParent());
                }
                if (tempc.getSecondParent() != null) {
                    branchQ.add(tempc.getSecondParent());
                }
            }
            depth++;
        }
//        System.out.println(headLog);
//        System.out.println(branchLog);
        String minKey = null;
        int minValue = Integer.MAX_VALUE;
        for (String s : headLog.keySet()) {
            if (branchLog.containsKey(s)) {
                if (branchLog.get(s) < minValue) {
                    minValue = branchLog.get(s);
                    minKey = s;
                }
            }
        }
        return minKey;
    }
    public static void mergeBranch(String branchName) {
        /* Record whether encountered a merge conflict. */
        boolean changed = false;
        /* get current stage Blobs and rmBlobs*/
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        HashMap<String, String> blobs = stage.getBlobs();
        HashSet<String> rmBlobs = stage.getRmBolbsBlobs();
        /* If stage area have uncommitted blobs, print error */
        if (blobs.size() > 0 || rmBlobs.size() > 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        /* Get tree branch */
        Tree temp = readObject(TREE_PATH, Tree.class);
        HashMap<String, String> branch = temp.getBranch();
        /* If given branch name does not exist, print error */
        if (!branch.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        /* If given branch name equals to current branch name, print error */
        if (temp.getCurBranch().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        /* If there is an untracked file, print error */
        if (getUntrackFile().size() > 0) {
            System.out.println("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
            System.exit(0);
        }
        /* Get given branch commit */
        String branchCommitsha = branch.get(branchName);
        Commit branchCommit = getCommitfromSHA(branchCommitsha);
        /* Get head commit */
        String headCommitsha = temp.getHead();
        Commit head = getHeadCommit();
        /* Get split commit */
        String splitCommitsha = getSplitCommitsha(headCommitsha, branchCommitsha);
        Commit splitCommit = getCommitfromSHA(splitCommitsha);
//        System.out.println(splitCommit.getMessage());
        /* If split commit equals to given branch commit, print error */
        if (splitCommitsha.equals(branchCommitsha)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        /* If the split point is the current branch, checkout given branch */
        if (splitCommitsha.equals(temp.getHead())) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        /* Create new stage area */
        StagingArea newStage = new StagingArea();
        /* Create new commit */
        String message = "Merged " + branchName + " into " + temp.getCurBranch();
        Commit newCommit = new Commit(message, temp.getHead(), branchCommitsha);
        HashMap<String, String> remainBlobs = newCommit.getBlobs();

        /* Get new stage area blob and rmblob */
        HashMap<String, String> stageBlobs = newStage.getBlobs();
        HashSet<String> stagermBlobs = newStage.getRmBolbsBlobs();
        /* Get split commit blobs */
        HashMap<String, String> splitBlobs = splitCommit.getBlobs();
        /* Get head commit blobs */
        HashMap<String, String> headBlobs = head.getBlobs();
        /* Get branch commit blobs */
        HashMap<String, String> branchBolbs = branchCommit.getBlobs();
        /* Iterate split commit blobs */
        for (String s : splitBlobs.keySet()) {
            if (!headBlobs.containsKey(s) && !branchBolbs.containsKey(s)) {
                continue;
            } else if (headBlobs.containsKey(s) && !branchBolbs.containsKey(s)) {
                if (!headBlobs.get(s).equals(splitBlobs.get(s))) {
                    File inFileHead = Utils.join(BOLB_DIR, headBlobs.get(s));
                    String stringHead = readContentsAsString(inFileHead);
                    String stringBranch = "";
                    String top = "<<<<<<< HEAD";
                    String middle = "=======";
                    String bottom = ">>>>>>>";
                    String finalContent = top + System.lineSeparator() + stringHead + middle
                            + System.lineSeparator() + stringBranch + bottom + System.lineSeparator();
                    /* Replace file in working directory */
                    File outFile = Utils.join(CWD, s);
                    writeContents(outFile, finalContent);
                    String fileSHA = getSHAfromfile(s);
                    /* Save blob */
                    File inFile1 = Utils.join(CWD, s);
                    byte[] filetoByte = readContents(inFile1);
                    File outFile1 = Utils.join(BOLB_DIR, fileSHA);
                    writeContents(outFile1, filetoByte);

                    stageBlobs.put(s, fileSHA);
                    changed = true;
                } else {
                    stagermBlobs.add(s);
                }
            } else if (!headBlobs.containsKey(s) && branchBolbs.containsKey(s)) {
                if (!branchBolbs.get(s).equals(splitBlobs.get(s))) {
                    File inFileHead = Utils.join(BOLB_DIR, branchBolbs.get(s));
                    String stringHead = readContentsAsString(inFileHead);
                    String stringBranch = "";
                    String top = "<<<<<<< HEAD";
                    String middle = "=======";
                    String bottom = ">>>>>>>";
                    String finalContent = top + System.lineSeparator() + stringHead + middle
                            + System.lineSeparator() + stringBranch + bottom + System.lineSeparator();
                    /* Replace file in working directory */
                    File outFile = Utils.join(CWD, s);
                    writeContents(outFile, finalContent);
                    String fileSHA = getSHAfromfile(s);
                    /* Save blob */
                    File inFile1 = Utils.join(CWD, s);
                    byte[] filetoByte = readContents(inFile1);
                    File outFile1 = Utils.join(BOLB_DIR, fileSHA);
                    writeContents(outFile1, filetoByte);

                    stageBlobs.put(s, fileSHA);
                    changed = true;
                }
            } else if (headBlobs.get(s).equals(splitBlobs.get(s))
                    && branchBolbs.get(s).equals(splitBlobs.get(s))) {
                remainBlobs.put(s, headBlobs.get(s));
            } else if (headBlobs.get(s).equals(splitBlobs.get(s))
                    && !branchBolbs.get(s).equals(splitBlobs.get(s))) {
                stageBlobs.put(s, branchBolbs.get(s));
            } else if (!headBlobs.get(s).equals(splitBlobs.get(s))
                    && branchBolbs.get(s).equals(splitBlobs.get(s))) {
                remainBlobs.put(s, headBlobs.get(s));
            } else if (!headBlobs.get(s).equals(splitBlobs.get(s))
                    && !branchBolbs.get(s).equals(splitBlobs.get(s))) {
                if (headBlobs.get(s).equals(branchBolbs.get(s))) {
                    remainBlobs.put(s, headBlobs.get(s));
                } else {
                    File inFileHead = Utils.join(BOLB_DIR, headBlobs.get(s));
                    String stringHead = readContentsAsString(inFileHead);
                    File inFileBranch = Utils.join(BOLB_DIR, branchBolbs.get(s));
                    String stringBranch = readContentsAsString(inFileBranch);
                    String top = "<<<<<<< HEAD";
                    String middle = "=======";
                    String bottom = ">>>>>>>";
                    String finalContent = top + System.lineSeparator() + stringHead + middle
                            + System.lineSeparator() + stringBranch + bottom + System.lineSeparator();
                    /* Replace file in working directory */
                    File outFile = Utils.join(CWD, s);
                    writeContents(outFile, finalContent);
                    String fileSHA = getSHAfromfile(s);
                    /* Save blob */
                    File inFile1 = Utils.join(CWD, s);
                    byte[] filetoByte = readContents(inFile1);
                    File outFile1 = Utils.join(BOLB_DIR, fileSHA);
                    writeContents(outFile1, filetoByte);

                    stageBlobs.put(s, fileSHA);
                    changed = true;
                }
            }
        }
        /* Iterate branch commit blobs which split commit and head commit doesn't have */
        for (String s : branchBolbs.keySet()) {
            if (!splitBlobs.containsKey(s) && !headBlobs.containsKey(s)) {
                stageBlobs.put(s, branchBolbs.get(s));
            }
        }
        /* Iterate head commit blobs which split commit and branch commit doesn't have */
        for (String s : headBlobs.keySet()) {
            if (!splitBlobs.containsKey(s) && !branchBolbs.containsKey(s)) {
                remainBlobs.put(s, headBlobs.get(s));
            }
        }
        /* Iterate head commit blobs which branch commit
        and head commit both have but split commit doesn't */
        for (String s : headBlobs.keySet()) {
            if (!splitBlobs.containsKey(s) && branchBolbs.containsKey(s)) {
                /* If the blob in head commit and branch commit are equal, add to remain blobs */
                if (headBlobs.get(s).equals(branchBolbs.get(s))) {
                    remainBlobs.put(s, headBlobs.get(s));
                } else {
                    /* If there is conflict, add text to the file */
                    File inFileHead = Utils.join(BOLB_DIR, headBlobs.get(s));
                    String stringHead = readContentsAsString(inFileHead);
                    File inFileBranch = Utils.join(BOLB_DIR, branchBolbs.get(s));
                    String stringBranch = readContentsAsString(inFileBranch);
                    String top = "<<<<<<< HEAD";
                    String middle = "=======";
                    String bottom = ">>>>>>>";
                    String finalContent = top + System.lineSeparator() + stringHead + middle
                            + System.lineSeparator() + stringBranch + bottom + System.lineSeparator();

                    /* Replace file in working directory */
                    File outFile = Utils.join(CWD, s);
                    writeContents(outFile, finalContent);
                    String fileSHA = getSHAfromfile(s);
                    /* Save blob */
                    File inFile1 = Utils.join(CWD, s);
                    byte[] filetoByte = readContents(inFile1);
                    File outFile1 = Utils.join(BOLB_DIR, fileSHA);
                    writeContents(outFile1, filetoByte);

                    stageBlobs.put(s, fileSHA);
                    changed = true;
                }
            }
        }
        for (String key : stagermBlobs) {
            File dfile = Utils.join(CWD, key);
            restrictedDelete(dfile);
        }
        /* Iterate through the staging area Blobs HashMap,
         *  add files to new Blobs HashMap except for those in remove Blobs HashSet
         */
        for (String key: stageBlobs.keySet()) {
            remainBlobs.put(key, stageBlobs.get(key));
        }

        /* add blobs to new commit */
        newCommit.addBlobs(remainBlobs);
        String newCommitSHA = sha1(getClassBytes(newCommit));
        /* Write commit */
        String firstTwo = newCommitSHA.substring(0, 2);
        String last = newCommitSHA.substring(2, newCommitSHA.length());
        File commitDir = Utils.join(COMMIT_DIR, firstTwo);
        if (!commitDir.exists()) {
            commitDir.mkdir();
        }
        File commitFile = Utils.join(COMMIT_DIR, firstTwo, last);
        writeObject(commitFile, newCommit);

        /* Write to tree and stage */
        temp.put(newCommitSHA);
        branch.put(branchName, newCommitSHA);
        writeObject(TREE_PATH, temp);
        stage = new StagingArea();
        writeObject(STAGE_PATH, stage);
        /* If there is a merge conflict, print message */
        if (changed) {
            System.out.println("Encountered a merge conflict.");
        }
        replaceFilefromcommit(newCommitSHA);
    }
    /** checkout -- [file name] */
    public static void checkoutFileName(String fileName) {
        Commit c = getHeadCommit();
        HashMap<String, String> bolbs = c.getBlobs();
        if (!bolbs.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String oldFileSHA = bolbs.get(fileName);
        File inFile = Utils.join(BOLB_DIR, oldFileSHA);
        String oldFile = readContentsAsString(inFile);
        File outFile = Utils.join(CWD, fileName);
        writeContents(outFile, oldFile);
    }
    private static Commit getCommitfromSHA(String commitId) {
        String firstTwo = commitId.substring(0, 2);
        String last = commitId.substring(2, commitId.length());
        File commitDir = Utils.join(COMMIT_DIR, firstTwo);
        if (!commitDir.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        File[] files = commitDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().substring(0, commitId.length() - 2).equals(last)) {
                    File infile = Utils.join(commitDir, file.getName());
                    Commit c = readObject(infile, Commit.class);
                    return c;
                }
            }
        }
        System.out.println("No commit with that id exists.");
        System.exit(0);
        return null;
    }
    /** checkout [commit id] -- [file name] */
    public static void checkoutIDFileName(String commitId, String fileName) {
        Commit c = getCommitfromSHA(commitId);
        HashMap<String, String> bolbs = c.getBlobs();
        if (!bolbs.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String oldFileSHA = bolbs.get(fileName);
        File inFile1 = Utils.join(BOLB_DIR, oldFileSHA);
        String oldFile = readContentsAsString(inFile1);
        File outFile = Utils.join(CWD, fileName);
        writeContents(outFile, oldFile);
    }
    /** checkout [branch name] */
    public static void checkoutBranch(String branchName) {
        Tree temp = readObject(TREE_PATH, Tree.class);
        HashMap<String, String> branch = temp.getBranch();
        if (!branch.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (branchName.equals(temp.getCurBranch())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        if (getUntrackFile().size() > 0) {
            System.out.println("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
            System.exit(0);
        }
        String commitSHA = branch.get(branchName);
        replaceFilefromcommit(commitSHA);
        temp.changeBranch(branchName);
        temp.changeHead(commitSHA);
        writeObject(TREE_PATH, temp);
    }
    private static void replaceFilefromcommit(String commitID) {
        rmAllFile(CWD);
        Commit c = getCommitfromSHA(commitID);
        HashMap<String, String> bolbs = c.getBlobs();
        for (String s : bolbs.keySet()) {
            File inFile1 = Utils.join(BOLB_DIR, bolbs.get(s));
            String oldFile = readContentsAsString(inFile1);
            String[] splitPath = s.split(Pattern.quote("/"));
            if (splitPath.length > 1) {
                File dir = Utils.join(CWD);
                for (int i = 0; i < splitPath.length - 1; i++) {
                    dir = Utils.join(dir, splitPath[i]);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                }
            }
            File outFile = Utils.join(CWD, s);
            writeContents(outFile, oldFile);
        }
        StagingArea stage = new StagingArea();
        writeObject(STAGE_PATH, stage);
    }
    public static void reset(String commitId) {
        getCommitfromSHA(commitId);
        if (getUntrackFile().size() > 0) {
            System.out.println("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
            System.exit(0);
        }
        replaceFilefromcommit(commitId);
        Tree temp = readObject(TREE_PATH, Tree.class);
        temp.changeHead(commitId);
        HashMap<String, String> branch = temp.getBranch();
        branch.put(temp.getCurBranch(), commitId);
        writeObject(TREE_PATH, temp);
    }
    private static void rmAllFile(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && !isFileExcluded(file)) {
                    file.delete();
                } else if (file.isDirectory() && !isDicExcluded(file)) {
                    rmAllFile(file);
                    file.delete();
                }
            }
        }
    }
    public static void branch(String branchName) {
        Tree temp = readObject(TREE_PATH, Tree.class);
        HashMap<String, String> branch = temp.getBranch();
        if (branch.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        branch.put(branchName, branch.get(temp.getCurBranch()));
        writeObject(TREE_PATH, temp);
    }
    public static void rmBranch(String branchName) {
        Tree temp = readObject(TREE_PATH, Tree.class);
        HashMap<String, String> branch = temp.getBranch();
        if (!branch.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(temp.getCurBranch())) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branch.remove(branchName);
        writeObject(TREE_PATH, temp);
    }
    /** Add file to staging area */
    public static void addFile(String fileName) {
        String fileSHA = getSHAfromfile(fileName);

        /* Load tree */
        Tree temp = readObject(TREE_PATH, Tree.class);

        /* If main not equal to root, find whether the file has been changed */
        if (temp.getSize() != 1) {
            /* Get head commit */
            Commit c = getHeadCommit();

            /* Get the commits blobs */
            HashMap<String, String> h = c.getBlobs();

            /* Check the corresponding SHA is equal or not compared to previous commit
             *  If is equal, remove the file from the stage.
             */
            if (h.containsKey(fileName) && fileSHA.equals(h.get(fileName))) {
//                System.out.println("there is no change to the file compared to previous commit");
                StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
                if (stage.getBlobs().containsKey(fileName)) {
//                    System.out.println("Remove the file from commit");
                    stage.removeBlob(fileName);
                    writeObject(STAGE_PATH, stage);
                    return;
                }
                return;
            }
        }
        /* Find whether the file has been changed to previously add version */
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        HashMap<String, String> blobs = stage.getBlobs();

        if (blobs.containsKey(fileName) && fileSHA.equals(blobs.get(fileName))) {
//            System.out.println("there is no change to the file compared to previous add version");
            return;
        }

        /* Add the file to staging area */
        blobs.put(fileName, fileSHA);
        writeObject(STAGE_PATH, stage);
    }
    /** Creating commit */
    public static void commitBolb(String message) {
        if (message.length() == 0) {
            System.out.println("Please enter a commit message.");
            return;
        }
        /* Get staging area */
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        /* Get staging area Blobs HashMap */
        HashMap<String, String> stageBlobs = stage.getBlobs();
        HashSet<String> rmBlobs = stage.getRmBolbsBlobs();
        /* If there is no file in stage area, return */
        if (stageBlobs.size() == 0 && rmBlobs.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }

        /* Get tree */
        Tree temp = readObject(TREE_PATH, Tree.class);
        /* Get tree Head commit SHA */
        String sha = temp.getHead();
        /* Using SHA to get the commit file */
        Commit c = getCommitfromSHA(sha);
        /* Get Head commit Blobs HashMap */
        HashMap<String, String> previousBolbs = c.getBlobs();

        /* Creating new Blobs HashMap */
        HashMap<String, String> curBolbs = new HashMap<>();

        /* Iterate through the Head commit Blobs HashMap,
         *  add files to new Blobs HashMap except for those in remove Blobs HashSet
         */
        for (String key: previousBolbs.keySet()) {
            if (!rmBlobs.contains(key)) {
                curBolbs.put(key, previousBolbs.get(key));
            }
        }
        /* Iterate through the staging area Blobs HashMap,
         *  add files to new Blobs HashMap except for those in remove Blobs HashSet
         */
        for (String key: stageBlobs.keySet()) {
            if (!rmBlobs.contains(key)) {
                curBolbs.put(key, stageBlobs.get(key));
            }
        }
        /* Creating Blobs */
        for (String key: curBolbs.keySet()) {
            File f = Utils.join(CWD, key);
            byte[] filetoByte = readContents(f);
            File outFile = Utils.join(BOLB_DIR, curBolbs.get(key));
            writeContents(outFile, filetoByte);
        }

        /* Creating commit */
        Commit newCommit = new Commit(message, sha, curBolbs);
        String s = sha1(getClassBytes(newCommit));

        /* Write commit */
        String firstTwo = s.substring(0, 2);
        String last = s.substring(2, 40);
        File commitDir = Utils.join(COMMIT_DIR, firstTwo);
        if (!commitDir.exists()) {
            commitDir.mkdir();
        }
        File outFile = Utils.join(COMMIT_DIR, firstTwo, last);
        writeObject(outFile, newCommit);

        /* Write to tree and stage */
        temp.put(s);
        writeObject(TREE_PATH, temp);
        stage = new StagingArea();
        writeObject(STAGE_PATH, stage);
    }
    public static void rmFile(String fileName) {
        boolean changed = false;
        /* Get staging area */
        StagingArea stage = readObject(STAGE_PATH, StagingArea.class);
        /* Get staging area Blobs HashMap */
        HashMap<String, String> stageBlobs = stage.getBlobs();
        /* If file exist in staging area, remove it */
        if (stageBlobs.containsKey(fileName)) {
//            System.out.println("Remove file from staging area");
            stage.removeBlob(fileName);
            writeObject(STAGE_PATH, stage);
            changed = true;
        }
        /* Get Head commit Blobs HashMap */
        Commit c = getHeadCommit();
        HashMap<String, String> headBlobs = c.getBlobs();
        /* If file exist in current commit, remove it */
        if (headBlobs.containsKey(fileName)) {
//            System.out.println("Remove file from the working directory");
            /* Stage it for removal */
            stage.addtormBlob(fileName);
            writeObject(STAGE_PATH, stage);
            /* Remove the file from the working directory  */
            File f = Utils.join(CWD, fileName);
            restrictedDelete(f);
            changed = true;
        }
        if (!changed) {
            System.out.println("No reason to remove the file.");
        }
    }
    /** Initialize gitlet */
    public static void initialCommit() {
        /* Setup Persistence, if have been set up, just return */
        if (!setupPersistence()) {
            return;
        }

        /* Create instance variable of tree and stage*/
        Tree root = new Tree();
        StagingArea stage = new StagingArea();

        /* Add initial commit */
        Commit firstCommit = new Commit("initial commit");
        /* Get first commit SHA */
        String s = sha1(getClassBytes(firstCommit));

        /* Write commit */
        String firstTwo = s.substring(0, 2);
        String last = s.substring(2, 40);
        File commitDir = Utils.join(COMMIT_DIR, firstTwo);
        if (!commitDir.exists()) {
            commitDir.mkdir();
        }
        File outFile = Utils.join(COMMIT_DIR, firstTwo, last);
        writeObject(outFile, firstCommit);

        /* Write tree and stage */
        root.put(s);
        writeObject(TREE_PATH, root);
        writeObject(STAGE_PATH, stage);
    }

    private static byte[] getClassBytes(Object myObject) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(myObject);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] classBytes = baos.toByteArray();
        return classBytes;
    }
}
