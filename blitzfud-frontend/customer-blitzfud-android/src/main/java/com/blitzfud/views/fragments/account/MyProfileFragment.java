package com.blitzfud.views.fragments.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.databinding.FragmentMyProfileBinding;
import com.blitzfud.views.pages.MainActivity;

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentMyProfileBinding binding;

    public MyProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();
        bindListeners();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                ((MainActivity) getActivity()).closeFragment();
                break;
            case R.id.imgEditAvatar:
            case R.id.imgEditName:
            case R.id.imgEditEmail:
            case R.id.btnSave:
            default: break;
        }
    }

    private void loadData() {
        binding.txtFirstName.setText(AuthService.getUser().getFirstName());
        binding.txtLastName.setText(AuthService.getUser().getLastName());
//        binding.txtPhoneNumber.setText(AuthService.getUser().getPhoneNumber());
//        binding.txtEmail.setText(AuthService.getUser().getEmail());
    }

    private void bindListeners() {
        binding.imgBack.setOnClickListener(this);
        binding.imgEditAvatar.setOnClickListener(this);
        binding.imgEditName.setOnClickListener(this);
        binding.imgEditEmail.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
