package com.blitzfud.views.fragments.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.databinding.FragmentAccountBinding;
import com.blitzfud.views.pages.MainActivity;
import com.blitzfud.views.pages.order.HistoryOrdersActivity;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_MY_PROFILE;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private FragmentAccountBinding binding;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();
        bindListeners();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutMyProfile:
                goToMyProfile();
                break;
            case R.id.layoutHistoryOrders:
                goToMyHistoryOrders();
                break;
            case R.id.btnLogout:
                ((MainActivity) getActivity()).logout();
                break;
            default:
                break;
        }
    }

    private void loadData() {
        binding.txtFirstName.setText(AuthService.getUser().getFirstName());
        binding.txtLastName.setText(AuthService.getUser().getLastName());
    }

    private void bindListeners() {
        binding.layoutMyProfile.setOnClickListener(this);
        binding.layoutHistoryOrders.setOnClickListener(this);
        binding.btnLogout.setOnClickListener(this);
    }

    private void goToMyProfile() {
        ((MainActivity)getActivity()).newFragment(new MyProfileFragment(), FRAGMENT_MY_PROFILE);
    }

    private void goToMyHistoryOrders() {
        startActivity(new Intent(getContext(), HistoryOrdersActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
