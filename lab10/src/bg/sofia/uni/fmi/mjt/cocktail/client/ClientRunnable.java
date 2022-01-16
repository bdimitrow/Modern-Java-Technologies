package bg.sofia.uni.fmi.mjt.cocktail.client;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientRunnable implements Runnable {

    private final BufferedReader reader;

    public ClientRunnable(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        String line;
        while (true) {
            try {
                line = reader.readLine();
                if (line != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println("Connection is closed, stop waiting for server messages due to: " + e.getMessage());
                System.err.println(e);
                break;
            }
        }
    }

}