package server;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Request {
    @Parameter(names = "-t", description = "Type of request")
    private String type;

    @Parameter(names = "-k", description = "Key")
    private JsonElement key;

    @Parameter(names = "-v", description = "Value")
    private JsonElement value;

    @Parameter(names = "-in", description = "read 1 request from filename in default path")
    private String filename;

    public String getType() {
        return type;
    }

    public JsonElement getKey() {
        return key;
    }

    public JsonElement getValue() {
        return value;
    }

    public String getFilename() {
        return filename;
    }

    public static Request parse(String line) {
        return new Gson().fromJson(line, Request.class);
    }

    @Override
    public String toString() {
        if ("exit".equals(type)) {
            return type;
        } else {
            return String.format("%s %s %s", type, key, value);
        }
    }

    public String toJson(String path) {
        String s = null;
        if (filename == null){
            s = new Gson().toJson(this);
        } else {
            File file = new File(path + filename);
            try{
                s = Files.readString(file.toPath());
            } catch (IOException e) {
                System.out.println("Failed to read file: " + file.getAbsolutePath());
                e.printStackTrace();
                System.exit(1);
            }
        }
        return s;
    }
}
