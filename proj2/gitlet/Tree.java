package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Tree implements Serializable {
    private TreeNode root;
    private TreeNode main;
    public Tree() {
        this.root = null;
        this.main = null;
    }
    private class TreeNode {
        private String commit;
        private Map<String, TreeNode> children;
        public TreeNode(String commit) {
            this.commit = commit;
            this.children = new HashMap<>();
        }
        public Map<String, TreeNode> getChildren() {
            return children;
        }
    }
    public void put(String commit) {
        if (root == null) {
            root = new TreeNode(commit);
            main = root;
        } else {
            TreeNode temp = new TreeNode(commit);
            main.getChildren().put(commit, temp);
            main = main.getChildren().get(commit);
        }
    }
}