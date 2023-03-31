package AgentBruteForce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ABCCounter {
    private List<Character> ABC;
    private int numOfRotors;

    public ABCCounter(List<Character> abc, int numOfRotors) {
        this.numOfRotors = numOfRotors;
        ABC = abc;
    }

    public List<Character> increment(List<Character> curr) {
        List<Character> res = new ArrayList<>(curr);

        for (int i = curr.size() - 1; i >= 0 ; i--) {
            char c = res.get(i);
            char newChar = incrementSingleChar(c);
            res.set(i,newChar);
            if(newChar != ABC.get(0))
                break;
        }

        return res;
    }

    private char incrementSingleChar(char c) {
        int index = ABC.indexOf(c) + 1;
        if(index >= ABC.size())
            index = 0;
        return ABC.get(index);
    }

    public List<Character> getStartingPositionByIndex(int index) {

        List<Character> res = new ArrayList<>();
        for (int i = 0; i < numOfRotors; i++) {
            int mod = index % ABC.size();
            res.add(ABC.get(mod));
            index /= ABC.size();
        }
        Collections.reverse(res);
        return res;
    }
}
