package gitlet;

import java.io.Serializable;
import java.util.HashMap;


public class Tree implements Serializable {
    private String root;
    private String curBranchname;
    /* HashMap's key is branch name, value is commit SHA */
    private HashMap<String, String> branch;
    /* head commit sha */
    private String head;
    private int size = 0;
    public Tree() {
        this.root = null;
        this.head = null;
        curBranchname = "master";
        this.branch = new HashMap<>();
        this.branch.put(curBranchname, null);
    }
    public void put(String commit) {
        if (this.root == null) {
            this.root = commit;
            this.branch.put(curBranchname, root);
            this.head = root;
        } else {
            this.branch.put(curBranchname, commit);
            this.head = commit;
        }
        size++;
    }
    public String getRoot() {
        return root;
    }
    public String getHead() {
        return head;
    }
    public int getSize() {
        return size;
    }
    public String getCurBranch() {
        return curBranchname;
    }
    public HashMap<String, String> getBranch() {
        return branch;
    }
    public void changeBranch(String b) {
        if (!branch.containsKey(b)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        this.curBranchname = b;
    }
    public void changeHead(String h) {
        this.head = h;
    }
}
