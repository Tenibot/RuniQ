package fmi.codes.project.newserver;

import fmi.codes.project.argument.ArgumentParser;
import fmi.codes.project.argument.ArgumentType;
import fmi.codes.project.command.Command;
import fmi.codes.project.exception.*;
import fmi.codes.project.response.ResponseStatus;
import fmi.codes.project.response.ServerResponseBuilder;

import java.util.List;

public class DistributorCommandExecutor {

    private final DistributorServerStorage distributorServerStorage;

    public DistributorCommandExecutor(final DistributorServerStorage distributorServerStorage) {
        this.distributorServerStorage = distributorServerStorage;
    }

    public ServerResponseBuilder execute(final Command toExecute)
            throws NullCommandException, InaccessibleCommandException, NoSuchArgumentException, NoSuchUserException,
            UserAlreadyLoggedInException, InvalidPasswordException, InvalidUsernameException, WrongPasswordException,
            WeakPasswordException, UsernameAlreadyUsedException, MapAlreadyExistsException {

        if (toExecute == null) {
            throw new NullCommandException();
        }

        ServerResponseBuilder serverResponseBuilder = null;

        if (toExecute.username() == null) {
            serverResponseBuilder = switch (toExecute.commandType()) {
                case LOGIN -> loginUser(toExecute.arguments());
                case REGISTER -> registerUser(toExecute.arguments());
                default -> throw new InaccessibleCommandException();
            };
        } else {
            serverResponseBuilder = switch (toExecute.commandType()) {
                case CREATE_MAP -> createMap(toExecute.username(), toExecute.arguments());
                case JOIN_SESSION -> null;
                case START_SESSION -> null;
                default -> throw new InaccessibleCommandException();
            };
        }

        return serverResponseBuilder.setResponseStatus(ResponseStatus.OK);
    }

    private ServerResponseBuilder loginUser(final List<String> arguments)
            throws NoSuchArgumentException, NoSuchUserException, UserAlreadyLoggedInException,
            InvalidPasswordException, InvalidUsernameException, WrongPasswordException {
        String username = ArgumentParser.parseStringValue(arguments, ArgumentType.USERNAME.getArgumentName(), false);
        String password = ArgumentParser.parseStringValue(arguments, ArgumentType.PASSWORD.getArgumentName(), false);

        return distributorServerStorage.loginUser(username, password);
    }

    private ServerResponseBuilder registerUser(final List<String> arguments)
            throws NoSuchArgumentException, InvalidUsernameException, WeakPasswordException,
            UsernameAlreadyUsedException {
        String username = ArgumentParser.parseStringValue(arguments, ArgumentType.USERNAME.getArgumentName(), false);
        String password = ArgumentParser.parseStringValue(arguments, ArgumentType.PASSWORD.getArgumentName(), false);

        return distributorServerStorage.registerUser(username, password);
    }

    private ServerResponseBuilder createMap(final String username, final List<String> arguments)
            throws NoSuchArgumentException, MapAlreadyExistsException {

        String quizStringFormat = ArgumentParser.parseStringValue(arguments, ArgumentType.QUIZ.getArgumentName(), false);

        return distributorServerStorage.createMap(username, quizStringFormat);
    }

    //start and join session require other classes
}
