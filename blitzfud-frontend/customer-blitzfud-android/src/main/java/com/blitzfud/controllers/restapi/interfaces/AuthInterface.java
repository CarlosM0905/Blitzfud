package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.ResponseAPI;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthInterface {
    @FormUrlEncoded
    @POST("signin")
    Call<ResponseAPI> signIn(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<ResponseAPI> signUp(
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("email") String email,
            @Field("password") String password);
}
