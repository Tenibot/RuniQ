package fmi.codes.project.argument;

import fmi.codes.project.exception.NoSuchArgumentException;

import java.util.Arrays;
import java.util.List;

public class ArgumentParser {
    private static final String ARGUMENT_SPLIT_REGEX = "=";
    private static final int FIRST_ARGUMENT = 0;
    private static final int SECOND_ARGUMENT = 1;

    public static String parseStringValue(List<String> arguments, String argumentName, boolean isOptional)
            throws NoSuchArgumentException {

        if (arguments == null) {
            if (!isOptional) {
                throw new NoSuchArgumentException(String
                        .format(NoSuchArgumentException.DEFAULT_MESSAGE_FORMAT, argumentName));
            }

            return null;
        }

        for (String argument : arguments) {
            String[] splitArgument = argument.split(ARGUMENT_SPLIT_REGEX);

            if (splitArgument[FIRST_ARGUMENT].equals(argumentName)) {
                return splitArgument[SECOND_ARGUMENT];
            }
        }

        if (!isOptional) {
            throw new NoSuchArgumentException(String
                    .format(NoSuchArgumentException.DEFAULT_MESSAGE_FORMAT, argumentName));
        }

        return null;
    }

    public static String[] trimAll(String[] toTrim) {
        return Arrays
                .stream(toTrim)
                .map(String::trim)
                .toArray(String[]::new);
    }
}
