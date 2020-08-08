package com.blitzfud.controllers.restapi;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    //    public static final String DOMINIO = "http://192.168.0.32:3005";
    //public static final String DOMINIO = "https://customer-blitzfud-api.herokuapp.com";
    public static final String DOMINIO = "https://dev-customer-blitzfud-api.herokuapp.com";
    public static final String BASE_URL = DOMINIO + "/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <T> T createService(Class<T> serviceClass) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(serviceClass);
    }

    public static <T, E> T createService(Class<T> serviceClass, String URL) {
        builder = new Retrofit.Builder()
                .baseUrl(BASE_URL + URL)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static <T, E, F> T createService(Class<T> serviceClass, Class<E> clase, F deserializador) {
        GsonBuilder builderGson = new GsonBuilder();
        builderGson.registerTypeAdapter(clase, deserializador);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builderGson.create())).build();

        return retrofit.create(serviceClass);
    }

    public static <T, E, F> T createService(Class<T> serviceClass, Class<E> clase, F deserializador, String URL) {
        GsonBuilder builderGson = new GsonBuilder();
        builderGson.registerTypeAdapter(clase, deserializador);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + URL)
                .addConverterFactory(GsonConverterFactory.create(builderGson.create())).build();

        return retrofit.create(serviceClass);
    }
}
