package com.example.projektwaluty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_,container,false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng l1 = new LatLng(53.136742185125594, 23.15185847003022);
                LatLng l2 = new LatLng(53.12813003697529, 23.15386018951748);
                LatLng l3 = new LatLng(53.13261447796903, 23.135977260171106);
                LatLng l4 = new LatLng(53.14470140141968, 23.15825436627927);
                LatLng l5 = new LatLng(53.13380388275585, 23.169899786343308);
                LatLng l6 = new LatLng(53.1233972833588, 23.17956506463052);
                LatLng l7 = new LatLng(53.133383191771586, 23.15461437781164);
                LatLng l8 = new LatLng(53.102957743543655, 23.143697371237415);

                googleMap.addMarker(new MarkerOptions().position(l1));
                googleMap.addMarker(new MarkerOptions().position(l2));
                googleMap.addMarker(new MarkerOptions().position(l3));
                googleMap.addMarker(new MarkerOptions().position(l4));
                googleMap.addMarker(new MarkerOptions().position(l5));
                googleMap.addMarker(new MarkerOptions().position(l6));
                googleMap.addMarker(new MarkerOptions().position(l7));
                googleMap.addMarker(new MarkerOptions().position(l8));


                LatLng startPosition = new LatLng(53.11717845392099, 23.146372673599448);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPosition,13));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("fajno");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
        return view;
    }

}