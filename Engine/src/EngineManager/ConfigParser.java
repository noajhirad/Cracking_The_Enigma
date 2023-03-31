package EngineManager;

import Exceptions.ConfigException;

import java.util.ArrayList;
import java.util.List;

public class ConfigParser {
    private EngineManager engineManager;
    private List<Integer> rotorsNumbers = new ArrayList<>();
    private List<Character> rotorsPos = new ArrayList<>();
    private int reflectorNumber;
    List<Integer> rotorsNumValidation = new ArrayList<>();


    public ConfigParser(EngineManager engineManager) { this.engineManager = engineManager; }

    private void checkRotorNum(int rotorNum) throws ConfigException {
        int definedRotors = engineManager.getAmountOfDefinedRotors();
        if (rotorNum<1 || rotorNum > definedRotors)
            throw new ConfigException(ConfigException.ErrorType.OUT_OF_BOUND_ROTOR_NUMBER,definedRotors);
    }

    public List<Integer> getRotorsNumbers() { return rotorsNumbers; }

    public List<Character> getRotorsPositions() { return rotorsPos; }

    public void setReflector(int reflectorNum) throws ConfigException {
        try {
            checkIfValidReflectorNumber(reflectorNum);
            this.reflectorNumber = reflectorNum;
        }
        catch (NumberFormatException e) {
            throw new ConfigException(ConfigException.ErrorType.INVALID_REFLECTOR_NUMBER);
        }
    }

    public void setReflector(String input) throws ConfigException {
        try {
            int reflectorNum = Integer.parseInt(input);
            checkIfValidReflectorNumber(reflectorNum);
            this.reflectorNumber = reflectorNum;
        }
        catch (NumberFormatException e) {
            throw new ConfigException(ConfigException.ErrorType.INVALID_REFLECTOR_NUMBER);
        }
    }

    private void checkIfValidReflectorNumber(int reflectorNum) throws ConfigException {
        int reflectorCount = engineManager.getReflectorsCount();
        if(reflectorNum<1 || reflectorNum > reflectorCount)
            throw new ConfigException(ConfigException.ErrorType.OUT_OF_BOUND_REFLECTOR_NUMBER, reflectorCount);
    }

    private void checkRotorsAmount(int rotosAmount) throws ConfigException {
        int rotorCount = engineManager.getRotorsCount();
        if(rotosAmount != rotorCount)
            throw new ConfigException(ConfigException.ErrorType.INVALID_ROTORS_AMOUNT, rotorCount);
    }

    public int getReflectorNumber() { return reflectorNumber; }

    public void setUsedRotorsNumbers(List<Integer> input) throws ConfigException {
        try {
            for (int rotorNum : input) {
                checkRotorNum(rotorNum);
                if (rotorsNumValidation.contains(rotorNum))
                    throw new ConfigException(ConfigException.ErrorType.DOUBLE_ROTOR_SELECTION, rotorNum);
                rotorsNumValidation.add(rotorNum);
                rotorsNumbers.add(rotorNum);
            }
            checkRotorsAmount(input.size());
        }
        catch (NumberFormatException e) {
            throw new ConfigException(ConfigException.ErrorType.INVALID_ROTORS_NUMBER);
        }
    }

    public void setUsedRotorsNumbers(String input) throws ConfigException {

        List<Integer> validation = new ArrayList<>();
        String[] inputArray = input.split(",");

        try {
            for (String str : inputArray) {
                int rotorNum = Integer.parseInt(str);
                checkRotorNum(rotorNum);
                if (validation.contains(rotorNum))
                    throw new ConfigException(ConfigException.ErrorType.DOUBLE_ROTOR_SELECTION, rotorNum);
                validation.add(rotorNum);
                rotorsNumbers.add(rotorNum);
            }
            checkRotorsAmount(inputArray.length);
        }
        catch (NumberFormatException e) {
            throw new ConfigException(ConfigException.ErrorType.INVALID_ROTORS_NUMBER);
        }
    }

    public void setRotorsPositions(String input) throws ConfigException{

        if (input.equals(""))
            throw new ConfigException(ConfigException.ErrorType.EMPTY_INPUT);

        input = input.toUpperCase();

        for (int i = 0; i < input.length(); i++) {
            Character rotorPos = input.charAt(i);
            engineManager.checkIfInVocabulary(rotorPos);
            rotorsPos.add(rotorPos);
        }
        checkRotorsAmount(input.length());
    }

    public void setRotorsPositions(List<Character> input) throws ConfigException {
        for(Character c:input){
            engineManager.checkIfInVocabulary(c);
            rotorsPos.add(c);
        }
        checkRotorsAmount(input.size());
    }

    public void clear(){
        rotorsNumbers = new ArrayList<>();
        rotorsNumValidation = new ArrayList<>();
        rotorsPos = new ArrayList<>();
        reflectorNumber = 0;
    }
}