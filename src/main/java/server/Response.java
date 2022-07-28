package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Response {
    private String response;
    private JsonElement value;
    private String reason;

    public Response(String response) {
        this.response = response;
    }

    public static String getError(String message) {
        Response r = new Response("ERROR");
        r.reason = message;
        return new Gson().toJson(r);
    }

    public static String OK(JsonElement value) {
        Response r = new Response("OK");
        if (value != null) {
            r.value = value;
        }
        return new Gson().toJson(r);
    }
}
