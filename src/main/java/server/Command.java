package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Command {
    protected static Database database;
    protected List<String> key;

    static Command parse(Database database, String line) throws DbException {
        if (database != null){
            Command.database = database;
        }
        Request request = Request.parse(line);
        List<String> keyList = getKeyList(request.getKey());
        switch (request.getType()) {
            case "set":
                return new SetCommand(keyList, request.getValue());
            case "delete":
                return new DeleteCommand(keyList);
            case "get":
                return new GetCommand(keyList);
            case "exit":
                return new ExitCommand("OK");
        }
        return null;
    }

    private static List<String> getKeyList(JsonElement key) {
        List<String> keysList = new ArrayList<>();
        if (Objects.isNull(key)){
            return keysList;
        } else if (key.isJsonArray()) {
            JsonArray jsonArray = key.getAsJsonArray();
            jsonArray.forEach(element -> keysList.add(element.getAsString()));
        } else {
            keysList.add(key.getAsString());
        }
        return keysList;
    }
    //    private static String getText(String input) {
//        return input.split(" ", 2)[1];
//    }

    public abstract JsonElement execute() throws DbException, ExitException;
}

class GetCommand extends Command {
    public GetCommand(List<String> index) {
        this.key = index;
    }
    public JsonElement execute() throws DbException {
        return database.get(key);
    }
}
class DeleteCommand extends Command {
    public DeleteCommand(List<String> index) {
        this.key = index;
    }
    public JsonElement execute() throws DbException {
        return database.delete(key);
    }
}
class SetCommand extends Command {
    protected JsonElement text;
    public SetCommand(List<String> index, JsonElement text) {
        this.key = index;
        this.text = text;
    }
    public JsonElement execute() throws DbException {
        return database.set(key, text);
    }
}
class ExitCommand extends Command {
    protected String message;
    public ExitCommand(String message) {
        this.message = message;
    }

    public JsonElement execute() throws ExitException {
        throw new ExitException(message);
    }
}