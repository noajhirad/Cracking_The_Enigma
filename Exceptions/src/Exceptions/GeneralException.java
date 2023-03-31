package Exceptions;

public class GeneralException extends Exception {

    public enum ErrorType {

        XML_NOT_LOADED("No XML file was loaded. Please load an XML file first."),
        NO_CONFIGURATION("No machine configurations were made. Please set your configurations first."),
        NOT_VALID_ENCRYPTION("Your input message contains invalid characters. The character %c isn't in the machine ABC"),
        INVALID_MAIN_MENU_INPUT("Invalid input. Please insert a number between 1 and 10."),
        UNKNOWN_ERROR("Oops.. Something went wrong."),
        MACHINE_LOADING_ERROR("The file doesn't exists or doesn't contain a valid machine. Please enter a file path without file type suffix."),
        EMPTY_ENCRYPTION("Your message is empty. Please insert a valid message."),
        UNFINISHED_ENCRYPTION("Please finish your former char-by-char encryption first."),
        NO_AGENTS_FOUND("You must first have at least one logged-in agent to mark yourself as ready.");

        private String message;

        ErrorType(String message) { this.message = message; }

        public String getMessage() {
            return message;
        }
    }

    private ErrorType errorType;
    private char c;

    public GeneralException(ErrorType e) {
        errorType = e;
    }

    public GeneralException(ErrorType e, char c) {
        errorType = e;
        this.c = c;
    }

    @Override
    public String getMessage() {
        return String.format(errorType.getMessage(), c);
    }


    public ErrorType getType() { return errorType; }
}
