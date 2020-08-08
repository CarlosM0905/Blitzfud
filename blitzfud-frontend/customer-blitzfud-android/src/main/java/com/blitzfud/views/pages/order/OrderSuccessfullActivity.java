package com.blitzfud.views.pages.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blitzfud.databinding.ActivityOrderSuccessfullBinding;
import com.blitzfud.views.pages.MainActivity;

public class OrderSuccessfullActivity extends AppCompatActivity {

    private ActivityOrderSuccessfullBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSuccessfullBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bindListeners();
    }

    private void bindListeners() {
        binding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(OrderSuccessfullActivity.this,
                        MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


}
