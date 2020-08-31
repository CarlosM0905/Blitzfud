package com.blitzfud.views.pages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.DBConnection;
import com.blitzfud.controllers.utilities.BlitzfudPreference;
import com.blitzfud.databinding.ActivityMainBinding;
import com.blitzfud.views.authentication.SignInActivity;
import com.blitzfud.views.fragments.account.AccountFragment;
import com.blitzfud.views.fragments.market.FavoriteMarketsFragment;
import com.blitzfud.views.fragments.market.MarketsFragment;
import com.blitzfud.views.fragments.order.OrdersFragment;
import com.blitzfud.views.fragments.shoppingCart.ShoppingCartFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

import io.realm.Realm;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_ACCOUNT;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_CART;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_FAVORITES;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_HOME;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.FRAGMENT_ORDERS;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.MAP_ACTIVITY_RESULT;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private String tagSelected = "";
    private Stack<String> stackFragments;
    private SharedPreferences prefs;
    private boolean logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        bindListener();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                changeFragment(new MarketsFragment(), FRAGMENT_HOME);
                return true;
            case R.id.nav_favorites:
                changeFragment(new FavoriteMarketsFragment(), FRAGMENT_FAVORITES);
                return true;
            case R.id.nav_orders:
                changeFragment(new OrdersFragment(), FRAGMENT_ORDERS);
                return true;
            case R.id.nav_cart:
                changeFragment(new ShoppingCartFragment(), FRAGMENT_CART);
                return true;
            case R.id.nav_account:
                changeFragment(new AccountFragment(), FRAGMENT_ACCOUNT);
                return true;
            default:
                return false;
        }
    }

    private void initConfig() {
        prefs = getSharedPreferences(BlitzfudPreference.PREFERENCE_NAME, MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();
        stackFragments = new Stack<>();
        changeFragment(new MarketsFragment(), FRAGMENT_HOME);
    }

    private void bindListener() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public void changeFragment(final Fragment fragment, final String tag) {
        if (!tagSelected.isEmpty()) {
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(tagSelected)).commit();
        }

        if (stackFragments.size() > 1) {
            for (int i = 0; i < stackFragments.size() - 1; i++) {
                final String popTag = stackFragments.pop();
                final Fragment fragmentRemove = fragmentManager.findFragmentByTag(popTag);
                fragmentManager.beginTransaction().remove(fragmentRemove).commit();
            }
        }

        final Fragment fragmentSelected = fragmentManager.findFragmentByTag(tag);

        if (fragmentSelected == null) {
            fragmentManager.beginTransaction().add(binding.contentFrame.getId(), fragment, tag).commit();
        } else {
            fragmentManager.beginTransaction().show(fragmentSelected).commit();
        }

        tagSelected = tag;
        stackFragments.clear();
        stackFragments.push(tagSelected);
    }

    public void changeFragment(final int selectedId) {
        binding.bottomNavigationView.setSelectedItemId(selectedId);
    }

    public void newFragment(final Fragment fragment, final String tag) {
        if (!tagSelected.isEmpty()) {
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(tagSelected)).commit();
        }

        stackFragments.push(tag);

        fragmentManager.beginTransaction().add(binding.contentFrame.getId(), fragment, tag).commit();
        tagSelected = tag;
    }

    public void closeFragment() {
        final String popTag = stackFragments.pop();
        final Fragment fragment = fragmentManager.findFragmentByTag(popTag);
        fragmentManager.beginTransaction().remove(fragment).commit();

        tagSelected = stackFragments.peek();

        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(tagSelected)).commit();
    }

    public void logout() {
        for (Fragment fragment : fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }

        logout = true;
        final Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_ACTIVITY_RESULT && resultCode == Activity.RESULT_OK &&
                tagSelected.equals(FRAGMENT_HOME)) {
            MarketsFragment marketsFragment = (MarketsFragment) fragmentManager
                    .findFragmentByTag(FRAGMENT_HOME);
            marketsFragment.reloadMarkets(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (stackFragments.size() > 1) {
            closeFragment();
        } else if (!tagSelected.equals(FRAGMENT_HOME)) {
            changeFragment(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (logout) {
            BlitzfudPreference.clearPreferences(prefs);
            DBConnection.clearDatabase(Realm.getDefaultInstance());
        }

    }
}
