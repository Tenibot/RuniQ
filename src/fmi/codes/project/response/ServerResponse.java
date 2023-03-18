package fmi.codes.project.response;

import fmi.codes.project.command.CommandParser;
import fmi.codes.project.command.CommandType;

public class ServerResponse {
    private final ResponseStatus responseStatus;
    private final CommandType commandType;
    private final String clientUsername;
    private final String message;

    public ServerResponse(ServerResponseBuilder serverResponseBuilder) {
        this.responseStatus = serverResponseBuilder.getResponseStatus();
        this.commandType = serverResponseBuilder.getCommandType();
        this.clientUsername = serverResponseBuilder.getClientUsername();
        this.message = serverResponseBuilder.getMessage();
    }

    public String convertToJson() {
        return CommandParser.toJson(this);
    }

    public static ServerResponse convertFromJson(String serverResponseJsonFormat) {
        return CommandParser.fromJson(serverResponseJsonFormat, ServerResponse.class);
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public String getMessage() {
        return message;
    }
}