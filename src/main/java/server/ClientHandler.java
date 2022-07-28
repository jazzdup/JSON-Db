package server;

import com.google.gson.JsonElement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Database database;
    private Socket socket;
    public ClientHandler(Database database, Socket accept) {
        this.database = database;
        this.socket = accept;
    }

    /**
     *
     * @return true if it should stop
     */
    boolean start() {
        try(DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());) {
            String request = input.readUTF();
            System.out.printf("Received: %s\n", request);
            Command command = null;
            try {
                command = Command.parse(database, request);
                JsonElement value = command.execute();
                output.writeUTF(Response.OK(value));
            } catch (DbException e) {
                output.writeUTF(Response.getError(e.getMessage()));
            } catch (ExitException e) {
                output.writeUTF(Response.OK(null));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
