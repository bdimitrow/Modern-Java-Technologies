package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FoodAnalyzerClient {
    private final static String SERVER_HOST = "localhost";
    private final static int SERVER_PORT = 7777;
    private String host;
    private int port;

    public FoodAnalyzerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        new FoodAnalyzerClient(SERVER_HOST, SERVER_PORT).start();
    }

    public void start() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(this.host, this.port));

            System.out.println("Connected to the server.");

            String inputString;
            while ((inputString = scanner.nextLine()) != null) {
                if ("disconnect".equalsIgnoreCase(inputString)) {
                    break;
                }
                System.out.println("Sent message: " + inputString);
                writer.println(inputString);
                writer.flush();

                String reply = reader.readLine();
                System.out.println("Server reply: " + reply);
            }

            System.out.println("Disconnected from the server! ");
        } catch (IOException e) {
            System.err.println("An error occurred in the client I/O: " + e.getMessage());
        }
    }

}

