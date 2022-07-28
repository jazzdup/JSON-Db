package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static final int PORT = 60065;
    private static final String ADDRESS = "127.0.0.1";
    private final Params argObject;

    public Client(Params args) {
        this.argObject = args;
    }

    public void sendMessage() throws IOException {
        Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
        System.out.println("Client started!");
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output  = new DataOutputStream(socket.getOutputStream());

        // for reading client requests from file
        String path = System.getProperty("user.dir") + "/src/client/data/";// + fileName;
        String request = argObject.toJson(path);

        output.writeUTF(request);
        System.out.printf("Sent: %s\n", request);
        System.out.printf("Received: %s\n", input.readUTF());
    }
}
