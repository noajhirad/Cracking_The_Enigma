package EngineManager;

import BruteForce.BruteForceDifficulty;
import BruteForce.BruteForceSettings;
import BruteForce.DecryptionManager;
import Contest.BattleField;
import DTOs.*;
import Exceptions.BruteForceException;
import Exceptions.ConfigException;
import Exceptions.FileException;
import Exceptions.GeneralException;
import EnigmaMachine.*;
import XMLFilesEx3.CTEEnigma;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

import EnigmaMachine.NotchesState;

public class EngineManager implements UBoatEngineManager{
    
    private EnigmaMachine enigma = null;
    private boolean isXMLLoaded = false;
    private boolean isConfigurated = false;

    private EncryptionData currentEncryption = new EncryptionData();
    private DecryptionManager DM;
    private String toDecrypt = null;

    public DTOFileInfo readXML(InputStream inputStream) throws FileException , GeneralException {
        try {
            DTOFileInfo ret = new DTOFileInfo();
            CTEEnigma machineFromFile = XMLReader.deserializeFrom(inputStream);
            MachineFactory machineFactory = new MachineFactory();

            // get battlefield
            BattleField battleField = machineFactory.createBattleField(machineFromFile.getCTEBattlefield());
            DTOBattleField dtoBattleField = new DTOBattleField(battleField.getName(), battleField.getNumOfAllies(),
                    battleField.getDifficulty().toString());
            ret.setDtoBattleField(dtoBattleField);

            // get enigma
            enigma = machineFactory.createMachine(machineFromFile);
            isXMLLoaded = true;
            isConfigurated = false;
            enigma.setConfigurationsToFalse();
            currentEncryption = new EncryptionData();
            ret.setDtoMachineInfo(getMachineSpecifications());

            return ret;
        } catch (JAXBException e) {
            throw new FileException(FileException.ErrorType.JAXB_ERROR);
        }
    }

    public DTOMachineInfo getMachineSpecifications() throws GeneralException{

        if(!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);

        DTOMachineInfo machineInfo = new DTOMachineInfo();
        machineInfo.setRotorsCount(enigma.getRotorsCount());
        machineInfo.setAmountOfDefinedRotors(enigma.getAmountOfDefinedRotors());
        machineInfo.setReflectorsCount(enigma.getReflectorsCount());
        machineInfo.setNumOfProcessedMessages(enigma.getProcessesMessagesNumber());
        machineInfo.setABC(enigma.getVocabulary());
        machineInfo.setDictionary(enigma.getDictionary());

        // get machine configurations - if exists
        if (enigma.isConfigurated()) {
            machineInfo.setOriginalMachineConfig(enigma.getMachineConfig(NotchesState.ORIGINAL_NOTCHES_SETUP));
            machineInfo.setCurrentMachineConfig(enigma.getMachineConfig(NotchesState.CURRENT_NOTCHES_SETUP));
        }
        return machineInfo;
    }

    public DTOMachineConfig setManualConfig(DTOMachineConfig configData) throws Exception {
        if(!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);

        ConfigParser parser = new ConfigParser(this);
        currentEncryption = new EncryptionData();

        // set rotors numbers
        List<Integer> rotorsNumbers = configData.getUsedRotors();
        parser.setUsedRotorsNumbers(rotorsNumbers);
        Collections.reverse(rotorsNumbers);
        enigma.setUsedRotors(rotorsNumbers);

        // set rotors positions
        List<Character> rotorsPositions = configData.getRotorsStartingPoints();
        parser.setRotorsPositions(rotorsPositions);
        Collections.reverse(rotorsPositions);
        enigma.setRotorsStartingPoint(rotorsPositions);

        // set reflector number
        parser.setReflector(configData.getReflectorNumber());
        enigma.setReflector(parser.getReflectorNumber());

        // save to history & change isConfig to true
        saveConfig();
        return getCurrentMachineConfig();
    }

    public void checkIfInVocabulary(char c) throws ConfigException{
        if (!enigma.checkIfInVocabulary(c))
            throw new ConfigException(ConfigException.ErrorType.CHAR_NOT_IN_ABC);
    }

    public DTOMachineConfig setAutomaticConfig() throws GeneralException{
        if (!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);
        currentEncryption = new EncryptionData();
        enigma.automaticCodeConfig();
        saveConfig();
        return enigma.getMachineConfig(NotchesState.CURRENT_NOTCHES_SETUP);
    }

