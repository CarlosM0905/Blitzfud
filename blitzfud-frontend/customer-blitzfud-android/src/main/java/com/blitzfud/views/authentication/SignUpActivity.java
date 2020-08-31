package com.blitzfud.views.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.R;
import com.blitzfud.controllers.firebase.PhoneNumberProvider;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivitySignUpBinding;
import com.blitzfud.views.pages.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.MAP_ACTIVITY_RESULT;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding binding;
    private PhoneNumberProvider phoneNumberProvider;
    private AlertDialog dialog;
    private String address;
    private LatLng ubication;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_ACTIVITY_RESULT && resultCode == Activity.RESULT_OK) {
            String positionExtra = data.getStringExtra("address");
            double positionLat = data.getDoubleExtra("ubicationLat", 0);
            double positionLng = data.getDoubleExtra("ubicationLng", 0);
            address = positionExtra;
            ubication = new LatLng(positionLat, positionLng);
            binding.txtPosition.setText(address);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
            case R.id.btnSignIn:
                onBackPressed();
                break;
            case R.id.layoutPosition:
                redirectMap();
                break;
            case R.id.btnSignUp:
                clickSignUp();
                break;
            default : break;
        }
    }

    private void initConfig() {
        dialog = BlitzfudUtils.initLoading(this);
        phoneNumberProvider = new PhoneNumberProvider();
    }

    private void bindListeners() {
        binding.layoutPosition.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
    }

    private void redirectMap() {
        Intent intent = new Intent(SignUpActivity.this, MapActivity.class);
        startActivityForResult(intent, MAP_ACTIVITY_RESULT);
    }

    private void clickSignUp() {
        final String phoneNumber = "+51" + binding.txtPhoneNumber.getText().toString();
        final String firstName = binding.txtFirstName.getText().toString();
        final String lastName = binding.txtLastName.getText().toString();
        final String email = binding.txtEmail.getText().toString();
        final String password = binding.txtPassword.getText().toString();
        final String confirmPassword = binding.txtConfirmPassword.getText().toString();

        if (isValid(phoneNumber, firstName, lastName, email, password, confirmPassword)) {
            dialog.show();
            signUp(phoneNumber, firstName, lastName, email, password);
        }
    }

    private void signUp(final String phoneNumber, final String firstName, final String lastName,
                        final String email, final String password) {

        dialog.show();
        phoneNumberProvider.getPhoneNumber(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                if (!dataSnapshot.exists()) {
                    Intent intent = new Intent(SignUpActivity.this, VerifyPhoneActivity.class);

                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("address", address);
                    intent.putExtra("ubicationLat", ubication.latitude);
                    intent.putExtra("ubicationLng", ubication.longitude);

                    startActivity(intent);
                } else {
                    BlitzfudUtils.showError(SignUpActivity.this, "El número ya está registrado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                BlitzfudUtils.showError(SignUpActivity.this, "No se pudo conectar al servidor, inténtelo más tarde");
            }
        });
    }

    private boolean isValid(final String phoneNumber, final String firstName, final String lastName, final String email,
                            final String password, final String confirmPassword) {
        clearErrors();

        if (phoneNumber.equals("+51")) {
            binding.inputPhoneNumber.setError("El número es necesario");
            binding.mainScroll.fullScroll(ScrollView.FOCUS_UP);

            return false;
        } else if (phoneNumber.trim().length() != 12) {
            binding.inputPhoneNumber.setError("El número no contiene 9 dígitos");
            binding.mainScroll.fullScroll(ScrollView.FOCUS_UP);

            return false;
        }

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

        if (password.trim().isEmpty()) {
            binding.inputPassword.setError("La contraseña es necesaria");

            return false;
        } else if (password.length() < 5) {
            binding.inputPassword.setError("La contraseña es muy débil");

            return false;
        }

        if (confirmPassword.trim().isEmpty()) {
            binding.inputConfirmPassword.setError("Debe confirmar contraseña");

            return false;
        } else if (confirmPassword.length() < 5) {
            binding.inputConfirmPassword.setError("La contraseña es muy débil");

            return false;
        }

        if (!password.equals(confirmPassword)) {
            binding.inputPassword.setError("La contraseña no coincide");
            binding.inputConfirmPassword.setError("La contraseña no coincide");

            return false;
        }

        if (address == null || ubication == null) {
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
