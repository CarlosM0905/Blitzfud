package com.blitzfud.views.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.MyPreference;
import com.blitzfud.views.authentication.SignInActivity;
import com.blitzfud.views.fragments.market.FavoriteMarketsFragment;
import com.blitzfud.views.fragments.order.HistoryOrdersFragment;
import com.blitzfud.views.fragments.market.MarketsFragment;
import com.blitzfud.views.fragments.settings.MyProfileFragment;
import com.blitzfud.views.fragments.order.PendingOrdersFragment;
import com.blitzfud.views.pages.shoppingCart.ShoppingCartActivity;
import com.blitzfud.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final int MAP_ACTIVITY_RESULT = 1;

    private static MenuItem itemSelected;
    private static String titleAppbar;

    private ActivityMainBinding binding;
    private SharedPreferences prefs;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment fragment;
    private TextView txtFullName;
    private ImageView imgBlitzfud;
    private int countTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        prefs = getSharedPreferences(MyPreference.PREFERENCE_NAME, MODE_PRIVATE);

        bindUI();
        showFirstFragment(savedInstanceState);
        bindListeners();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (titleAppbar == null || titleAppbar.equals(MyPreference.position)) {
            getMenuInflater().inflate(R.menu.menu_w_position, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.optMyPosition:
                redirectMyProfile();
                return true;
            case R.id.optMyOrder:
                redirectMyOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAllMarkets:
                fragment = new MarketsFragment();
                break;
            case R.id.menuFavoriteMarkets:
                fragment = new FavoriteMarketsFragment();
                break;
            case R.id.menuPendingOrders:
                fragment = new PendingOrdersFragment();
                break;
            case R.id.menuHistoryOrders:
                fragment = new HistoryOrdersFragment();
                break;
            case R.id.menuMyProfile:
                fragment = new MyProfileFragment();
                break;
            case R.id.menuLogout:
                logout();
                return true;
        }

        changeFragment(fragment, item);
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBlitzfud: easterEgg(); break;
        }
    }

    private void bindUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navview);
        View view = navigationView.getHeaderView(0);
        txtFullName = view.findViewById(R.id.txtFullName);
        imgBlitzfud = view.findViewById(R.id.imgBlitzfud);
        navigationView.setItemIconTintList(null);

        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (titleAppbar != null) {
            getSupportActionBar().setTitle(titleAppbar);
            if (titleAppbar.equals(MyPreference.position)) {
                itemSelected = navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
            }
        }
    }

    private void showFirstFragment(Bundle bundle) {
        if (bundle == null) {
            changeFragment(new MarketsFragment(), navigationView.getMenu().getItem(0).getSubMenu().getItem(0));
        }
    }

    private void bindListeners() {
        imgBlitzfud.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadData(){
        txtFullName.setText(AuthService.getUser().getFirstName());
    }

    private void easterEgg(){
        countTap++;

        if (countTap == 5) {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("IN MEMORIAM")
                    .setContentText("Encontraste al Don Pepucho de los proyectos, sacarás un 20")
                    .setCustomImage(R.drawable.pepucho1)
                    .show();
        }

        if (countTap > 10) {
            countTap = 0;

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Tilín")
                    .setContentText("Ya deja a Don Pepucho en paz")
                    .setCustomImage(R.drawable.pepucho2)
                    .show();
        }
    }

    public void changeFragment(Fragment fragment, MenuItem item) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        if (itemSelected != null) itemSelected.setChecked(false);

        itemSelected = item;
        itemSelected.setChecked(true);

        if (fragment instanceof MarketsFragment) {
            titleAppbar = MyPreference.position;
        } else {
            titleAppbar = item.getTitle().toString();
        }

        getSupportActionBar().setTitle(titleAppbar);

        invalidateOptionsMenu();
    }

    private void redirectMyProfile() {
        changeFragment(fragment = new MyProfileFragment(), navigationView.getMenu().getItem(2).getSubMenu().getItem(0));
    }

    private void redirectMyOrder() {
        startActivity(new Intent(MainActivity.this, ShoppingCartActivity.class));
    }

    private void logout() {
        MyPreference.clearPreferences(prefs);
        final Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_ACTIVITY_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String positionExtra = data.getStringExtra("position");
                double positionLat = data.getDoubleExtra("positionLat", 0);
                double positionLng = data.getDoubleExtra("positionLng", 0);

                if (fragment instanceof MyProfileFragment) {
                    ((MyProfileFragment) fragment).updateView(positionExtra, new LatLng(positionLat, positionLng));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        if (!getSupportActionBar().getTitle().toString().equals(MyPreference.position)) {
            changeFragment(new MarketsFragment(), navigationView.getMenu().getItem(0).getSubMenu().getItem(0));
        } else {
            super.onBackPressed();
        }
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}
