package MainAgents;

import DTOs.DTOMachineConfig;
import DTOs.DTOMachineInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DTOParser {

    public static List<String> parseDTOMachineInfo(DTOMachineInfo machineInfo) {

        List<String> printableInfo = new ArrayList<>();

        // get rotors amount info
        String rotorsCountInfo = machineInfo.getRotorsCount() + "/" + machineInfo.getAmountOfDefinedRotors();
        printableInfo.add(rotorsCountInfo);

        // get reflector count
        String reflectorsCount = Integer.toString(machineInfo.getReflectorsCount());
        printableInfo.add(reflectorsCount);

        // get number of processed messages
        String numOfProcessedMessages = Integer.toString(machineInfo.getNumOfProcessedMessages());
        printableInfo.add(numOfProcessedMessages);

        return printableInfo;
    }

    public static Map<String, String> parseDTOMachineConfig(DTOMachineConfig machineConfig) {

        Map<String, String> res = new HashMap<>();

        List<Integer> usedRotors = machineConfig.getUsedRotors();
        String usedRotorsStr = "";
        String notchesPos = "";

        for (int i = usedRotors.size() - 1; i > 0 ; i--) {
            usedRotorsStr += usedRotors.get(i) + ",";
            notchesPos += machineConfig.getNotches().get(i) + ",";
        }
        usedRotorsStr += usedRotors.get(0);
        notchesPos += machineConfig.getNotches().get(0);
        res.put("used rotors", usedRotorsStr);
        res.put("nothces pos", notchesPos);

        // print starting point of rotors
        List<Character> startingPoints = machineConfig.getRotorsStartingPoints();
        String rotorsStartingPos = "";
        for (int i = startingPoints.size() - 1; i >= 0; i--) {
            rotorsStartingPos += startingPoints.get(i);
        }
        res.put("rotors starting pos", rotorsStartingPos);

        String reflectorId = getIDStringByValue(machineConfig.getReflectorNumber());
        res.put("reflector ID", reflectorId);

        return res;
    }

    public static String getIDStringByValue(int val) {
        switch (val) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            default: return "";
        }
    }

    public static String parseDTOMachineConfigToString(DTOMachineConfig machineConfig) {
        String res = "";

        // print used rotors numbers
        List<Integer> usedRotors = machineConfig.getUsedRotors();
        res += '<';
        for (int i = usedRotors.size() - 1; i > 0 ; i--) {
            res += usedRotors.get(i) + ",";
        }
        res += usedRotors.get(0) + ">";

        // print starting point of rotors
        List<Character> startingPoints = machineConfig.getRotorsStartingPoints();
        res += '<';
        for (int i = startingPoints.size() - 1; i > 0; i--) {
            res += startingPoints.get(i);
            res += "(" + machineConfig.getNotches().get(i) + ")";
        }
        res += startingPoints.get(0);
        res += "(" + machineConfig.getNotches().get(0) + ")" + ">";

        // print reflector number
        res += '<' + getIDStringByValue(machineConfig.getReflectorNumber()) + '>';

        return res;
    }
}

