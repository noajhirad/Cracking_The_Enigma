package EngineManager;
import BruteForce.BruteForceDifficulty;
import Contest.BattleField;
import Exceptions.FileException;
import java.util.*;
import java.util.stream.Collectors;
import EnigmaMachine.*;
import XMLFilesEx3.*;

public class MachineFactory {
    private MachineData data = new MachineData();

    public EnigmaMachine createMachine(CTEEnigma cteEnigma) throws FileException {

        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        // get vocabulary
        String ABC = cteMachine.getABC();
        getMachineVocabulary(ABC);

        // get rotors count
        int rotorsCount = cteMachine.getRotorsCount();
        data.setRotorsCount(rotorsCount);

        // get rotors info
        List<CTERotor> allRotors = cteMachine.getCTERotors().getCTERotor();
        getRotorsInfo(allRotors);

        // get reflectors info
        List<CTEReflector> allCTEReflectors = cteMachine.getCTEReflectors().getCTEReflector(); // list of all reflectors
        getReflectorsInfo(allCTEReflectors);

        // get dictionary
        CTEDecipher decipher = cteEnigma.getCTEDecipher();
        CTEDictionary cteDictionary = decipher.getCTEDictionary();
        getDictionary(cteDictionary);

        return new EnigmaMachine(data);

    }

    public BattleField createBattleField(CTEBattlefield cteBattlefield) throws FileException {

        BattleField battleField = new BattleField();
        battleField.setName(cteBattlefield.getBattleName());

        int alliesNumber = cteBattlefield.getAllies();
        if(alliesNumber <= 0)
            throw new FileException(FileException.ErrorType.INVALID_ALLIES_NUM);
        battleField.setNumOfAllies(alliesNumber);

        BruteForceDifficulty difficulty = BruteForceDifficulty.convertStringToValue(cteBattlefield.getLevel());
        if(difficulty == null)
            throw new FileException(FileException.ErrorType.INVALID_DIFFICULTY);
        battleField.setDifficulty(difficulty);

        return battleField;
    }

    private void getDictionary(CTEDictionary cteDictionary) {

        String words = cteDictionary.getWords();
        words = words.toUpperCase();

        String excludedChars = cteDictionary.getExcludeChars();

        words = words.replaceAll("[\t\n]","");

        String[] splited = excludedChars.split("");
        data.setExcludedChars(Arrays.asList(splited));

        for (String c: splited)
            words = words.replace(c, "");

        String[] tmp = words.split(" ");
        Set<String> dictionary = new HashSet<>(Arrays.asList(tmp));

        data.setDictionary(dictionary);
    }

    private void getReflectorsInfo(List<CTEReflector> allCTEReflectors) throws FileException {

        validateReflectorsAmount(allCTEReflectors.size());

        List<Map<Integer,Integer>> allReflectors = new ArrayList<>();
        validateReflectorsID(allCTEReflectors);

        Collections.sort(allCTEReflectors, MachineFactory::compareByReflectorId);

        for (CTEReflector reflector : allCTEReflectors){
            validateReflectorLength(reflector.getCTEReflect().size());
            Map<Integer,Integer> newReflector = new HashMap<>();
            List<Integer> validate = new ArrayList<>();

            for (CTEReflect reflect : reflector.getCTEReflect()){
                int input = reflect.getInput();
                int output = reflect.getOutput();
                if(validate.contains(input) || validate.contains(output))
                    throw new FileException(FileException.ErrorType.INVALID_REFLECTOR_MAPPING);
                validateReflectorMapping(input, output);
                newReflector.put(input, output);
                newReflector.put(output, input);
                validate.add(input);
                validate.add(output);
            }
            allReflectors.add(newReflector);
        }
        data.setAllReflectors(allReflectors);
    }

    private void validateReflectorsAmount(int amountOfDefinedReflectors) throws FileException {
        if (amountOfDefinedReflectors < 1 || amountOfDefinedReflectors > 5)
            throw new FileException(FileException.ErrorType.TOO_MUCH_DEFINED_REFLECTORS);
    }

    private void validateReflectorLength(int size) throws FileException{
        if (size!= data.getVoc().size() / 2)
            throw new FileException(FileException.ErrorType.INVALID_REFLECTOR_LENGTH);
    }

    private void validateReflectorsID(List<CTEReflector> allCTEReflectors) throws FileException {
        List<ReflectorID> reflectorIDs = new ArrayList<>();
        for (CTEReflector r : allCTEReflectors) {
            reflectorIDs.add(ReflectorID.getIDByString(r.getId()));
        }

        Collections.sort(reflectorIDs, ReflectorID::compareByValue);

        for (int i = 0; i < reflectorIDs.size(); i++) {
            if(reflectorIDs.get(i).getValue() != i + 1)
                throw new FileException(FileException.ErrorType.INVALID_REFLECTOR_ID);
        }

    }

