package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import exceptions.BadRequestException;
import exceptions.FoodNotFoundException;
import server.cache.LRUCacheFood;
import server.dto.Food;
import server.dto.FoodList;
import server.dto.FoodReport;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FoodAnalyzerServer implements AutoCloseable {

    private static final int SERVER_PORT = 7777;
    private static final int BUFFER_SIZE = 64 * 1024;
    private static final int DEFAULT_CACHE_SIZE = 1024;
    private static final String SERVER_HOST = "localhost";
    private static final String CODE_ARGUMENT = "--code";
    private static final String IMAGE_ARGUMENT = "--image";
    private static boolean isStarted = true;

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final ByteBuffer messageBuffer;
    private final FoodHttpClient foodHttpClient;
    private final Map<Integer, Food> cache;
    private final LRUCacheFood lruCacheFood;


    private final static Logger LOGGER = Logger.getLogger(FoodAnalyzerServer.class.getName());

    public static Logger getLogger() {
        return LOGGER;
    }

    public FoodAnalyzerServer() throws IOException {
        messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        selector = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

        foodHttpClient = new FoodHttpClient(HttpClient.newHttpClient());

        cache = new ConcurrentHashMap<>();
        lruCacheFood = new LRUCacheFood(DEFAULT_CACHE_SIZE);
        setupLogger();
    }

    public FoodAnalyzerServer(int port) throws IOException {
        messageBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        selector = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, port));

        foodHttpClient = new FoodHttpClient(HttpClient.newHttpClient());

        cache = new ConcurrentHashMap<>();
        lruCacheFood = new LRUCacheFood(DEFAULT_CACHE_SIZE);
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
            saveFoodsToFile();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "IOException occurred: ", e);
        }
    }

    void handleKeyIsReadable(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        try {
            messageBuffer.clear();
            int r = socketChannel.read(messageBuffer);
            if (r == -1) {
                return;
            }
            messageBuffer.flip();
            String message = new String(messageBuffer.array(), 0, messageBuffer.limit()).trim();
            String resultFromCommand;
            try {
                resultFromCommand = handleCommand(message);
            } catch (Exception e) {
                resultFromCommand = e.getMessage();
            }
            LOGGER.log(Level.INFO, "Message from client: " + message);
            LOGGER.log(Level.INFO, "Response from server: " + resultFromCommand);

            String response = resultFromCommand + System.lineSeparator();
            messageBuffer.clear();
            messageBuffer.put(response.getBytes());
            messageBuffer.flip();
            socketChannel.write(messageBuffer);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "IOException: " + e.getMessage());
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

    private String handleCommand(String commandMessage) throws Exception {
        if (commandMessage == null) {
            return null;
        }
        String[] commandParts = commandMessage.split("\\s");
        if (commandParts.length == 1) {
            return "Invalid command. ";
        }

        String command = commandParts[0].trim();
        if (command.equalsIgnoreCase("get-food-report")) {
            FoodReport result = foodHttpClient.getFoodReport(commandParts[1]);
            return result != null ? result.toString() : "Such a food could not be found";
        }
        if (command.equalsIgnoreCase("get-food")) {
            String searchString = commandMessage.substring(command.length() + 1);
            return getFood(searchString);
        }
        if (command.equalsIgnoreCase("get-food-by-barcode")) {
            if (commandParts[1].contains(CODE_ARGUMENT)) {
                String upc = commandParts[1].substring(CODE_ARGUMENT.length() + 1);
                return getFoodByUpcCode(upc);
            }
            if (commandParts[1].contains(IMAGE_ARGUMENT)) {
                String imagePath = commandParts[1].substring(IMAGE_ARGUMENT.length() + 1);
                return getFoodByImage(imagePath);
            }
            return "No GTIN/UPC was provided. ";
        }

        return "Unknown command!";
    }

    private String getFood(String seachedString) throws FoodNotFoundException, BadRequestException {
        FoodList result = lruCacheFood.getByKeywords(seachedString);
        if (result == null) {
            result = foodHttpClient.getFoodsBySearch(seachedString);
        }
        if (result != null) {
            cacheFoodList(result);
            return result.toString();
        }

        return "Such a food could not be found.";
    }

    private String getFoodByImage(String path) {
        if (!new File(path).exists()) {
            return "Image could not be opened. ";
        }
        String upc = extractUPCFromImage(path);
        if (upc == null) {
            return "Food with that barcode could not be found in cache. ";
        }

        return getFoodByUpcCode(upc);
    }

    private String extractUPCFromImage(String path) {
        File imgFile = new File(path);
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(imgFile);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Error while opening image: " + e);
            return null;
        }
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            LOGGER.log(Level.INFO, "No barcode was found. ");
            return null;
        }
    }

    private String getFoodByUpcCode(String upcCode) {
        return lruCacheFood.getByUpcCode(upcCode).toString();
    }

    private void cacheFood(Food food) {
        if (!cache.containsKey(food.getFdcId())) {
            cache.put(food.getFdcId(), food);
        }
        lruCacheFood.set(food);
    }

    private void cacheFoodList(FoodList foodList) {
        foodList.getFoods().forEach(this::cacheFood);
    }

    private void saveFoodsToFile() {
        Gson gson = new Gson();
        try (FileOutputStream foodFileStream = new FileOutputStream("resources/cacheFood.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(foodFileStream))
        ) {
            gson.toJson(cache.values(), bufferedWriter);
            bufferedWriter.flush();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "IO exception occured: " + e.getMessage());
        }
    }

    private void loadFoodFromFile() {
        Gson gson = new Gson();
        try (InputStream inputStream = new FileInputStream("resources/cacheFood.txt");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String fileContent = bufferedReader.readLine();
            if (fileContent == null) {
                return;
            }
            LOGGER.log(Level.INFO, "File content: " + fileContent);
            List<Food> foodsInFile = gson.fromJson(fileContent, new TypeToken<List<Food>>() {
            }.getType());
            foodsInFile.forEach(this::cacheFood);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "IO exception occured: " + e.getMessage());
        }
    }

    private void setupLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("resources/log.txt", true);
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandler);
        LOGGER.setUseParentHandlers(false);
    }
}
