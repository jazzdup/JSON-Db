package client;

import com.beust.jcommander.JCommander;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try{
            Params argObject = parseCommand(args);
            Client client = new Client(argObject);
            client.sendMessage();
//            String command = Arrays.stream(args).reduce("", (a, b) -> a + " " + b);

        } catch (IOException e) {
            System.out.println("Client failed");
            e.printStackTrace();
        }
    }

    private static Params parseCommand(String[] args) {
        Params argObject = new Params();
        JCommander.newBuilder().addObject(argObject).build().parse(args);
        return argObject;
    }
}
