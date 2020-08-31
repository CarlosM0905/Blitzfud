package com.blitzfud.controllers.restapi;

import com.blitzfud.controllers.restapi.interfaces.APIInterface;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    public static String DOMINIO;
    public static String BASE_URL;
    private static Retrofit retrofit;

    public static <T> T createService(Class<T> serviceClass) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(serviceClass);
    }

    public static <T, E> T createService(Class<T> serviceClass, String URL) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

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


    public static Call<ResponseAPI> wakeup() {
        APIInterface apiInterface = createService(APIInterface.class);

        return apiInterface.wakeup();
    }
}
