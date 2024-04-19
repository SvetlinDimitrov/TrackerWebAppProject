package org.gateway.utils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class GsonWrapper {
    private final Gson gson = new Gson();

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}