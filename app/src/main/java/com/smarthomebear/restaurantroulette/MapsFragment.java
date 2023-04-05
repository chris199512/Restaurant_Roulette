package com.smarthomebear.restaurantroulette;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private double lat, lng;
    private String fullAddress;
    private final static int REQUEST_CODE=100;

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

        //getRestaurants
        restaurant=rootView.findViewById(R.id.map_restaurant_button);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addLocation();

    }
    //get coordinates and add them to the map
    private void addLocation(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lat = MainActivity.getLat();
            lng = MainActivity.getLng();
            fullAddress=MainActivity.getFullAddress();
            LatLng currentLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(currentLocation).title(fullAddress));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

        }else{
            askPermission();

             }
    }

    //Ask for location authorization
    private void askPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    //If rejected, check again and report back
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                addLocation();
            }
            else{
                Toast.makeText(getContext(), R.string.req_permission, Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}