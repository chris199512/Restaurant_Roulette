package com.smarthomebear.restaurantroulette;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouletteFragment extends Fragment {

    // variables
    ImageView imageViewRoulette;
    Button filterButton;
    Animation rotateImage;
    SeekBar priceSeekBar, metersSeekBar;

    //set filter variables
    private boolean nowOpenFilter = true;
    private int distanceFilter = 5000;//in meters
    private int priceFilter = 0;
    private double ratingFilter = 1.0;
    double ratingFilterPreferences;
    private List<String> selectedItems = new ArrayList<>();
    private String[][] res;
    Random r = new Random();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roulette, container, false);

        filterButton = view.findViewById(R.id.filter);
        imageViewRoulette = view.findViewById(R.id.roulette);
        imageViewRoulette.setOnClickListener(new View.OnClickListener() {
            @Override
            // disc rotation
            public void onClick(View view) {
                Animation rotateImage = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate);
                imageViewRoulette.startAnimation(rotateImage);

                StringBuilder stringBuilder=new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
                stringBuilder.append("&query=restaurant");
                stringBuilder.append("&location="+MainActivity.getLat()+","+MainActivity.getLng());
                stringBuilder.append("&radius="+distanceFilter);
                stringBuilder.append("&key="+BuildConfig.MAPS_API_KEY);

                String url=stringBuilder.toString();
                Object dataFetch[]=new Object[1];
                dataFetch[0]=url;

                FetchDataRoulette fetchData=new FetchDataRoulette();
                fetchData.execute(dataFetch);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        res=FetchDataRoulette.getRes();
                        int i1=r.nextInt(res.length);
                        AlertDialog.Builder restaurant=new AlertDialog.Builder(getActivity());
                        restaurant.setTitle(R.string.view_restaurant);
                        restaurant.setMessage(res[i1][1]+"\nAddress: "+res[i1][2]+"\nnow Open: "+res[i1][7]+"\nPrice: "+res[i1][8]+"\nRating: "+res[i1][9]);
                        restaurant.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        restaurant.setNegativeButton("Navigate", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Uri uri=Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+res[i1][2]+","+res[i1][3]);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setPackage("com.google.android.apps.maps");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        });
                        restaurant.create().show();
                    }
                }, 4000);
            }
        });

        //open filter button
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPreferencesDialog();
            }
        });


        return view;
    }


    private void showPreferencesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.fragment_preferences, null);
        builder.setView(dialogView);

        View view = dialogView.getRootView();
        priceSeekBar = view.findViewById(R.id.priceSeekBar);
        metersSeekBar = view.findViewById(R.id.metersSeekBar);
        TextView kilometerFilter=(TextView) view.findViewById(R.id.kilometer_filter);

        // meter seekbar
        metersSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceFilter = (progress + 1) *1000; // Wert in Metern (1 km = 1000 m)
int distance=distanceFilter/1000;
                kilometerFilter.setText( distance+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not required, can be left blank
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not required, can be left blank
            }
        });

        //price seekbar
        priceSeekBar.setMax(4); // four price classes, so set maximum value to 4
        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // eConversion of the progress (0-4) into the corresponding price class
                switch (progress) {
                    case 0:
                        priceFilter = 0; //(free)
                        break;
                    case 1:
                        priceFilter = 1; // € (cheap)
                        break;
                    case 2:
                        priceFilter = 2; // €€ (affordable)
                        break;
                    case 3:
                        priceFilter = 3; // €€€ (expensive)
                        break;
                    case 4:
                        priceFilter = 4; // €€€€ (very expensive)
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not required, can be left blank
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not required, can be left blank
            }
        });

        //rating with stars
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        ratingBar.setRating((float) ratingFilter);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingFilterPreferences = rating;

            }
        });

        //show open restautants
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //nowOpenFilter = true;
                        ratingFilter = ratingFilterPreferences;

                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }


                });

     
        builder.create().show();

    }

}


