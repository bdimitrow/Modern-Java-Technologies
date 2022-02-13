package server;

import java.util.Scanner;

public class ServerThread implements Runnable {

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String consoleInput;

        while ((consoleInput = scanner.nextLine()) != null) {
            if (consoleInput.equalsIgnoreCase("disconnect")) {
                FoodAnalyzerServer.stopServer();
                break;
            }
        }
        scanner.close();
    }
}
