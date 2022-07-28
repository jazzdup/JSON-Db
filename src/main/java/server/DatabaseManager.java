package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseManager {
    private Database database;
    private static final int PORT = 60065;
    private static final String ADDRESS = "127.0.0.1";
    private final ExecutorService executorService;
    public DatabaseManager() throws FileNotFoundException {
        String fileName = System.getProperty("user.dir") + "/src/server/data/db.json"; // for your db server file
        Database database = new Database(fileName);
        this.database = database;
        executorService = Executors.newFixedThreadPool(8);
    }

    void start(){
        try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS));){
            System.out.println("Server started!");
            while (true) {
                ClientHandler handler = new ClientHandler(database, server.accept());
                Future<Boolean> stop = executorService.submit(() -> {
                    return handler.start();
                });
                if (stop.get()) {
                    System.out.println("Shutting down...");
                    break;
                }
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            System.out.println("Server failed");
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
