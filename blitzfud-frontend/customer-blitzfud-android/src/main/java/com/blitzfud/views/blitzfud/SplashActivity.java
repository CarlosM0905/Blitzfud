package com.blitzfud.views.blitzfud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.DBConnection;
import com.blitzfud.controllers.localDB.providers.MarketsDBProvider;
import com.blitzfud.controllers.localDB.providers.ShoppingCartDBProvider;
import com.blitzfud.controllers.restapi.API;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.restapi.services.MarketService;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudPreference;
import com.blitzfud.models.responseAPI.MarketSet;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.responseAPI.ShoppingCartSet;
import com.blitzfud.views.authentication.SignInActivity;
import com.blitzfud.views.fragments.market.MarketsFragment;
import com.blitzfud.views.fragments.shoppingCart.ShoppingCartFragment;
import com.blitzfud.views.pages.MainActivity;
import com.google.gson.Gson;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.TOKEN_FAILED;

public class SplashActivity extends AppCompatActivity {

    private Realm realm;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences(BlitzfudPreference.PREFERENCE_NAME, MODE_PRIVATE);

        BlitzfudPreference.loadPreferences(prefs);

        if (BlitzfudPreference.firstTime || !AuthService.existsSession()) {
            wakeup(BlitzfudPreference.firstTime);
        } else {
            initialData();
        }
    }

    private void wakeup(final boolean firstTime) {
        API.wakeup().enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                redirectSignOrGuide(firstTime);
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                redirectSignOrGuide(firstTime);
            }
        });
    }

    private void redirectSignOrGuide(boolean firstTime) {
        final Intent intent;

        if (firstTime) intent = new Intent(this, GuideActivity.class);
        else intent = new Intent(this, SignInActivity.class);

        startActivity(intent);
        finish();
    }

    private void initialData() {
        realm = Realm.getDefaultInstance();
        loadShoppingCart();
    }

    private void loadMarkets() {
        MarketService.getAll(AuthService.getUser().getLocationLatLng()).enqueue(new Callback<MarketSet>() {
            @Override
            public void onResponse(Call<MarketSet> call, Response<MarketSet> response) {
                if (response.isSuccessful()) {
                    MarketsDBProvider.save(realm, response.body());
                    MarketsFragment.loadedFromAPI = true;

                }

                redirectMain();
            }

            @Override
            public void onFailure(Call<MarketSet> call, Throwable t) {
                redirectMain();
            }
        });
    }

    private void loadShoppingCart() {
        ShoppingCartService.getAll().enqueue(new Callback<ShoppingCartSet>() {
            @Override
            public void onResponse(Call<ShoppingCartSet> call, Response<ShoppingCartSet> response) {
                if (response.isSuccessful()) {
                    ShoppingCartDBProvider.save(realm, response.body());
                    ShoppingCartFragment.loadedFromAPI = true;

                    loadMarkets();
                } else {
                    try {
                        ResponseAPI error = new Gson().fromJson(response.errorBody().string(),
                                ResponseAPI.class);

                        if(error.getMessage().equals(TOKEN_FAILED)){
                            BlitzfudPreference.clearPreferences(prefs);
                            DBConnection.clearDatabase(realm);

                            Toast.makeText(SplashActivity.this,
                                    "La sesión ha caducado, por favor, vuelva a iniciar sesión",
                                    Toast.LENGTH_LONG).show();

                            realm.close();
                            redirectSignOrGuide(false);
                        }else{
                            redirectMain();
                        }
                    } catch (Exception e) {
                        redirectMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartSet> call, Throwable t) {
                redirectMain();
            }
        });
    }

    private void redirectMain() {
        realm.close();
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

}
