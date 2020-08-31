package com.blitzfud.views.pages.map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.services.AuthService;
import com.blitzfud.controllers.utilities.BlitzfudPreference;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityMapBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.SphericalUtil;

import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SharedPreferences prefs;
    private ActivityMapBinding binding;
    private GoogleMap map;
    private LatLng mCurrentLatLng;
    private AutocompleteSupportFragment autocompleteDestination;
    private String address;
    private LatLng ubication;
    private GoogleMap.OnCameraIdleListener mCameraListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        instancePlaces();
        intanceAutocompleteDestination();
        instanceOnCameraListener();
        bindListener();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMinZoomPreference(10);
        map.setMaxZoomPreference(20);
        map.setOnCameraIdleListener(mCameraListener);

        if(AuthService.existsSession()){
            mCurrentLatLng = new LatLng(AuthService.getUser().getLocationLatLng().latitude,
                    AuthService.getUser().getLocationLatLng().longitude);
        }else{
            mCurrentLatLng = new LatLng(BlitzfudPreference.defaultUbication.latitude,
                    BlitzfudPreference.defaultUbication.longitude);
        }

        final CameraPosition camera = new CameraPosition.Builder()
                .target(mCurrentLatLng)
                .zoom(14)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        limitSearch();
    }

    private void initConfig(){
        BlitzfudUtils.initToolbar(this, "Define tu ubicación", true);
        prefs = getSharedPreferences(BlitzfudPreference.PREFERENCE_NAME, MODE_PRIVATE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void limitSearch() {
        LatLng northSide = SphericalUtil.computeOffset(mCurrentLatLng, 5000, 0);
        LatLng southSide = SphericalUtil.computeOffset(mCurrentLatLng, 5000, 180);
        autocompleteDestination.setCountry("PE");
        autocompleteDestination.setLocationBias(RectangularBounds.newInstance(southSide, northSide));
    }

    private void instancePlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }

        Places.createClient(this);
    }

    private void intanceAutocompleteDestination() {
        autocompleteDestination = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocompleteDestination);
        autocompleteDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteDestination.setHint("Ubicación");
        autocompleteDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                address = place.getName();
                ubication = place.getLatLng();

                map.moveCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                                .zoom(14f)
                                .tilt(30)
                                .build()
                ));
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    private void instanceOnCameraListener() {
        mCameraListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    Geocoder geocoder = new Geocoder(MapActivity.this);

                    ubication = map.getCameraPosition().target;
                    List<Address> addressList = geocoder.getFromLocation(ubication.latitude,
                            ubication.longitude, 1);
                    String city = addressList.get(0).getLocality();
                    address = addressList.get(0).getAddressLine(0)+" "+city;
                    autocompleteDestination.setText(address);

                } catch (Exception e) {
                }
            }
        };
    }

    private void bindListener() {
        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address != null) {
                    if (AuthService.existsSession()){
                        AuthService.getUser().getLocation().setAddress(address);
                        AuthService.getUser().getLocation().getCoordinates().set(0, ubication.longitude);
                        AuthService.getUser().getLocation().getCoordinates().set(1, ubication.latitude);
                        BlitzfudPreference.saveUser(prefs);
                    }

                    final Intent intent = new Intent();
                    intent.putExtra("address", address);
                    intent.putExtra("ubicationLat", ubication.latitude);
                    intent.putExtra("ubicationLng", ubication.longitude);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

}
