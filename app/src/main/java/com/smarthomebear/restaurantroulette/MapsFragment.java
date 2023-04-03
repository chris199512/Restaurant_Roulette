package com.smarthomebear.restaurantroulette;

/*
This is an Android application code written in Java for a Restaurant Roulette app. The app uses Google Maps API to show the user's current location and nearby restaurants on the map.

The code starts by importing necessary classes and packages, such as the FusedLocationProviderClient, GoogleMap, and MarkerOptions. It then defines a MapsFragment class that extends Fragment and implements the OnMapReadyCallback interface.

In the onCreateView() method, the code inflates the fragment_maps layout and gets a reference to the Google Maps fragment using the SupportMapFragment class. It also initializes the restaurant button and fusedLocationClient.

When the user clicks the restaurant button, the app sends a request to the Google Places API to find nearby restaurants. The response is returned as a JSON object and is parsed by the FetchData class. The parsed data is then displayed on the map using markers.

In the onMapReady() method, the code checks for location permission, and if it is granted, it uses the FusedLocationProviderClient to get the user's last known location. It then sets the user's location as the center of the map and adds a marker at the location. The zoom level is set to 15.
 */


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private double lat, lng;

    ImageButton restaurant, bar;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //getPlaces
        restaurant=rootView.findViewById(R.id.map_restaurant_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location="+lat+","+lng);
                stringBuilder.append("&radius=10000");
                stringBuilder.append("&type=restaurant");
                stringBuilder.append("&sensor=true");
                stringBuilder.append("&key="+BuildConfig.MAPS_API_KEY);

                String url=stringBuilder.toString();
                Object dataFetch[]=new Object[2];
                dataFetch[0]=mMap;
                dataFetch[1]=url;

                FetchData fetchData=new FetchData();
                fetchData.execute(dataFetch);
            }
        });

        return rootView;
    }

    @Override
    //because of the missing permission error
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lat= location.getLatitude();
                            lng=location.getLongitude();
                            LatLng currentLocation = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        }
                    }
                });
    }
}