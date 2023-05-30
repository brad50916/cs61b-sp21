package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Tree implements Serializable {
    private TreeNode root;
    private String curBranch;
    /** HashMap's key is branch name, value is TreeNode */
    private HashMap<String, TreeNode> branch;
    private TreeNode head;
    private int size = 0;
    public Tree() {
        this.root = null;
        this.head = null;
        curBranch = "master";
        this.branch = new HashMap<>();
    }
    public class TreeNode implements Serializable{
        private String commitSHA;
        /** map key is SHA, value is child commit */
        private Map<String, TreeNode> children;
        public TreeNode(String commit) {
            this.commitSHA = commit;
            this.children = new HashMap<>();
        }
        public Map<String, TreeNode> getChildren() {
            return children;
        }
        public String getCommitSHA() {
            return commitSHA;
        }
    }
    public void put(String commit) {
        if (this.root == null) {
            this.root = new TreeNode(commit);
            this.branch.put(curBranch, root);
            this.head = root;
        } else {
            TreeNode temp = new TreeNode(commit);
            this.branch.get(curBranch).getChildren().put(commit, temp);
            this.branch.put(curBranch, temp);
            this.head = temp;
        }
        size++;
    }
    public TreeNode getRoot() {
        return root;
    }
    public TreeNode getHead() {
        return head;
    }
    public int getSize() {
        return size;
    }
}