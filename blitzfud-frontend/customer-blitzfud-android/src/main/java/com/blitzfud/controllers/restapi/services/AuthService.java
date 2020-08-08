package com.blitzfud.controllers.restapi.services;

import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.deserializers.auth.SignInDeserializer;
import com.blitzfud.controllers.restapi.interfaces.AuthInterface;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.models.User;

import retrofit2.Call;

public class AuthService {
    private static final String URL = "auth/";
    private static String token;
    private static User user;

    public static Call<ResponseAPI> signIn(String email, String password) {
        AuthInterface userInterface = API.createService(AuthInterface.class, ResponseAPI.class,
                new SignInDeserializer(), URL);

        return userInterface.signIn(email, password);
    }

    public static Call<ResponseAPI> signUp(String firstName, String lastName,
                                           String email, String password) {
        AuthInterface userInterface = API.createService(AuthInterface.class, URL);

        return userInterface.signUp(firstName, lastName, email, password);
    }


    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AuthService.token = token;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        AuthService.user = user;
    }

    public static boolean existsSession(){
        if(token == null){
            return false;
        }

        return !token.equals("none");
    }
}
