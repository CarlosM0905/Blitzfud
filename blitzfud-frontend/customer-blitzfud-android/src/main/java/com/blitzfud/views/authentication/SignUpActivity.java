package com.blitzfud.views.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.controllers.utilities.MyPreference;
import com.blitzfud.databinding.ActivitySignUpBinding;
import com.blitzfud.models.ResponseAPI;
import com.blitzfud.views.pages.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MAP_ACTIVITY_RESULT = 1;

    private ActivitySignUpBinding binding;
    private AlertDialog dialog;
    private String position;
    private LatLng positionLatLng;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        bindListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_ACTIVITY_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String positionExtra = data.getStringExtra("position");
                double positionLat = data.getDoubleExtra("positionLat", 0);
                double positionLng = data.getDoubleExtra("positionLng", 0);
                position = positionExtra;
                positionLatLng = new LatLng(positionLat, positionLng);
                binding.txtPosition.setText(position);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutPosition:
                redirectMap();
                break;
            case R.id.btnSignUp:
                clickSignUp();
                break;
            case R.id.btnSignIn:
                onBackPressed();
                break;
        }
    }

    private void initConfig() {
        BlitzfudUtils.initToolbar(this, "Registrarme", true);
        dialog = BlitzfudUtils.initLoading(this);
        prefs = getSharedPreferences(MyPreference.PREFERENCE_NAME, MODE_PRIVATE);
    }

    private void bindListeners() {
        binding.layoutPosition.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.btnSignIn.setOnClickListener(this);
    }

    private void redirectMap() {
        Intent intent = new Intent(SignUpActivity.this, MapActivity.class);
        startActivityForResult(intent, MAP_ACTIVITY_RESULT);
    }

    private void clickSignUp() {
        final String firstName = binding.txtFirstName.getText().toString();
        final String lastName = binding.txtLastName.getText().toString();
        final String email = binding.txtEmail.getText().toString();
        final String password = binding.txtPassword.getText().toString();
        final String confirmPassword = binding.txtConfirmPassword.getText().toString();

        if (isValid(firstName, lastName, email, password, confirmPassword)) {
            dialog.show();
            signUp(firstName, lastName, email, password);
        }
    }

    private void signUp(final String firstName, final String lastName, final String email,
                        final String password) {
        AuthService.signUp(firstName, lastName, email, password).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Ya puedes iniciar sesión " + firstName, Toast.LENGTH_SHORT).show();

                    MyPreference.saveLocation(prefs, positionLatLng, position);

                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    intent.putExtra("email", email);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    BlitzfudUtils.showError(SignUpActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(SignUpActivity.this);
            }
        });
    }

    private boolean isValid(final String firstName, final String lastName, final String email,
                            final String password, final String confirmPassword) {
        clearErrors();

        if (firstName.trim().isEmpty()) {
            binding.inputFirstName.setError("El nombre es necesario");
            binding.mainScroll.fullScroll(ScrollView.FOCUS_UP);

            return false;
        }

        if (lastName.trim().isEmpty()) {
            binding.inputLastName.setError("El apellido es necesario");
            binding.mainScroll.fullScroll(ScrollView.FOCUS_UP);

            return false;
        }

        if (email.trim().isEmpty()) {
            binding.inputEmail.setError("El email es necesario");
            binding.mainScroll.fullScroll(ScrollView.FOCUS_UP);

            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("No es un correo válido");

            return false;
        }

        if (password.trim().isEmpty()) {
            binding.inputPassword.setError("La contraseña es necesaria");

            return false;
        }

        if (password.length() < 5) {
            binding.inputPassword.setError("La contraseña es muy débil");

            return false;
        }

        if (confirmPassword.trim().isEmpty()) {
            binding.inputConfirmPassword.setError("Debe confirmar contraseña");

            return false;
        }

        if (confirmPassword.length() < 5) {
            binding.inputConfirmPassword.setError("La contraseña es muy débil");

            return false;
        }

        if (!password.equals(confirmPassword)) {
            binding.inputPassword.setError("La contraseña no coincide");
            binding.inputConfirmPassword.setError("La contraseña no coincide");

            return false;
        }

        if (position == null || positionLatLng == null) {
            new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Aviso")
                    .setContentText("Debes definir tu posición")
                    .show();
            return false;
        }

        return true;
    }

    private void clearErrors() {
        binding.inputFirstName.setError(null);
        binding.inputLastName.setError(null);
        binding.inputEmail.setError(null);
        binding.inputPassword.setError(null);
        binding.inputConfirmPassword.setError(null);
    }


}
