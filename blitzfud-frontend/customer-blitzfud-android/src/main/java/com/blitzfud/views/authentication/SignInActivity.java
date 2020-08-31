package com.blitzfud.views.authentication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.BlitzfudPreference;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivitySignInBinding;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.views.pages.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignInBinding binding;
    private AlertDialog dialog;
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
        switch (v.getId()) {
            case R.id.btnSignIn:
                clickSignIn();
                break;
            case R.id.btnSignUp:
                redirectSignUp();
                break;
            default: break;
        }
    }

    private void initConfig() {
        dialog = BlitzfudUtils.initLoading(this);
        prefs = getSharedPreferences(BlitzfudPreference.PREFERENCE_NAME, MODE_PRIVATE);
    }

    private void loadData() {
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        if (phoneNumber != null) {
            binding.txtPhoneNumber.setText(phoneNumber.substring(3));
        }

    }

    private void bindListeners() {
        binding.btnSignIn.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
    }

    private void clickSignIn() {
        final String phoneNumber = "+51" + binding.txtPhoneNumber.getText().toString();
        final String password = binding.txtPassword.getText().toString();

        if (isValid(phoneNumber, password)) {
            dialog.show();
            signIn(phoneNumber, password);
        }
    }

    private void signIn(final String phoneNumber, final String password) {
        AuthService.signIn(phoneNumber, password).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    BlitzfudPreference.savePreferences(prefs);
                    Toast.makeText(SignInActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    BlitzfudUtils.showErrorWithCatch(SignInActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(SignInActivity.this);
            }
        });
    }

    private void redirectSignUp() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    private boolean isValid(final String phoneNumber, final String password) {
        clearErrors();

        if (phoneNumber.equals("+51")) {
            binding.inputPhoneNumber.setError("El número es necesario");
            return false;
        }

        if (phoneNumber.length() != 12) {
            binding.inputPhoneNumber.setError("El número no tiene 9 dígitos");

            return false;
        }

        if (password.trim().isEmpty()) {
            binding.inputPassword.setError("La contraseña es necesario");
            return false;
        }

        return true;
    }

    private void clearErrors() {
        binding.inputPhoneNumber.setError(null);
        binding.inputPassword.setError(null);
    }

}
