package fmi.codes.project.newserver;

import fmi.codes.project.command.CommandParser;
import fmi.codes.project.command.CommandType;
import fmi.codes.project.exception.*;
import fmi.codes.project.response.ResponseStatus;
import fmi.codes.project.response.ServerResponseBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DistributorServer implements Runnable {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";

    private final String host;
    private final int port;
    private final ByteBuffer buffer;
    private boolean isServerWorking;
    private Selector selector;

    private final DistributorCommandExecutor distributorCommandExecutor;
    private final DistributorServerStorage distributorServerStorage;
    private final Map<SelectionKey, String> loggedInUsers;

    public DistributorServer(final String host, final int port) {
        this.host = host == null ? HOST : host;
        this.port = port;
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.distributorServerStorage = new DistributorServerStorage();
        this.distributorCommandExecutor = new DistributorCommandExecutor(distributorServerStorage);
        this.loggedInUsers = new HashMap<>();
    }

    public static void main(String[] args) {
        DistributorServer server = new DistributorServer("localhost", 16869);

        server.run();
    }

    @Override
    public void run() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);

            isServerWorking = true;

            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {

                        SelectionKey key = keyIterator.next();

                        if (key.isReadable()) {

                            SocketChannel clientChannel = (SocketChannel) key.channel();

                            if (!loggedInUsers.containsKey(key)) {
                                loggedInUsers.put(key, null);
                            }

                            String clientInput = getClientInput(clientChannel);

                            if (clientInput == null) {
                                continue;
                            }

                            ServerResponseBuilder serverResponseBuilder = null;

                            try {
                                serverResponseBuilder = distributorCommandExecutor
                                        .execute(CommandParser.parseCommand(loggedInUsers.get(key), clientInput))
                                        .setResponseStatus(ResponseStatus.OK);
                            } catch (Exception | InvalidCommandSyntaxException | NullCommandException |
                                     InaccessibleCommandException | NoSuchArgumentException | NoSuchUserException |
                                     UserAlreadyLoggedInException | InvalidPasswordException |
                                     InvalidUsernameException | WrongPasswordException | WeakPasswordException |
                                     UsernameAlreadyUsedException | MapAlreadyExistsException e) {
                                serverResponseBuilder = new ServerResponseBuilder()
                                        .setResponseStatus(ResponseStatus.ERROR)
                                        .setMessage(e.getMessage());
                            }

                            if (serverResponseBuilder.getCommandType() == CommandType.LOGIN &&
                                    serverResponseBuilder.getResponseStatus() == ResponseStatus.OK) {
                                loggedInUsers.put(key, serverResponseBuilder.getClientUsername());
                            }

                            serverResponseBuilder.setClientUsername(loggedInUsers.get(key));

                            writeClientOutput(clientChannel, CommandParser.toJson(serverResponseBuilder.build()));

                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(host, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}
