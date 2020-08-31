package com.blitzfud.views.authentication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.controllers.firebase.PhoneNumberProvider;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityVerifyPhoneBinding;
import com.blitzfud.models.auth.User;
import com.blitzfud.models.body.LocationAPI;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VerifyPhoneActivity";

    private ActivityVerifyPhoneBinding binding;
    private AlertDialog dialog;

    private PhoneNumberProvider phoneNumberProvider;
    private String verificationId;
    private FirebaseAuth auth;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private double ubicationLat;
    private double ubicationLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyPhoneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        loadIntent();
        sendVerificationCode();
        bindListeners();
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    private void initConfig() {
        phoneNumberProvider = new PhoneNumberProvider();
        dialog = BlitzfudUtils.initLoading(this);
        auth = FirebaseAuth.getInstance();
    }

    private void loadIntent() {
        final Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        address = intent.getStringExtra("address");
        ubicationLat = intent.getDoubleExtra("ubicationLat", 0);
        ubicationLng = intent.getDoubleExtra("ubicationLng", 0);

        Log.i(TAG, "phoneNumber: " + phoneNumber);
        Log.i(TAG, "firstName: " + firstName);
        Log.i(TAG, "lastName: " + lastName);
        Log.i(TAG, "email: " + email);
        Log.i(TAG, "password: " + password);
        Log.i(TAG, "address: " + address);
        Log.i(TAG, "ubicationLat: " + ubicationLat);
        Log.i(TAG, "ubicationLng: " + ubicationLng);
    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbackVerification
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            callbackVerification = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                binding.txtCodigo.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, "Deshabilitada hasta el siguiente sprint", Toast.LENGTH_LONG).show();
        }

    };

    private void bindListeners() {
        binding.imgBack.setOnClickListener(this);
        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.txtCodigo.getText().toString();

                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        if (isValid(code)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signUp(credential);
        }
    }

    private void signUp(PhoneAuthCredential credential) {
        dialog.show();

        signInFirebase(credential);
    }

    private void signInFirebase(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    auth.signOut();
                    signUpBackend();
                } else {
                    dialog.dismiss();
                    BlitzfudUtils.showError(VerifyPhoneActivity.this, "El código es incorrecto");
                }
            }
        });
    }

    private void signUpBackend() {
        final User user = new User(phoneNumber, email, firstName, lastName, password,
                new LocationAPI(address, ubicationLat, ubicationLng));

        AuthService.signUp(user).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    savePhoneOnFirebase();
                } else {
                    BlitzfudUtils.showErrorWithCatch(VerifyPhoneActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                dialog.dismiss();
                BlitzfudUtils.showFailure(VerifyPhoneActivity.this);
            }
        });
    }

    private void savePhoneOnFirebase() {
        phoneNumberProvider.create(phoneNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(VerifyPhoneActivity.this, "Ya puedes iniciar sesión " + firstName, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(VerifyPhoneActivity.this, SignInActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    BlitzfudUtils.showError(VerifyPhoneActivity.this, "No se pudo crear el usuario, inténtelo más tarde");
                }
            }
        });
    }

    private boolean isValid(String code) {
        binding.inputCodigo.setError(null);

        if (code.trim().isEmpty()) {
            binding.inputCodigo.setError("Ingrese código");
            return false;
        } else if (code.length() != 6) {
            binding.inputCodigo.setError("El código no tiene 6 dígitos");
            return false;
        }

        return true;
    }

}
