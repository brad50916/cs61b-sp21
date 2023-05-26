package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tree implements Serializable {
    private String commit;
    private Tree root;
    private Tree main;
    private List<Tree> children;
    public Tree(String commit) {
        this.commit = commit;
        this.root = this;
        this.main = this;
        children = new ArrayList<>();
    }
}