package EnigmaMachineAgent;
import DTOs.DTOMachineConfig;

import java.io.*;
import java.util.*;

public class EnigmaMachine implements Serializable {

    private List<Rotor> allRotors;
    private List<Integer> usedRotors;
    private List<Reflector> reflectors;
    private int reflectorNumber;
    private List<Character> vocabulary;
    private int rotorsCount;
    private int processedMessagesCount = 0;
    private Set<String> dictionary;
    private List<String> excludedChars;

    private boolean isReflectorSet = false;
    private boolean isUsedRotorsSet = false;
    private boolean isRotorsPosSet = false;

    public EnigmaMachine(MachineData data) {

        List<LinkedList<Character>> rotorsRight = data.getRotorsRight();
        List<LinkedList<Character>> rotorsLeft= data.getRotorsLeft();
        List<Integer> notchs = data.getNotchs();
        List<Map<Integer, Integer>> allReflectors = data.getAllReflectors();
        List<Character> voc = data.getVoc();

        // init rotors
        allRotors = new ArrayList<>();
        int size = rotorsRight.size();
        for (int i = 0; i < size; i++)
            allRotors.add(new Rotor(notchs.get(i), rotorsRight.get(i), rotorsLeft.get(i)));
        usedRotors = null;

        rotorsCount = data.getRotorsCount();

        // init reflectors
        reflectors = new ArrayList<>();
        for (Map<Integer, Integer> reflector : allReflectors)
            reflectors.add(new Reflector(reflector));

        vocabulary = voc;
        dictionary = data.getDictionary();
        excludedChars = data.getExcludedChars();
    }

    public String encrypt(String message){
        String result = "";

        int size = message.length();

        for (int i = 0; i < size; i++) {
            moveRotors();
            Character c = message.charAt(i); // get current char from message
            result = result + encryptSingleChar(c); // add to encrypted string
        }
        processedMessagesCount++;
        return result;

    }

    private Character encryptSingleChar(Character c){
        int charInd = getIndexFromChar(c); // convert char -> index

        for (int rotorInd : usedRotors)     // go throw rotors
            charInd = allRotors.get(rotorInd - 1).convertRTL(charInd);

        charInd = reflectors.get(reflectorNumber - 1).getMatchingValue(charInd + 1) - 1; // reflector

        for (int i = usedRotors.size() - 1; i >= 0; i--) { // go back throw rotors
            charInd = allRotors.get(usedRotors.get(i) - 1).convertLTR(charInd);
        }

        c = getCharFromIndex(charInd + 1); // convert index -> char

        return c;
    }

    public void moveRotors() {

        Rotor firstRotor = allRotors.get(usedRotors.get(0) - 1);
        firstRotor.step();                              // move first rotor
        boolean moveNext = firstRotor.moveNextRotor();

        for (int i = 1; i < usedRotors.size() ; i++) {     // go over rotors, skipping the first one
            Rotor rotor = allRotors.get(usedRotors.get(i) - 1);

            if (moveNext)
                rotor.step();

            moveNext = rotor.moveNextRotor();
        }
    }

    private Character getCharFromIndex(int charInd) {
        return vocabulary.get(charInd - 1);
    }

    private int getIndexFromChar(Character c) {
        return vocabulary.indexOf(c);
    }

    public void reset() {
        for (int rotorInd : usedRotors)
            allRotors.get(rotorInd - 1).reset();
    }

    public DTOMachineConfig getMachineConfig(NotchesState state){

        if (!isConfigurated())
            return null;
        DTOMachineConfig machineConfig = new DTOMachineConfig();

        machineConfig.setUsedRotor(usedRotors);

        machineConfig.setReflectorNumber(reflectorNumber);

        switch(state) {
            case ORIGINAL_NOTCHES_SETUP:
                machineConfig.setNotches(getOriginalNotches());

                for (int i = 0; i < usedRotors.size(); i++)
                    machineConfig.setRotorsPosition(allRotors.get(usedRotors.get(i)-1).getStartingPoint());

                break;
            case CURRENT_NOTCHES_SETUP:
                machineConfig.setNotches(getNotchesCurrentPositions());

                for (int i = 0; i < usedRotors.size(); i++)
                    machineConfig.setRotorsPosition(allRotors.get(usedRotors.get(i)-1).getCurrentPosition());

                break;
        }

        return machineConfig;
    }

    public void setUsedRotors(List<Integer> rotors) {
        usedRotors = rotors;
        isUsedRotorsSet = true;
    }

    public void setReflector(int reflector) {
        reflectorNumber = reflector;
        isReflectorSet = true;
    }

    public void setRotorsStartingPoint(List<Character> rotorsStartingPoint) {

        int ind = 0;
        for (int rotor : usedRotors) {
            allRotors.get(rotor - 1).setStartingPoint(rotorsStartingPoint.get(ind));
            ind++;
        }
        isRotorsPosSet = true;
    }

    public void setRotorsStartingPointBruteForce(List<Character> rotorsStartingPoint) {

        List<Character> reversedList = new ArrayList<>();
        for (int i = rotorsStartingPoint.size()-1; i >= 0 ; i--)
            reversedList.add(rotorsStartingPoint.get(i));

        int ind = 0;
        for (int rotor : usedRotors) {
            allRotors.get(rotor - 1).setStartingPoint(reversedList.get(ind));
            ind++;
        }

        isRotorsPosSet = true;
    }

