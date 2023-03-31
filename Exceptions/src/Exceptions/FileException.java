package Exceptions;

public class FileException extends Exception {

    public enum ErrorType{
        FILE_NOT_FOUND("No such file exists.", 1),
        JAXB_ERROR("An error found trying to read the XML file: JAXBException.", 2),
        NOT_XML("The given path doesn't contain xml file.", 3),
        ABC_ODD("The ABC size is odd: should be even.", 4),
        NOT_ENOUGH_ROTORS("The number of defined rotors (%d) is smaller than rotors count.", 5),
        ROTORS_COUNT_OUT_OF_BOUNDS("Rotors count should be between 2 to 99.", 6),
        INVALID_ROTOR_ID("Invalid rotor ID: one or more of the rotors has an invalid ID. " +
               "The IDs should be consecutive numbers starting from 1. The rotor with id %d is invalid." , 7),
        INVALID_ROTOR_MAPPING("Invalid rotor mapping: each character can be mapped only once. " +
                "The rotor with ID %d contains a character that is mapped twice.", 8),
        INVALID_NOTCH("Invalid notch: The notch of the rotor with ID %d is beyond the vocabulary range.",9),
        INVALID_REFLECTOR_ID("Invalid reflector ID: one or more of the reflectors has an invalid ID. " +
                "The IDs should be consecutive rome numbers starting from I.", 10),
        INVALID_REFLECTOR_MAPPING("Invalid reflector mapping: One or more of the reflectors has a number that mapped to itself or mapped twice.", 11),
        INVALID_ROTOR_ABC("One or more of the characters in the rotor with ID %d is not valid." +
                "All of the characters in the rotor should be in the defined machine ABC.",12),
        INVALID_REFLECTOR_LENGTH("One or more of the reflectors has invalid number of mappings. ", 13),
        INVALID_REFLECTOR_VALUE("One or more of the rotors contain invalid number for mapping.", 14),
        INVALID_ROTOR_LENGTH("The rotor with ID %d has invalid amount of mappings." + System.lineSeparator() +
                "The number of mappings should correspond with the length of the ABC.", 15),
        TOO_MUCH_DEFINED_REFLECTORS("The amount of defined reflectors is too small/large. The amount of reflectors should be between 1 and 5.", 16),
        INVALID_AGENTS_NUM("The agents number should be between 2 and 50.", 17),
        INVALID_ALLIES_NUM("The allies number should be larger than 0.", 18),
        INVALID_DIFFICULTY("Invalid level. The level should be one of the following options:" + System.lineSeparator() +
                "Easy, Medium, Hard or Impossible.", 19),
        CONTEST_NAME_EXIST("The contest name defined in file is already exist.", 20);


        private int errorNumber;
        private String message;

        ErrorType(String message, int errorNumber) {
            this.message = message;
            this.errorNumber = errorNumber;
        }

        public String getMessage() {
            return this.message;
        }
    }

    private ErrorType errorType;
    private int messageAddition;

    public FileException(ErrorType e, int messageAddition) {
        errorType = e;
        this.messageAddition = messageAddition;
    }

    public FileException(ErrorType e) {
        errorType = e;
    }

    @Override
    public String getMessage() {
        return String.format(errorType.getMessage(), messageAddition);
    }
}