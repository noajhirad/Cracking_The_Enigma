package Exceptions;

public class BruteForceException extends Exception{
    public enum ErrorType {

        NO_ENCRYPTION("No encryption were done yet."),
        MISSING_FIELD("Please fill all of the fields."),
        INVALID_TASK_SIZE("Task size should be a positive integer."),
        WORD_NOT_IN_DICTIONARY("One or more of the words aren't in the dictionary."),
        PLUGS_ARENT_ALLOWED("Brute force isn't supporting the definition of plugs. Please remove the plugs first.");

        private String message;

        ErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

    private BruteForceException.ErrorType errorType;


    public BruteForceException(BruteForceException.ErrorType e) {
        errorType = e;
    }

    @Override
    public String getMessage() {
        return String.format(errorType.getMessage());
    }

}
