package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private JsonObject jsonObject;
    private final String filename;
    private final Lock readLock;
    private final Lock writeLock;
    public Database(String filename) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        this.filename = filename;
        System.out.println("Loading database...");
        File file = new File(filename);
        try{
            readLock.lock();
            if (file.exists()) {
                FileReader reader = new FileReader(filename);
                jsonObject = new Gson().fromJson(reader, JsonObject.class);
                reader.close();
            }else {
                System.out.println("Database file not found. Creating new one.");
                file.createNewFile();
//                jsonObject = new ConcurrentHashMap<>();
                jsonObject = new JsonObject();
            }
        } catch (IOException e) {
            System.out.println("Problem initialising db. Exiting.");
            e.printStackTrace();
            System.exit(1);
        } finally {
            readLock.unlock();
        }
    }

    JsonElement delete(List<String> keys) throws DbException {
//        String removed = jsonObject.remove(key);
//        if (removed == null) {
//            throw new DbException("No such key");
//        }
        String lastKey = keys.remove(keys.size() - 1);
        JsonObject current = jsonObject;
        for (String key : keys) {
            JsonElement next = current.getAsJsonObject().get(key);
            if (Objects.isNull(next)) {
                throw new DbException("No such key");
            }
            current = next.getAsJsonObject();
        }
        JsonElement removed = current.remove(lastKey);
        if (Objects.isNull(removed)) {
            throw new DbException("No such key");
        }
        save();
        return null;
    }

    private void save() throws DbException {
        System.out.println("Saving database");
        try (FileWriter writer = new FileWriter(filename);){
            writeLock.lock();
            new Gson().toJson(jsonObject, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new DbException("Failed to save database");
        } finally {
            writeLock.unlock();
        }
    }

    JsonElement set(List<String> key, JsonElement text) throws DbException {
//        jsonObject.put(key, text);
        String lastKey = key.remove(key.size() - 1);
        JsonObject current = jsonObject;
        for (String currentKey : key) {
            JsonElement next = current.getAsJsonObject().get(currentKey);
            if (!Objects.isNull(next)) {
                current = next.getAsJsonObject();
            } else {
                JsonObject newObject = new JsonObject();
                current.add(currentKey, newObject);
                current = newObject;
            }
        }
        current.add(lastKey, text);
        save();
        return null;
    }

    JsonElement get(List<String> keys) throws DbException {
//        String s = jsonObject.get(key);
//        if (s == null) {
//            throw new DbException("No such key");
//        } else {
//            return s;
//        }
        JsonElement current = jsonObject;
        for (String key : keys) {
            current = current.getAsJsonObject().get(key);
            if (Objects.isNull(current)) {
                throw new DbException("No such key");
            }
        }
        return current;
    }

}
