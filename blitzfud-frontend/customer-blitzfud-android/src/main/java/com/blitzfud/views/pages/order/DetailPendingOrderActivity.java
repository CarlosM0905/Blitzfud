package com.blitzfud.views.pages.order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityDetailPendingOrderBinding;

public class DetailPendingOrderActivity extends AppCompatActivity {

    private ActivityDetailPendingOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPendingOrderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        BlitzfudUtils.initToolbar(this, "Detalles de mi orden", true);
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
}
