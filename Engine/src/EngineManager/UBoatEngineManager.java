package EngineManager;

import DTOs.*;
import EnigmaMachine.EnigmaMachine;
import Exceptions.FileException;
import Exceptions.GeneralException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Set;

public interface UBoatEngineManager {
    public DTOFileInfo readXML(InputStream inputStream) throws FileException, GeneralException;
    public DTOMachineConfig setAutomaticConfig() throws GeneralException;
    public DTOMachineConfig setManualConfig(DTOMachineConfig configData) throws Exception;
    public String bruteForceEncryption(String input) throws Exception;
    public void resetCurrentCode() throws GeneralException;
    public DTOMachineConfig getCurrentMachineConfig() throws GeneralException;
    public Set<String> getDictionary();
    public ObjectInputStream getEnigmaObjInputStream() throws Exception;
    public String getToDecrypt();
    public EnigmaMachine getEnigmaCopy() throws Exception;
}
