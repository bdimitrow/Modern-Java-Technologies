package bg.sofia.uni.fmi.mjt.cocktail.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.storage.DefaultCocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class CocktailServer {

    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_HOST = "localhost";
    private static final Random RANDOM = new Random();

    private final int port;
    private final DefaultCocktailStorage defaultCocktailStorage;
    private final ByteBuffer messageBuffer;

    private boolean isStarted = true;

    public CocktailServer(int port) {
        this.port = port;
        this.defaultCocktailStorage = new DefaultCocktailStorage();
        this.messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    public static void main(String[] args) {
        new CocktailServer(SERVER_PORT).start();
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, port));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (isStarted) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        messageBuffer.clear();
                        int r = socketChannel.read(messageBuffer);
                        if (r <= 0) {
                            System.out.println("Nothing to read, closing channel");
                            socketChannel.close();
                            continue;
                        }

                        handleKeyIsReadable(key, messageBuffer);
                    } else if (key.isAcceptable()) {
                        handleKeyIsAcceptable(selector, key);
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.err.println("There is a problem with the server socket: " + e.getMessage());
            System.err.println(e);
        } catch (CocktailNotFoundException | CocktailAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("Server stopped");
    }

    public void stop() {
        isStarted = false;
    }

    private void handleKeyIsReadable(SelectionKey key, ByteBuffer buffer)
            throws IOException, CocktailNotFoundException, CocktailAlreadyExistsException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        buffer.flip();
        String message = new String(buffer.array(), 0, buffer.limit()).trim();

        System.out.println("Message [" + message + "] received from client " + socketChannel.getRemoteAddress());

        String command = message.split(" ")[0];
        String arguments = message.substring(message.indexOf(" ") + 1);

        String response = null;
        switch (command) {
            case "create":
                response = createCocktail(arguments);
                break;
            case "get":
                response = getCocktail(arguments);
                break;
            case "disconnect":
                disconnect(key);
                break;
            default:
                response = "{ Unknown command }";
        }

        if (response != null) {
            System.out.println("Sending response to client: " + response);
            response += System.lineSeparator();
            buffer.clear();
            buffer.put(response.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
        }
    }

    private void handleKeyIsAcceptable(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);

        System.out.println("Connection accepted from client " + accept.getRemoteAddress());
    }

    private String createCocktail(String arguments) {
        String[] argumentsArray = arguments.split("\\s+");
        String cocktailName = argumentsArray[0];
        Set<Ingredient> ingredientSet = new HashSet<>();
        for (int i = 1; i < argumentsArray.length; ++i) {
            String[] currentIngredient = argumentsArray[i].split("=");
            ingredientSet.add(new Ingredient(currentIngredient[0], currentIngredient[1]));
        }

        Cocktail toSubmit = new Cocktail(cocktailName, ingredientSet);
        try {
            defaultCocktailStorage.createCocktail(toSubmit);
            return "{ Cocktail " + cocktailName + " created successfully }";
        } catch (CocktailAlreadyExistsException e) {
            return "{ Cocktail " + cocktailName + " already exists }";
        }
    }

    private String getCocktail(String arguments) throws CocktailNotFoundException {
        if (defaultCocktailStorage.getCocktails().isEmpty()) {
            return "{ Cocktail storage is empty! }";
        }
        String[] argumentsArray = arguments.split("\\s+");
        StringBuilder result = new StringBuilder();

        switch (argumentsArray[0]) {
            case "all":
                Collection<Cocktail> cocktails = defaultCocktailStorage.getCocktails();
                for (var current : cocktails) {
                    result.append(current.toString());
                }
                return result.toString();
            case "by-name":
                return "[" + defaultCocktailStorage.getCocktail(argumentsArray[1]).toString() + "]";
            case "by-ingredient":
                var cocktailsWithIngredient = defaultCocktailStorage.getCocktailsWithIngredient(argumentsArray[1]);
                for (var current : cocktailsWithIngredient) {
                    result.append(current.toString());
                }
                return result.toString();
        }
        return "Incorrect command!";
    }

    private void disconnect(SelectionKey key) throws IOException {
        key.channel().close();
        key.cancel();
    }

}