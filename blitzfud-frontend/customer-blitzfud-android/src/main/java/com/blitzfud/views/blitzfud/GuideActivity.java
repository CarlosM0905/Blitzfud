package com.blitzfud.views.blitzfud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.R;
import com.blitzfud.controllers.utilities.BlitzfudPreference;
import com.blitzfud.databinding.ActivityGuideBinding;
import com.blitzfud.views.authentication.SignInActivity;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityGuideBinding binding;
    private SharedPreferences prefs;
    private static final int[] images = new int[]{
            R.drawable.onboarding1,
            R.drawable.onboarding2,
            R.drawable.onboarding3,
            R.drawable.onboarding4,
            R.drawable.onboarding5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        prefs = getSharedPreferences(BlitzfudPreference.PREFERENCE_NAME, MODE_PRIVATE);

        loadImages();
        bindListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNext: nextActivity(); break;
            default: break;
        }
    }

    private void loadImages(){
        binding.carousel.setPageCount(images.length);
        binding.carousel.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.with(GuideActivity.this).load(images[position]).fit().centerInside().into(imageView);
            }
        });
    }

    private void bindListeners() {
        binding.btnNext.setOnClickListener(this);
    }

    private void nextActivity(){
        BlitzfudPreference.firstTime = false;
        BlitzfudPreference.savePreferences(prefs);
        startActivity(new Intent(GuideActivity.this, SignInActivity.class));
        finish();
    }

}
