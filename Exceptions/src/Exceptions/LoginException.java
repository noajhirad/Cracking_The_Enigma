package Exceptions;

public class LoginException extends Exception{

    public enum ErrorType {

        INVALID_TASK_SIZE("The task size should be a positive integer."),
        EMPTY_ALLIE_TEAM("Please choose an allie team.");
        private String message;

        ErrorType(String message) { this.message = message; }

        public String getMessage() {
            return message;
        }
    }

    private LoginException.ErrorType errorType;

    public LoginException(LoginException.ErrorType e) {
        errorType = e;
    }

    @Override
    public String getMessage() {
        return errorType.getMessage();
    }


    public LoginException.ErrorType getType() { return errorType; }
}
