package fmi.codes.project.newserver;

import fmi.codes.project.command.CommandParser;
import fmi.codes.project.exception.*;
import fmi.codes.project.path.PathBuilder;
import fmi.codes.project.quiz.Quiz;
import fmi.codes.project.response.ServerResponseBuilder;
import fmi.codes.project.user.User;
import fmi.codes.project.user.UserInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class DistributorServerStorage {
    private final Set<String> loggedInUsers;
    private final Map<String, UserInfo> registeredUsers;
    private static final String USERS = "users";
    private static final String PRIVATE_MAPS = "private_maps";
    private static final int MIN_PASSWORD_LENGTH = 8;

    public DistributorServerStorage() {
        setUpStorage();

        this.loggedInUsers = new CopyOnWriteArraySet<>();
        this.registeredUsers = loadRegisteredUsers();
    }

    public ServerResponseBuilder loginUser(final String username, final String password)
        throws InvalidUsernameException, InvalidPasswordException, NoSuchUserException, WrongPasswordException,
        UserAlreadyLoggedInException {

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

        writeToFile(new PathBuilder().addDirectory(USERS).buildFilePath(), CommandParser.toJson(toSave),
            StandardOpenOption.APPEND);

        return new ServerResponseBuilder().setMessage("Successfully registered user");
    }

    public ServerResponseBuilder createMap(final String username, final String quizToAddStringFormat)
        throws MapAlreadyExistsException {
        Path userMapsFile = new PathBuilder().addDirectory(username).addDirectory(PRIVATE_MAPS).buildFilePath();

        if (!doesFileOrDirExist(userMapsFile)) {
            createDirectories(userMapsFile.getParent());
            createFile(userMapsFile);
        }

        if (doesFileContainLine(userMapsFile, quizToAddStringFormat)) {
            throw new MapAlreadyExistsException();
        }

        writeToFile(userMapsFile, quizToAddStringFormat, StandardOpenOption.APPEND);

        return new ServerResponseBuilder().setMessage("Successfully created map");
    }


    //////////////////////////////////////////////////////////////////////////////////////
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

            for (String line : allLines) {
                UserInfo toAdd = CommandParser.fromJson(line, UserInfo.class);
                toReturn.put(toAdd.getUsername(), toAdd);
            }

            return toReturn;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean doesFileContainLine(final Path pathToFile, final String toCheck) {
        for (String line : getFileContents(pathToFile)) {
            if (line.equals(toCheck)) {
                return true;
            }
        }

        return false;
    }

    private List<String> getFileContents(final Path pathToFile) {
        try {
            return Files.lines(pathToFile).toList();
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
            Files.writeString(pathToFile, stringToWrite + System.lineSeparator(), StandardCharsets.UTF_8, openOptions);
        } catch (IOException e) {
            throw new RuntimeException(
                String.format(IOUnsuccessfulOperationException.DEFAULT_MESSAGE_FORMAT, e.getMessage()), e);
        }
    }

    private boolean isPasswordStrong(final String password) {
        return password != null && !password.isEmpty() && !password.isBlank() &&
            password.length() >= MIN_PASSWORD_LENGTH;
    }
}
