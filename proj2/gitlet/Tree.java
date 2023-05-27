package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Tree implements Serializable {
    private TreeNode root;
    private TreeNode master;
    private int size = 0;
    public Tree() {
        this.root = null;
        this.master = null;
    }
    public class TreeNode implements Serializable{
        private String commitSHA;
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
        } else {
            TreeNode temp = new TreeNode(commit);
            this.master.getChildren().put(commit, temp);
            this.master = master.getChildren().get(commit);
        }
        size++;
    }
    public TreeNode getRoot() {
        return root;
    }
    public TreeNode getMaster() {
        return master;
    }
    public int getSize() {
        return size;
    }
}