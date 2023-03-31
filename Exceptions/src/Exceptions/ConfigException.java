package Exceptions;

public class ConfigException extends Exception {
    public enum ErrorType{
        OUT_OF_BOUND_ROTOR_NUMBER("One or more of the rotors numbers is out of bounds. The numbers should be between 1 and %d.",1),
        INVALID_ROTORS_AMOUNT("The amount of data entered is larger/smaller than the rotors count defined in the XML file. Please insert %d rotors data.", 2),
        INVALID_ROTORS_NUMBER("One or more of the rotors numbers is invalid number. The input should be integers only.", 3),
        CHAR_NOT_IN_ABC("One or more of the characters inserted is invalid. The characters should be defined in the machine ABC.",4),
        INVALID_REFLECTOR_NUMBER("The reflector number is invalid number. The input should be an integer.",6),
        OUT_OF_BOUND_REFLECTOR_NUMBER("The reflector number is out of bounds. The number should be between 1 and %d.",7),
        DOUBLE_MAPPING("One or more of the characters inserted is repeating itself. Each character can only be switched once.", 8),
        EMPTY_INPUT("An empty input is invalid. Please insert a single character from the defined ABC.", 9),
        INVALID_PLUGS_CHARS_AMOUNT("Your input is invalid. Please insert pair of characters from the defined ABC.", 10),
        DOUBLE_ROTOR_SELECTION("The rotor with ID %d is already selected. Each rotor can only appear once.", 11),
        EMPTY_SELECTION("Please fill all of the fields.", 12);

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


    public ConfigException(ErrorType e) {
        errorType = e;
    }

    public ConfigException(ErrorType e, int number) {
        errorType = e;
        messageAddition = number;
    }

    @Override
    public String getMessage() {
        return String.format(errorType.getMessage(), messageAddition);
    }

}
