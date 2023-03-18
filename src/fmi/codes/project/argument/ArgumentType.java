package fmi.codes.project.argument;

public enum ArgumentType {

    USERNAME("username"),
    PASSWORD("password"),
    TEAM("team"),
    QUIZ("quiz"),
    QUIZ_ID("quiz_id");

    private final String argumentName;

    ArgumentType(final String argumentName) {
        this.argumentName = argumentName;
    }

    public String getArgumentName() {
        return argumentName;
    }
}
