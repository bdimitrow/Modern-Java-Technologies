package server;

import exceptions.BadRequestException;
import exceptions.FoodNotFoundException;
import server.dto.Food;
import server.dto.FoodList;
import server.dto.FoodReport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FoodAnalyzerServer implements AutoCloseable{

    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_HOST = "localhost";
    private static final String API_KEY = "We5UALb9buICMpssP0NRPDneFLC9pAhctVG07lPv";
    private static final String API_URL = "https://api.nal.usda.gov/fdc/v1/";
    private static boolean isStarted = true;

    private final BufferedWriter writer;
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final ByteBuffer messageBuffer;
    private final FoodHttpClient foodHttpClient;
    private Map<Integer, Food> cache;
    private boolean isCacheUpdated = false;


    private final static Logger logger = Logger.getLogger(FoodAnalyzerServer.class.getName());

    public FoodAnalyzerServer() throws IOException {
        messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        selector = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

        foodHttpClient = new FoodHttpClient(HttpClient.newHttpClient());

        writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                new File("resources/logFile.txt"), true)));

        cache = new ConcurrentHashMap<>();
    }

    public FoodAnalyzerServer(int port) throws IOException {
        messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        selector = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, port));

        foodHttpClient = new FoodHttpClient(HttpClient.newHttpClient());

        writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                new File("resources/logFile.txt"), true)));

        cache = new ConcurrentHashMap<>();
        setupLogger();
    }

    public static void main(String[] args) {
        try {
            new FoodAnalyzerServer(SERVER_PORT).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ServerThread serverThread = new ServerThread();
            Thread serverReaderThread = new Thread(serverThread);
            serverReaderThread.setDaemon(true);
            serverReaderThread.start();
            while (isStarted) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext() && isStarted) {
                    SelectionKey selectionKey = keyIterator.next();
                    if (selectionKey.isReadable()) {
                        handleKeyIsReadable(selectionKey);
                    } else if (selectionKey.isAcceptable()) {
                        handleKeyIsAcceptable(selectionKey);
                    }
                    keyIterator.remove();
                }
            }
            //@TODO server.cache??
        } catch (IOException e) {
            logger.log(Level.INFO,"IOException occurred: ", e);
        }
    }

    void handleKeyIsReadable(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        try {
            messageBuffer.clear();
            int r = socketChannel.read(messageBuffer);
            if (r == -1) {
                logger.log(Level.INFO,"Nothing to read. ");
                stopServer();
                return;
            }
            messageBuffer.flip();
            String message = new String(messageBuffer.array(), 0, messageBuffer.limit()).trim();
            String resultFromCommand = handleCommand(message);

            logger.log(Level.INFO, "Message: " + message);
            logger.log(Level.INFO, "Result: " + resultFromCommand);

            byte[] response = (resultFromCommand + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
            messageBuffer.clear();
            messageBuffer.put(response);
            messageBuffer.flip();
            socketChannel.write(messageBuffer);
        } catch (IOException e) {
            stopServer();
        } catch (FoodNotFoundException | BadRequestException e) {
            e.printStackTrace();
        }
    }

    void handleKeyIsAcceptable(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel accept = sockChannel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    public static void stopServer() {
        isStarted = false;
    }

    @Override
    public void close() throws Exception {
        serverSocketChannel.close();
        selector.close();
    }

    private String handleCommand(String commandMessage) throws FoodNotFoundException, BadRequestException {
        if (commandMessage == null) {
            return null;
        }
        String[] commandParts = commandMessage.split("\\s");
        if (commandParts.length == 1) {
            return "Invalid command!";
        }

        String command = commandParts[0].trim();
        if (command.equalsIgnoreCase("get-food-report")) {
            FoodReport result = foodHttpClient.getFoodReport(commandParts[1]);
            return result.toString();
        }
        if (command.equalsIgnoreCase("get-food")) {
            return foodHttpClient.getFoodsBySearch(commandMessage.substring(command.length() + 1)).toString();
        }
        if (command.equalsIgnoreCase("get-food-by-barcode")) {
            String argumentName = "--code";
            if (commandParts[1].contains(argumentName)) {
                String upc = commandParts[1].substring(argumentName.length() + 1);
//                return displayFoodByUPC(upc);
            }
            String imagePath = commandParts[1].substring(argumentName.length() + 1);
//            return displayFoodByImage(imagePath);
        }

        return "Unknown command!";
    }

    private void cacheFood(Food food) {
        cache.put(food.getFdcId(), food);
    }

    private void cacheFoodList(FoodList foodList) {
        foodList.getFoods().forEach(this::cacheFood);
    }

    private void setupLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("resources/log.txt");
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false);
    }
}
