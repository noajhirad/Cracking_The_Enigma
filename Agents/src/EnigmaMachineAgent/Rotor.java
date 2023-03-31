package EnigmaMachineAgent;

import java.io.Serializable;
import java.util.LinkedList;

public class Rotor implements Serializable, AnimationRotor {

    private LinkedList<Character> right;
    private LinkedList<Character> left;
    private char startingPoint;
    private char notch;
    private int notchOriginalIndex;
    private boolean movedNextRotor = false;
    private boolean madeStep = false;

    public Rotor(int newNotch, LinkedList<Character> rightL, LinkedList<Character> leftL) {
        right = rightL;
        left = leftL;
        notch = right.get(newNotch-1);
    }

    public void setStartingPoint(char start){
        startingPoint = start;
        reset();
        notchOriginalIndex = right.indexOf(notch);
    }

    public void reset() {
        while (!right.getFirst().equals(startingPoint))
            step();
        madeStep = false;
    }

    public void step() {
        movedNextRotor = false;
        madeStep = true;

        left.addLast(left.removeFirst());
        right.addLast(right.removeFirst());
    }

    public int convertRTL(int ind) { // right to left
        return left.indexOf(right.get(ind));
    }

    public int convertLTR(int ind) { // left to right
        return right.indexOf(left.get(ind));
    }

    public boolean moveNextRotor() {
        boolean res = notch == right.getFirst() && !movedNextRotor && madeStep;

        if (res)
            movedNextRotor = true;

        madeStep = false;

        return res;
    }

    public char getStartingPoint() {
        return startingPoint;
    }

    public char getCurrentPosition() {
        return right.getFirst();
    }

    public char getNotch() { return notch; }

    public int getNotchCurrentPosition() { return right.indexOf(notch); }

    public int getNotchOriginalIndex() { return notchOriginalIndex; }

    public Character getRightCharAt(int charInd) {
        return right.get(charInd);
    }

    public Character getLeftCharAt(int charInd) {
        return left.get(charInd);
    }

    public LinkedList<Character> getRight() {
        return right;
    }

    public LinkedList<Character> getLeft() {
        return left;
    }
}
