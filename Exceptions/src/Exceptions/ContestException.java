package Exceptions;

public class ContestException extends Exception{
    public enum ErrorType {

        MAX_ALLIES_NUM_REACHED("The max amount of allies allowed in the contest has reached."),
        ALLIE_ALREADY_EXISTS("The allie with user name %s already signed in to this contest."),
        ALLIE_DOESNT_EXISTS("The allie with user name %s doesn't exist."),
        AGENT_ALREADY_EXISTS("The agent with user name %s already signed in to this contest."),
        CONTEST_ALREADY_EXIST("The battleField named %s is already exists.");


        private String message;

        ErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

    private ContestException.ErrorType errorType;
    private String messageAddition;

    public ContestException(ContestException.ErrorType e) {
        errorType = e;
    }

    public ContestException(ContestException.ErrorType e, String messageAddition) {
        errorType = e;
        this.messageAddition = messageAddition;
    }

    @Override
    public String getMessage() {
        return String.format(errorType.getMessage(), messageAddition);
    }

}