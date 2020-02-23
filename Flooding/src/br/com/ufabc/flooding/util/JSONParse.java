package br.com.ufabc.flooding.util;

import br.com.ufabc.flooding.model.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class JSONParse {

    private JSONParse() {
    }

    public static String requestToJson(Request message) {
        Type type = new TypeToken<Request>() {
        }.getType();
        Gson gson = new Gson();
        return gson.toJson(message, type);
    }

    public static Request jsonToRequest(String json) {
        try {
            Type type = new TypeToken<Request>() {}.getType();
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (ClassCastException e) {
            System.out.println(e.getCause().getMessage() + "\n12\n");
            throw new RuntimeException(e);
        }
    }
}
