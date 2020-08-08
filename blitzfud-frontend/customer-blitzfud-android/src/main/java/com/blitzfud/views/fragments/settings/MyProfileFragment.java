package com.blitzfud.views.fragments.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blitzfud.R;
import com.blitzfud.controllers.utilities.MyPreference;
import com.blitzfud.databinding.FragmentMyProfileBinding;
import com.blitzfud.views.pages.map.MapActivity;
import com.google.android.gms.maps.model.LatLng;

import static android.content.Context.MODE_PRIVATE;

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentMyProfileBinding binding;
    private final int MAP_ACTIVITY_RESULT = 1;
    private String position;
    private LatLng positionLatLng;
    private SharedPreferences prefs;

    public MyProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        prefs = getContext().getSharedPreferences(MyPreference.PREFERENCE_NAME, MODE_PRIVATE);

        loadData();
        bindListeners();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAccept:
                saveChanges();
                break;
            case R.id.layoutPosition:
                redirectMap();
                break;
        }
    }

    private void loadData() {
        binding.txtPosition.setText(MyPreference.position);
    }

    private void bindListeners() {
        binding.btnAccept.setOnClickListener(this);
        binding.layoutPosition.setOnClickListener(this);
    }

    private void redirectMap() {
        Intent i = new Intent(getContext(), MapActivity.class);
        getActivity().startActivityForResult(i, MAP_ACTIVITY_RESULT);
    }

    private void saveChanges() {
        if (positionLatLng == null || position == null) return;

        if (!MyPreference.position.equals(position)) {
            Toast.makeText(getContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
            MyPreference.saveLocation(prefs, positionLatLng, position);
        }
    }

    public void updateView(final String positionExtra, final LatLng p) {
        position = positionExtra;
        positionLatLng = new LatLng(p.latitude, p.longitude);
        binding.txtPosition.setText(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
