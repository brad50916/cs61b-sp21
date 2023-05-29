package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Tree implements Serializable {
    private TreeNode root;
    private TreeNode master;
    private TreeNode head;
    private int size = 0;
    public Tree() {
        this.root = null;
        this.master = null;
        this.head = null;
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
            this.master = root;
            this.head = root;
        } else {
            TreeNode temp = new TreeNode(commit);
            if (head.equals(master)) {
                this.master.getChildren().put(commit, temp);
                this.master = master.getChildren().get(commit);
                this.head = master;
            } else {
                this.head.getChildren().put(commit, temp);
                this.head = head.getChildren().get(commit);
            }
        }
        size++;
    }
    public TreeNode getRoot() {
        return root;
    }
    public TreeNode getMaster() {
        return master;
    }
    public TreeNode getHead() {
        return head;
    }
    public int getSize() {
        return size;
    }
}