    public void automaticCodeConfig() {
        autoSetRotors();
        autoSetReflector();
    }

    private void autoSetReflector() {
        Random rand = new Random();
        int reflectorNumber = rand.nextInt(reflectors.size()) + 1;
        setReflector(reflectorNumber);
        isReflectorSet = true;
    }

    private void autoSetRotors() {
        Random rand = new Random();

        // set used rotors
        usedRotors = new ArrayList<>();
        while (usedRotors.size() < rotorsCount){
            int num = rand.nextInt(allRotors.size()) + 1;
            if (!usedRotors.contains(num))
                usedRotors.add(num);
        }
        isUsedRotorsSet = true;

        // set rotors starting position
        int ABCSize = vocabulary.size();
        for (int i=0; i<rotorsCount; i++){
            Rotor currentRotor = allRotors.get(usedRotors.get(i)-1);
            int posVal = rand.nextInt(ABCSize) + 1;
            currentRotor.setStartingPoint(getCharFromIndex(posVal));
        }

        isRotorsPosSet = true;
    }

    public boolean isConfigurated() {
        return isReflectorSet && isRotorsPosSet && isUsedRotorsSet;
    }

    public int getAmountOfDefinedRotors() { return allRotors.size(); }

    public int getRotorsCount() { return rotorsCount; }

    public List<Integer> getOriginalNotches() {
        List<Integer> notches = new ArrayList<>();
        int currNotch;

        for(int rotorInd : usedRotors) {
            currNotch = allRotors.get(rotorInd - 1).getNotchOriginalIndex();
            notches.add(currNotch);
        }

        return notches;
    }

    public List<Integer> getNotchesCurrentPositions() {
        List<Integer> notches = new ArrayList<>();
        int currNotch;

        for(int rotorInd : usedRotors) {
            currNotch = allRotors.get(rotorInd - 1).getNotchCurrentPosition();
            notches.add(currNotch);
        }

        return notches;
    }

    public boolean checkIfInVocabulary(char c) {
        return vocabulary.contains(c);
    }

    public int getReflectorsCount() {
        return reflectors.size();
    }

    public int getProcessesMessagesNumber() {
        return processedMessagesCount;
    }

    public void setConfigurationsToFalse() {
        isUsedRotorsSet = isRotorsPosSet = isReflectorSet = false;
    }

    public List<Character> getVocabulary() {
        return vocabulary;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public List<String> getExcludedChars() {
        return excludedChars;
    }

    public boolean isInDictionary(String word) {
        return dictionary.contains(word);
    }

    public EnigmaMachine getDeepCopy() throws Exception{

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream oInputStream = new ObjectInputStream(bis);
        EnigmaMachine enigmaCopy = (EnigmaMachine) oInputStream.readObject();

        os.close();

        return enigmaCopy;
    }

    public List<Character> getRotorsPositions(){
        return getMachineConfig(NotchesState.ORIGINAL_NOTCHES_SETUP).getRotorsStartingPoints();
    }

    public List<Reflector> getReflectorsList() {
        return reflectors;
    }

    public List<Integer> getUsedRotors() {
        return usedRotors;
    }

    public List<String> getCharPath(Character c){

        List<String> res = new ArrayList<>();
        res.add(c.toString()); // input
        res.add(c.toString()); // plugs in

        res.add(c.toString()); // plugs out

        int charInd = getIndexFromChar(c);

        for (int rotorInd : usedRotors) {
            Rotor current = allRotors.get(rotorInd - 1);
            res.add(current.getRightCharAt(charInd).toString()); // rotors in
            charInd = current.convertRTL(charInd);
            res.add(current.getLeftCharAt(charInd).toString()); // rotors out
        }

        res.add(Integer.toString(charInd)); // reflector in
        res.add(Integer.toString(charInd)); // reflector in (other side)

        charInd = reflectors.get(reflectorNumber - 1).getMatchingValue(charInd + 1) - 1;

        res.add(Integer.toString(charInd)); // reflector out

        for (int i = usedRotors.size() - 1; i >= 0; i--) {
            Rotor current = allRotors.get(usedRotors.get(i) - 1);
            res.add(current.getLeftCharAt(charInd).toString()); // rotors in
            charInd = current.convertLTR(charInd);
            res.add(current.getRightCharAt(charInd).toString()); // rotors right
        }

        c = getCharFromIndex(charInd + 1);
        res.add(c.toString()); // plugs in
        res.add(c.toString()); // plugs out = result
        res.add(c.toString()); // plugs out = result

        return res;
    }

    public List<AnimationRotor> getRotors() {
        List<AnimationRotor> res = new ArrayList<>();
        for (int i = 0; i < usedRotors.size() ; i++) {
            res.add(allRotors.get(usedRotors.get(i) - 1));
        }
        return res;
    }

    public Map<Integer, Integer> getReflectorMap() {
        return reflectors.get(reflectorNumber-1).getMap();
    }

}



