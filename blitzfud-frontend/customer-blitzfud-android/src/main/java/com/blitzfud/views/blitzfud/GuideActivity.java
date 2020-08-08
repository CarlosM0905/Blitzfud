package com.blitzfud.views.blitzfud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blitzfud.R;
import com.blitzfud.controllers.utilities.MyPreference;
import com.blitzfud.databinding.ActivityGuideBinding;
import com.blitzfud.views.authentication.SignInActivity;
import com.synnapps.carouselview.ImageListener;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityGuideBinding binding;
    private SharedPreferences prefs;
    private static final int[] images = new int[]{
            R.drawable.image_default,
            R.drawable.image_default,
            R.drawable.image_default
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        prefs = getSharedPreferences(MyPreference.PREFERENCE_NAME, MODE_PRIVATE);

        loadImages();
        bindListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext: nextActivity(); break;
        }
    }

    private void loadImages(){
        binding.carousel.setPageCount(images.length);
        binding.carousel.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(images[position]);
            }
        });
    }

    private void bindListeners() {
        binding.btnNext.setOnClickListener(this);
    }

    private void nextActivity(){
        MyPreference.firstTime = false;
        MyPreference.savePreferences(prefs);
        startActivity(new Intent(GuideActivity.this, SignInActivity.class));
        finish();
    }

}