    private void validateReflectorMapping(int input, int output) throws FileException {
        if (output == input)
            throw new FileException(FileException.ErrorType.INVALID_REFLECTOR_MAPPING);
        int size = data.getVoc().size();
        if (input > size || input < 1 || output > size || output < 1)
            throw new FileException(FileException.ErrorType.INVALID_REFLECTOR_VALUE);
    }

    private void getRotorsInfo(List<CTERotor> allRotors) throws FileException{

        Collections.sort(allRotors, MachineFactory::compareByRotorId);
        validateRotorsID(allRotors);

        // go over all rotors, add to list r & list l & list notch
        List<LinkedList<Character>> rotorsRight = new ArrayList<>();
        List<LinkedList<Character>> rotorsLeft = new ArrayList<>();
        List<Integer> notchs = new ArrayList<>();

        for (CTERotor rotor : allRotors){
            LinkedList<Character> right = new LinkedList<>();
            LinkedList<Character> left = new LinkedList<>();
            validateRotorLength(rotor.getCTEPositioning().size(), rotor.getId());
            for (CTEPositioning pos : rotor.getCTEPositioning()) {
                char rightChar = pos.getRight().toUpperCase().charAt(0);
                char leftChar = pos.getLeft().toUpperCase().charAt(0);
                checkIfInVoc(rightChar, leftChar, rotor.getId());
                right.add(rightChar);
                left.add(leftChar);
            }

            validateRotorMapping(right, left, rotor.getId());
            rotorsRight.add(right);
            rotorsLeft.add(left);

            validateNotch(rotor.getNotch(), rotor.getId());
            notchs.add(rotor.getNotch());
        }

        validateRotorsCount(rotorsLeft.size());
        data.setRotorsLeft(rotorsLeft);
        data.setRotorsRight(rotorsRight);
        data.setNotchs(notchs);
    }

    private void validateRotorLength(int size, int id) throws FileException{
        if (size != data.getVoc().size())
            throw new FileException(FileException.ErrorType.INVALID_ROTOR_LENGTH,id);
    }

    private void checkIfInVoc(char rightChar, char leftChar, int rotorID) throws FileException {
        if (!data.getVoc().contains(rightChar) || !data.getVoc().contains(leftChar))
            throw new FileException(FileException.ErrorType.INVALID_ROTOR_ABC, rotorID);
    }

    private void validateRotorsCount(int definedRotorsAmount) throws FileException {
        if (definedRotorsAmount < data.getRotorsCount())
            throw new FileException(FileException.ErrorType.NOT_ENOUGH_ROTORS, definedRotorsAmount);
        if (data.getRotorsCount() > 99 || data.getRotorsCount() < 2)
            throw new FileException(FileException.ErrorType.ROTORS_COUNT_OUT_OF_BOUNDS);
    }

    private void validateRotorsID(List<CTERotor> allRotors) throws FileException {
        for(int i=0; i<allRotors.size(); i++){
            int id = allRotors.get(i).getId();
            if (id != i+1)
                throw new FileException(FileException.ErrorType.INVALID_ROTOR_ID, id);
        }
    }

    private void validateNotch(int notch, int rotorID) throws FileException{
        if (notch > data.getVoc().size() || notch < 1)
            throw new FileException(FileException.ErrorType.INVALID_NOTCH, rotorID);
    }

    // check if a letter is mapped twice in the rotor
    private void validateRotorMapping(LinkedList<Character> right, LinkedList<Character> left, int rotorID) throws FileException{
        Set<Character> rightRotor = new HashSet<>(right);
        Set<Character> leftRotor = new HashSet<>(left);

        if (rightRotor.size() < data.getVoc().size() || leftRotor.size() < data.getVoc().size())
            throw new FileException(FileException.ErrorType.INVALID_ROTOR_MAPPING, rotorID);
    }

    private void getMachineVocabulary(String ABC) throws FileException {
        ABC = ABC.toUpperCase();
        ABC = ABC.replaceAll("[\t\n]","");
        List<Character> voc = ABC.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        if (voc.size() % 2 == 1)
            throw new FileException(FileException.ErrorType.ABC_ODD);
        this.data.setVoc(voc);
    }

    //return 0 if r1 Id is equal to r2, 1 if greater, -1 if smaller
    public static int compareByRotorId(CTERotor r1, CTERotor r2) {
        Integer Id1 = r1.getId();
        return Id1.compareTo(r2.getId());
    }

    //return 0 if r1 Id is equal to r2, 1 if greater, -1 if smaller
    public static int compareByReflectorId(CTEReflector r1, CTEReflector r2) {
        String val1 = r1.getId();
        return val1.compareTo(r2.getId());
    }
}



