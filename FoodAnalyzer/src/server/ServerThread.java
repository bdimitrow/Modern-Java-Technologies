package server;

import java.util.Scanner;
import java.util.logging.Level;

public class ServerThread implements Runnable {

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String consoleInput;

        while ((consoleInput = scanner.nextLine()) != null) {
            if (consoleInput.equalsIgnoreCase("disconnect")) {
                FoodAnalyzerServer.getLogger().log(Level.INFO, "Disconnect!");
                FoodAnalyzerServer.stopServer();
                break;
            }
        }
        scanner.close();
    }
}
