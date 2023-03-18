package fmi.codes.project.command;

import com.google.gson.Gson;
import fmi.codes.project.argument.ArgumentParser;
import fmi.codes.project.exception.InvalidCommandSyntaxException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandParser {
//    private static final char COMMAND_PREFIX = '/';
//    private static final int COMMAND_SPLIT_LIMIT = 2;
//    private static final String COMMAND_SPLIT_REGEX = "\\s+";

//    public static Command parseCommand(final String commandStringFormat) throws InvalidCommandSyntaxException {
////        String parsedCommand = isCommand(commandStringFormat);
////
////        String[] splitParsedCommand = parsedCommand.split(COMMAND_SPLIT_REGEX, COMMAND_SPLIT_LIMIT);
//
//
//
//    }

    private static final char COMMAND_PREFIX = '/';
    private static final int LIMIT_TO_SPLIT = 2;
    private static final int MIN_INITIAL_ARGS_COUNT = 2;
    private static final String ARGUMENTS_SPLIT_REGEX = ",";
    private static final String COMMAND_SPLIT_REGEX = "\\s+";
    private static final String ARGUMENT_SPLIT_REGEX = "=";
    private static final Gson gsonParser = new Gson();

    public static <T> T fromJson(final String commandStringFormat, final Class<T> tClass) {
        return gsonParser.fromJson(commandStringFormat, tClass);
    }

    public static <T> String toJson(final T toConvert) {
        return gsonParser.toJson(toConvert);
    }

    public static Command parseCommand(String username, String clientRequest) throws InvalidCommandSyntaxException {

        if (clientRequest == null || clientRequest.isEmpty() || clientRequest.isBlank()) {
            throw new InvalidCommandSyntaxException("Client request has invalid syntax");
        }

        String[] requestMainArgs = clientRequest.split(COMMAND_SPLIT_REGEX, LIMIT_TO_SPLIT);
        String[] requestSecondaryArgs = requestMainArgs.length < MIN_INITIAL_ARGS_COUNT ?
                null : requestMainArgs[1].split(ARGUMENTS_SPLIT_REGEX);

        requestMainArgs = ArgumentParser.trimAll(requestMainArgs);

        requestMainArgs[0] = isCommand(requestMainArgs[0]);

        if (requestSecondaryArgs != null) {

            requestSecondaryArgs = ArgumentParser.trimAll(requestSecondaryArgs);

            for (String argumentPair : requestSecondaryArgs) {

                if (!argumentPair.contains(ARGUMENT_SPLIT_REGEX)) {
                    throw new InvalidCommandSyntaxException(String
                            .format("Command has incorrect format: %s", argumentPair));
                }
            }

            requestSecondaryArgs = Arrays.stream(requestSecondaryArgs)
                    .map(argPair ->
                            Arrays.stream(argPair.split(ARGUMENT_SPLIT_REGEX))
                                    .map(String::trim)
                                    .collect(Collectors.joining(ARGUMENT_SPLIT_REGEX)))
                    .toArray(String[]::new);
        }

        return new Command(username,
                CommandType.getCommandType(requestMainArgs[0]), Arrays.stream(requestSecondaryArgs).toList());
    }

    private static String isCommand(final String commandStringFormat) throws InvalidCommandSyntaxException {
        if (commandStringFormat.charAt(0) == COMMAND_PREFIX) {
            return commandStringFormat.substring(1);
        }

        throw new InvalidCommandSyntaxException();
    }
}
