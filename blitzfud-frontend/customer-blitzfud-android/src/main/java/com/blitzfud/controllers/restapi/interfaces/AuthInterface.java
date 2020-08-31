package com.blitzfud.controllers.restapi.interfaces;

import com.blitzfud.models.auth.User;
import com.blitzfud.models.responseAPI.ResponseAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthInterface {
    @FormUrlEncoded
    @POST("signin")
    Call<ResponseAPI> signIn(
            @Field("phoneNumber") String email,
            @Field("password") String password);

    @POST("signup")
    Call<ResponseAPI> signUp(
            @Body User user);
}
