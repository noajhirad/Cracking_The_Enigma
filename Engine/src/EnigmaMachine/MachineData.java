package EnigmaMachine;

import java.util.*;

public class MachineData {

    private List<LinkedList<Character>> rotorsRight;
    private List<LinkedList<Character>> rotorsLeft;
    private List<Integer> notchs;
    private List<Map<Integer, Integer>> allReflectors;
    private List<Character> voc;
    private Set<String> dictionary;
    private List<String> excludedChars;
    private int rotorsCount;

    public void setAllReflectors(List<Map<Integer, Integer>> allReflectors) {
        this.allReflectors = allReflectors;
    }

    public void setNotchs(List<Integer> notchs) {
        this.notchs = notchs;
    }

    public void setRotorsLeft(List<LinkedList<Character>> rotorsLeft) {
        this.rotorsLeft = rotorsLeft;
    }

    public void setRotorsRight(List<LinkedList<Character>> rotorsRight) {
        this.rotorsRight = rotorsRight;
    }

    public void setVoc(List<Character> voc) {
        this.voc = voc;
    }

    public List<Integer> getNotchs() {
        return notchs;
    }

    public List<Character> getVoc() {
        return voc;
    }

    public List<LinkedList<Character>> getRotorsLeft() {
        return rotorsLeft;
    }

    public List<LinkedList<Character>> getRotorsRight() {
        return rotorsRight;
    }

    public List<Map<Integer, Integer>> getAllReflectors() {
        return allReflectors;
    }

    public void setRotorsCount(int rotorsCount) { this.rotorsCount = rotorsCount; }

    public int getRotorsCount() { return rotorsCount; }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public void setExcludedChars(List<String> excludedChars) {
        this.excludedChars = excludedChars;
    }

    public List<String> getExcludedChars() {
        return excludedChars;
    }
}
