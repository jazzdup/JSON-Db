package server;

import java.io.FileNotFoundException;

public class Main {



    public static void main(String[] args) {
        DatabaseManager manager = null;
        try {
            manager = new DatabaseManager();
            manager.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
