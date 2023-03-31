package EnigmaMachine;

import java.io.Serializable;
import java.util.Map;

public class Reflector implements Serializable {

    private Map<Integer,Integer> values;

    public Reflector(Map<Integer,Integer> vals) {
        values = vals;
    }

    public int getMatchingValue(int pos) { return values.get(pos); }

    public Map<Integer, Integer> getMap() {
        return values;
    }
}