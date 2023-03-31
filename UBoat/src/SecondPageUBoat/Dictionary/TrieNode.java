package SecondPageUBoat.Dictionary;

import java.util.HashMap;

public class TrieNode {
    private char c;
    private TrieNode parent;
    private HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    private boolean isLeaf;

    public TrieNode() {}
    public TrieNode(char c){this.c = c;}

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public char getC() {
        return c;
    }

    public TrieNode getParent() {
        return parent;
    }

    public void setC(char c) {
        this.c = c;
    }

    public void setChildren(HashMap<Character, TrieNode> children) {
        this.children = children;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public void setParent(TrieNode parent) {
        this.parent = parent;
    }

    public boolean getIsLeaf() { return  isLeaf; }


}
