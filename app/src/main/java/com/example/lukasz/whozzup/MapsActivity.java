package com.example.lukasz.whozzup;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapLongClickListener {

    private GoogleMap mMap;
    Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(22, 114.9),
                new LatLng(22.5, 114.4)));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng loc = place.getLatLng();
                if (mMarker != null)
                    mMarker.remove();
                mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Your Event"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
            }

            @Override
            public void onError(Status status) {
            }
        });

        Button b = (Button) findViewById(R.id.confirm_location);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMarker != null) {
                    double[] result = new double[2];
                    result[0] = mMarker.getPosition().longitude;
                    result[1] = mMarker.getPosition().latitude;
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", result);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    System.out.println("it's null");
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(22.2783, 114.1747);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
        mMap.setOnMapLongClickListener(MapsActivity.this);
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (mMarker != null)
            mMarker.remove();
        mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Your Event"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
    }
}
