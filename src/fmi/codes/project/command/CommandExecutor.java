package fmi.codes.project.command;

import fmi.codes.project.ServerStorage;
import fmi.codes.project.argument.ArgumentParser;
import fmi.codes.project.argument.ArgumentType;
import fmi.codes.project.exception.*;
import fmi.codes.project.quiz.Quiz;
import fmi.codes.project.response.ServerResponseBuilder;
import fmi.codes.project.user.team.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CommandExecutor {
    private final ServerStorage serverStorage;
    private final Map<Integer, Quiz> activeQuizzes;
    private final Map<Integer, Map<String, Team>> quizTeams;

    public CommandExecutor(final ServerStorage serverStorage) {
        this.serverStorage = serverStorage;
        this.activeQuizzes = new HashMap<>();
        this.quizTeams = new HashMap<>();
    }

    public ServerResponseBuilder execute(final Command toExecute) throws Exception, NoSuchArgumentException,
            NoSuchUserException, UserAlreadyLoggedInException, InvalidPasswordException, InvalidUsernameException,
            WrongPasswordException, WeakPasswordException, UsernameAlreadyUsedException {

        if (toExecute == null) {
            throw new Exception();
        }

        if (toExecute.username() == null) {
            switch (toExecute.commandType()) {
                case LOGIN: return loginUser(toExecute.arguments());
                case REGISTER: return registerUser(toExecute.arguments());
            };
        } else {
            switch (toExecute.commandType()) {
                case CREATE_TEAM: return createTeam(toExecute.username(), toExecute.arguments());
            };
        }

        return new ServerResponseBuilder().setMessage("Why are we here");
    }

    private ServerResponseBuilder loginUser(final List<String> arguments)
            throws NoSuchArgumentException, NoSuchUserException, UserAlreadyLoggedInException,
            InvalidPasswordException, InvalidUsernameException, WrongPasswordException {
        String username = ArgumentParser.parseStringValue(arguments, ArgumentType.USERNAME.getArgumentName(), false);
        String password = ArgumentParser.parseStringValue(arguments, ArgumentType.PASSWORD.getArgumentName(), false);

        return serverStorage.loginUser(username, password);
    }

    private ServerResponseBuilder registerUser(final List<String> arguments)
            throws NoSuchArgumentException, InvalidUsernameException,
            WeakPasswordException, UsernameAlreadyUsedException {
        String username = ArgumentParser.parseStringValue(arguments, ArgumentType.USERNAME.getArgumentName(), false);
        String password = ArgumentParser.parseStringValue(arguments, ArgumentType.PASSWORD.getArgumentName(), false);

        return serverStorage.registerUser(username, password);
    }

    private ServerResponseBuilder createTeam(final String username, final List<String> arguments)
            throws NoSuchArgumentException {

        String teamStringFormat = ArgumentParser.parseStringValue(arguments, ArgumentType.TEAM.getArgumentName(), false);
        String quizIdStringFormat = ArgumentParser.parseStringValue(arguments, ArgumentType.QUIZ_ID.getArgumentName(), false);

        Team teamToAdd = CommandParser.fromJson(teamStringFormat, Team.class);
        Integer quizId = Integer.valueOf(quizIdStringFormat);

        //checks

        if (!quizTeams.containsKey(quizId)) {
            quizTeams.put(quizId, new HashMap<>());
        }

        quizTeams.get(quizId).put(teamToAdd.teamName(), teamToAdd);

        return new ServerResponseBuilder().setMessage("Successfully created team");
    }

    //private ServerResponseBuilder finishTask()

    private ServerResponseBuilder updateLeaderboard(final String username, final List<String> arguments)
            throws NoSuchArgumentException {
        String quizIdStringFormat = ArgumentParser.parseStringValue(arguments, ArgumentType.QUIZ_ID.getArgumentName(), false);

        Integer quizId = Integer.valueOf(quizIdStringFormat);

        return serverStorage.updateLeaderboard(quizId);
    }
}