    public String processInput(String input) throws GeneralException {
        if (!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);
        else if(!isConfigurated)
            throw new GeneralException(GeneralException.ErrorType.NO_CONFIGURATION);

        if (input.equals(""))
            throw new GeneralException(GeneralException.ErrorType.EMPTY_ENCRYPTION);

        input = input.toUpperCase();
        int size = input.length();
        for (int i = 0; i < size; i++) {
            Character c = input.charAt(i);
            if(!enigma.checkIfInVocabulary(c))
                throw new GeneralException(GeneralException.ErrorType.NOT_VALID_ENCRYPTION, c);
        }
        
        long start = System.nanoTime();
        String output =  enigma.encrypt(input);
        long end = System.nanoTime();
        long time = end - start;
        currentEncryption.updateEncryptionData(time, input, output);
        return output;
    }

    public void resetCurrentCode() throws GeneralException{
        if(!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);
        else if(!isConfigurated)
            throw new GeneralException(GeneralException.ErrorType.NO_CONFIGURATION);
        currentEncryption = new EncryptionData();
        enigma.reset();
    }

    public DTOMachineConfig getCurrentMachineConfig() throws GeneralException{

        if(!isXMLLoaded) {
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);
        }
        else if(!isConfigurated) {
            throw new GeneralException(GeneralException.ErrorType.NO_CONFIGURATION);
        }

        return enigma.getMachineConfig(NotchesState.CURRENT_NOTCHES_SETUP);
        }

    public DTOMachineConfig originalMachineConfig() throws GeneralException{
        if(!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);
        else if(!isConfigurated)
            throw new GeneralException(GeneralException.ErrorType.NO_CONFIGURATION);
        return enigma.getMachineConfig(NotchesState.ORIGINAL_NOTCHES_SETUP);
    }

    private void saveConfig() {
        //history.addConfig(enigma.getMachineConfig(NotchesState.ORIGINAL_NOTCHES_SETUP));
        isConfigurated = true;
    }

    public void checkIfXMLLoaded() throws GeneralException{
        if(!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);
    }

    public void checkIfConfigurated() throws GeneralException{
        if(!isConfigurated)
            throw new GeneralException(GeneralException.ErrorType.NO_CONFIGURATION);
    }

    public int getReflectorsCount() {
        return enigma.getReflectorsCount();
    }

    public int getRotorsCount() {
        return enigma.getRotorsCount();
    }

    public int getAmountOfDefinedRotors() {
        return enigma.getAmountOfDefinedRotors();
    }

    // brute force encryption
    public String bruteForceEncryption(String input) throws Exception {

        if (!isXMLLoaded)
            throw new GeneralException(GeneralException.ErrorType.XML_NOT_LOADED);
        else if(!isConfigurated)
            throw new GeneralException(GeneralException.ErrorType.NO_CONFIGURATION);

        currentEncryption = new EncryptionData();

        // remove excluded chars
        List<String> excludedChars = enigma.getExcludedChars();
        for(String c: excludedChars) {
            input = input.replace(c, "");
        }

        input = input.toUpperCase();

        // split by " " & check if in dictionary
        String[] inputWords = input.split(" ");
        for(String word : inputWords){
            if (!enigma.isInDictionary(word))
                throw new BruteForceException(BruteForceException.ErrorType.WORD_NOT_IN_DICTIONARY);
        }

        // encrypt input
        String output = processInput(input);
        toDecrypt = output;
        currentEncryption = new EncryptionData();
        return output;
    }


    public void startBruteForce(BruteForceSettings setting) throws Exception{
        EnigmaMachine machine = enigma.getDeepCopy();
        BruteForceDifficulty difficulty = setting.getDifficulty();

        DM = new DecryptionManager(setting, machine);
        DM.startBruteForce();
    }

    public Set<String> getDictionary() {
        return enigma.getDictionary();
    }

    public EnigmaMachine getEnigmaCopy() throws Exception {
        return enigma.getDeepCopy();
    }

    public ObjectInputStream getEnigmaObjInputStream() throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(enigma);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream oInputStream = new ObjectInputStream(bis);

        os.close();

        return oInputStream;
    }

    public String getToDecrypt() {
        return toDecrypt;
    }
}