package com.blitzfud.views.blitzfud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.MyPreference;
import com.blitzfud.views.authentication.SignInActivity;
import com.blitzfud.views.pages.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences(MyPreference.PREFERENCE_NAME, MODE_PRIVATE);
        MyPreference.loadPreferences(prefs);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;

                if(MyPreference.firstTime){
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                }else{
                    if(!AuthService.existsSession()){
                        intent = new Intent(SplashActivity.this, SignInActivity.class);
                    }else{
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }
                }

                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
