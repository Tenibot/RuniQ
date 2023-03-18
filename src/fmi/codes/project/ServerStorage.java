package fmi.codes.project;

import fmi.codes.project.command.CommandParser;
import fmi.codes.project.exception.*;
import fmi.codes.project.path.PathBuilder;
import fmi.codes.project.response.ServerResponseBuilder;
import fmi.codes.project.quiz.Task;
import fmi.codes.project.user.UserInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerStorage {

    private final Set<String> loggedInUsers;
    private final Map<String, UserInfo> registeredUsers; //username, userInfo
    private final Map<Integer, Map<AtomicInteger, String>> liveLeaderboard; //quizId, points, teamName
    private final Map<Integer, ConcurrentHashMap<String, CopyOnWriteArrayList<Task>>> tasks; //quizId, teamName, tasks
    private final Map<Integer, ConcurrentHashMap<String, AtomicInteger>> tasksDone; //quizId, teamName, taskNum

    private static final String USERS = "users";
    private static final String USER_CREDENTIALS_SPLITTER = " ";
    private static final int MIN_PASSWORD_LENGTH = 8;

    public ServerStorage() {
        setUpStorage();

        this.loggedInUsers = new CopyOnWriteArraySet<>();
        this.registeredUsers = loadRegisteredUsers();
        this.liveLeaderboard = new ConcurrentHashMap<>();
        this.tasks = new ConcurrentHashMap<>();
        this.tasksDone = new ConcurrentHashMap<>();
    }

    public ServerResponseBuilder loginUser(String username, String password)
            throws InvalidUsernameException, InvalidPasswordException, NoSuchUserException, WrongPasswordException, UserAlreadyLoggedInException {

        if (!UserInfo.isUsernameValid(username)) {
            throw new InvalidUsernameException();
        }

        if (!isPasswordStrong(password)) {
            throw new InvalidPasswordException();
        }

        if (!registeredUsers.containsKey(username)) {
            throw new NoSuchUserException();
        }

        if (!registeredUsers.get(username).isEnteredPasswordCorrect(password)) {
            throw new WrongPasswordException();
        }

        if (loggedInUsers.contains(username)) {
            throw new UserAlreadyLoggedInException();
        }

        loggedInUsers.add(username);

        return new ServerResponseBuilder().setMessage("Successfully logged in user").setClientUsername(username);
    }

    public ServerResponseBuilder registerUser(final String username, final String password)
            throws UsernameAlreadyUsedException, WeakPasswordException, InvalidUsernameException {

       if (registeredUsers.containsKey(username)) {
            throw new UsernameAlreadyUsedException();
       }

       if (!UserInfo.isUsernameValid(username)) {
           throw new InvalidUsernameException();
       }

       if (!isPasswordStrong(password)) {
           throw new WeakPasswordException();
       }

       UserInfo toSave = new UserInfo(username, password);

       registeredUsers.put(username, toSave);

       writeToFile(new PathBuilder().addDirectory(USERS).buildFilePath(), CommandParser.toJson(toSave), StandardOpenOption.APPEND);

       return new ServerResponseBuilder().setMessage("Successfully registered user");
    }

    public ServerResponseBuilder finishTask(final Integer quizId, final String taskName, final String teamName, final Integer oldPoints, final Integer pointDiff) {
        tasksDone.get(quizId).get(taskName).incrementAndGet();
        liveLeaderboard.get(quizId).remove(oldPoints, teamName);

        return new ServerResponseBuilder().setMessage("Successfully updated score");
    }

    public ServerResponseBuilder updateLeaderboard(final Integer quizId) {
        if (!liveLeaderboard.containsKey(quizId)) {
            TreeMap<AtomicInteger, String> toAdd = new TreeMap<>();
            liveLeaderboard.put(quizId, Collections.synchronizedMap(toAdd));
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<AtomicInteger, String> entry : liveLeaderboard.get(quizId).entrySet()) {
            stringBuilder.append(entry.getKey()).append(" ").append(entry.getValue()).append(System.lineSeparator());
        }

        return new ServerResponseBuilder().setMessage(stringBuilder.toString());
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setUpStorage() {
        Path usersFilePath = new PathBuilder().addDirectory(USERS).buildFilePath();

        if (!doesFileOrDirExist(usersFilePath)) {
            createDirectories(usersFilePath.getParent());
            createFile(usersFilePath);
        }
    }

    private Map<String, UserInfo> loadRegisteredUsers() {
        Path usersFilePath = new PathBuilder().addDirectory(USERS).buildFilePath();

        try {
            List<String> allLines = Files.lines(usersFilePath).toList();
            Map<String, UserInfo> toReturn = new ConcurrentHashMap<>();

            for(String line : allLines) {
                UserInfo toAdd = CommandParser.fromJson(line, UserInfo.class);
                toReturn.put(toAdd.getUsername(), toAdd);
            }

            return toReturn;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean doesFileOrDirExist(Path directoryPath) {
        return Files.exists(directoryPath);
    }

    private void createDirectories(Path directoryPath) {
        try {
            Files.createDirectories(directoryPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createFile(Path filePath) {
        try {
            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(Path pathToFile, String stringToWrite, StandardOpenOption... openOptions) {
        try {
            Files.writeString(pathToFile,
                    stringToWrite + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    openOptions);
        } catch (IOException e) {
            throw new RuntimeException(String
                    .format(IOUnsuccessfulOperationException.DEFAULT_MESSAGE_FORMAT, e.getMessage()), e);
        }
    }

    private boolean isPasswordStrong(final String password) {
        return password != null && !password.isEmpty() && !password.isBlank() && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
