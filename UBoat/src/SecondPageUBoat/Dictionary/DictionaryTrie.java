package SecondPageUBoat.Dictionary;

import java.util.*;
public class DictionaryTrie {
    private TrieNode root;
    private ArrayList<String> words;
    private TrieNode prefixRoot;
    private String curPrefix;

    public DictionaryTrie() {
        root = new TrieNode();
        words = new ArrayList<String>();
    }

    public void insert(String word) {
        HashMap<Character, TrieNode> children = root.getChildren();
        TrieNode currentParent = root;


        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            TrieNode t;
            if (children.containsKey(c)) {
                t = children.get(c);
            } else {
                t = new TrieNode(c);
                t.setParent(currentParent);
                children.put(c, t);
            }

            children = t.getChildren();
            currentParent = t;

            //set leaf node
            if (i == word.length() - 1)
                t.setLeaf(true);
        }
    }

    public boolean search(String word) {
        TrieNode t = searchNode(word);
        if (t != null && t.getIsLeaf()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean startsWith(String prefix) {
        if (searchNode(prefix) == null) {
            return false;
        } else {
            return true;
        }
    }

    public TrieNode searchNode(String str) {
        Map<Character, TrieNode> children = root.getChildren();
        TrieNode t = null;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (children.containsKey(c)) {
                t = children.get(c);
                children = t.getChildren();
            } else {
                return null;
            }
        }

        prefixRoot = t;
        curPrefix = str;
        words.clear();
        return t;
    }

    public List<String> wordsFinderTraversal(String prefix) {
        TrieNode tn = searchNode(prefix);
        if (tn == null)
            return words;
        wordsFinderTraversal(tn,0);
        return words;
    }

    public void wordsFinderTraversal(TrieNode node, int offset) {

        if (node.getIsLeaf()) {
            TrieNode current = node;
            Stack<String> hstack = new Stack<String>();

            while (current != prefixRoot) {
                hstack.push(Character.toString(current.getC()));
                current = current.getParent();
            }
            String currentWord = curPrefix;

            while (!hstack.empty())
                currentWord = currentWord + hstack.pop();

            words.add(currentWord);
        }

        Set<Character> kset = node.getChildren().keySet();
        Iterator itr = kset.iterator();
        ArrayList<Character> aloc = new ArrayList<Character>();

        while (itr.hasNext()) {
            Character ch = (Character) itr.next();
            aloc.add(ch);
        }

        for (int i = 0; i < aloc.size(); i++)
            wordsFinderTraversal(node.getChildren().get(aloc.get(i)), offset + 2);

    }
}