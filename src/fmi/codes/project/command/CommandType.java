package fmi.codes.project.command;

public enum CommandType {

    JOIN_SESSION("join-session"),
    LOGIN("login"),
    REGISTER("register"),
    CREATE_MAP("create-map"),
    START_SESSION("start-session"),

    ////////////////////////////////////////////////////////////

    CREATE_TEAM("create-team"),
    GET_NEXT_TASK("get-next-task"),
    FINISH_TASK("finish-task"),
    UPDATE_LEADERBOARD("update-leaderboard"),
    UNKNOWN("unknown");

    private final String commandName;

    CommandType(final String commandName) {
        this.commandName = commandName;
    }

    public static CommandType getCommandType(final String commandName) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.commandName.equals(commandName)) {
                return commandType;
            }
        }

        return UNKNOWN;
    }
}
