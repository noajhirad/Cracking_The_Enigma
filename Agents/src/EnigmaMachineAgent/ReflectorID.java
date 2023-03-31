package EnigmaMachineAgent;

import Exceptions.FileException;

public enum ReflectorID {
    I("I",1),
    II("II",2),
    III("III",3),
    IV("IV",4),
    V("V",5);

    private String romeNumber;
    private int value;

    private ReflectorID(String romeNumber, int value) {
        this.romeNumber = romeNumber;
        this.value = value;
    }

    //return 0 if r1 value is equal to r2, 1 if greater, -1 if smaller
    public static int compareByValue(ReflectorID r1, ReflectorID r2) {
        Integer val1 = r1.getValue();
        return val1.compareTo(r2.getValue());
    }

    public String getRomeNumber() {
        return romeNumber;
    }

    public int getValue() {
        return value;
    }

    public static ReflectorID getIDByString(String s) throws FileException{
        switch (s) {
            case "I":
                return I;
            case "II":
                return II;
            case "III":
                return III;
            case "IV":
                return IV;
            case "V":
                return V;
            default:
                throw new FileException(FileException.ErrorType.INVALID_REFLECTOR_ID);
        }
    }

    private static ReflectorID getIDByValue(int value) {
        switch (value) {
            case 1:
                return I;
            case 2:
                return II;
            case 3:
                return III;
            case 4:
                return IV;
            case 5:
                return V;
            default:
                return null;
        }
    }

    public static String getOptionsString(int size) {
        String result = "";

        for (int i = 0; i < size; i++) {
            ReflectorID curr = getIDByValue(i+1);
            result += curr.value + ". " + curr.romeNumber;
            if (i != size-1)
                result += System.lineSeparator();
        }

        return result;
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

    public static int getIntByString(String s) {
        switch (s) {
            case "I":
                return 1;
            case "II":
                return 2;
            case "III":
                return 3;
            case "IV":
                return 4;
            case "V":
                return 5;
            default:
                return -1;
        }
    }
}
