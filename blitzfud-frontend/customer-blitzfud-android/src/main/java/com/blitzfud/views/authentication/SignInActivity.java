package com.blitzfud.views.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.controllers.utilities.MyPreference;
import com.blitzfud.databinding.ActivitySignInBinding;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.views.pages.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivitySignInBinding binding;
    private AlertDialog dialog;
    private String email;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        loadData();
        bindListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn: clickSignIn(); break;
            case R.id.btnSignUp: redirectSignUp(); break;
        }
    }

    private void initConfig(){
        dialog = BlitzfudUtils.initLoading(this);
        prefs = getSharedPreferences(MyPreference.PREFERENCE_NAME, MODE_PRIVATE);
    }

    private void loadData() {
        email = getIntent().getStringExtra("email");

        if (email != null) {
            binding.txtEmail.setText(email);
        }
    }

    private void bindListeners() {
        binding.btnSignIn.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
    }

    private void clickSignIn() {
        final String email = binding.txtEmail.getText().toString();
        final String password = binding.txtPassword.getText().toString();

        if (isValid(email, password)) {
            dialog.show();
            signIn(email, password);
        }
    }

    private void signIn(final String email, final String password){
        AuthService.signIn(email, password).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    MyPreference.savePreferences(prefs);
                    Toast.makeText(SignInActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    BlitzfudUtils.showError(SignInActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(SignInActivity.this);
            }
        });
    }

    private void redirectSignUp(){
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    private boolean isValid(final String email, final String password) {
        clearErrors();

        if (email.trim().isEmpty()) {
            binding.inputEmail.setError("El email es necesario");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("No es un correo válido");

            return false;
        }

        if (password.trim().isEmpty()) {
            binding.inputPassword.setError("La contraseña es necesario");
            return false;
        }

        return true;
    }

    private void clearErrors() {
        binding.inputEmail.setError(null);
        binding.inputPassword.setError(null);
    }

}
