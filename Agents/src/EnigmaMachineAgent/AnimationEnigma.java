package EnigmaMachineAgent;

import java.util.List;
import java.util.Map;

public interface AnimationEnigma {
    public List<String> getCharPath(Character c);
    public List<AnimationRotor> getRotors();
    public Map<Integer, Integer> getReflectorMap();
    public Map<Character, Character> getPlugsMap();
    public List<Character> getVocabulary();
    public void moveRotors();
}
