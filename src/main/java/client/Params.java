package client;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Params {
    @Parameter(names = "-t", description = "Type of request")
    private String type;

    @Parameter(names = "-k", description = "Key")
    private String key;

    @Parameter(names = "-v", description = "Value")
    private String value;

    @Parameter(names = "-in", description = "read 1 request from filename in default path")
    private String filename;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFilename() {
        return filename;
    }

    public static Params parse(String line) {
        return new Gson().fromJson(line, Params.class);
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
