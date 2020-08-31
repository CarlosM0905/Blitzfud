package com.blitzfud.controllers.restapi.deserializers.auth;

import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.models.auth.User;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class SignInDeserializer implements JsonDeserializer<ResponseAPI> {
    @Override
    public ResponseAPI deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        Gson gson = new Gson();

        String userString = jo.get("user").getAsJsonObject().toString();
        final User user = gson.fromJson(userString, User.class);
        final String token = jo.get("token").getAsString();

        AuthService.setToken(token);
        AuthService.setUser(user);

        return new ResponseAPI("Bienvenido "+user.getFirstName());
    }
}
